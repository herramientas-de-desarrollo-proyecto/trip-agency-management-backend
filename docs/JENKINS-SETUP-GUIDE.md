# Guia de configuracion de Jenkins CI/CD

Guia paso a paso para replicar el pipeline de Jenkins en cualquier proyecto Spring Boot.

## Tabla de contenido

1. [Prerequisitos en el VPS](#1-prerequisitos-en-el-vps)
2. [Configuracion unica de Jenkins](#2-configuracion-unica-de-jenkins)
3. [Configuracion por proyecto](#3-configuracion-por-proyecto)
4. [Archivos del repositorio](#4-archivos-del-repositorio)
5. [Verificacion](#5-verificacion)
6. [Solucion de problemas](#6-solucion-de-problemas)

---

## 1. Prerequisitos en el VPS

Estos pasos se hacen **una sola vez** al instalar Jenkins.

### 1.1 Contenedor de Jenkins con Docker socket

Jenkins debe tener acceso al Docker del host para levantar contenedores sidecar (PostgreSQL, etc).

```bash
sudo docker run -d \
  --name jenkins \
  --restart unless-stopped \
  -p 8080:8080 -p 50000:50000 \
  -v jenkins_home:/var/jenkins_home \
  -v /var/run/docker.sock:/var/run/docker.sock \
  jenkins/jenkins:lts-jdk21
```

Si Jenkins ya esta corriendo, verificar si el socket esta montado:

```bash
sudo docker inspect jenkins --format '{{json .Mounts}}'
```

Buscar `"/var/run/docker.sock"` en la salida. Si no aparece, hay que recrear el contenedor (los datos se conservan en el volumen `jenkins_home` o `jenkins_jenkins_home`).

### 1.2 Instalar Docker CLI dentro de Jenkins

```bash
sudo docker exec -u root jenkins bash -c "apt-get update && apt-get install -y docker.io"
sudo docker exec -u root jenkins bash -c "usermod -aG docker jenkins"
sudo docker restart jenkins
```

Verificar:

```bash
sudo docker exec jenkins docker ps
```

### 1.3 Conectar Jenkins a la red Docker compartida

Si SonarQube u otros servicios corren en una red Docker (ej: `net-publica`):

```bash
sudo docker network connect net-publica jenkins
```

---

## 2. Configuracion unica de Jenkins

Estos pasos se hacen **una sola vez** en la UI de Jenkins.

### 2.1 Plugins necesarios

Manage Jenkins -> Plugins -> Available -> instalar:

| Plugin | Para que sirve |
|--------|---------------|
| **Docker Pipeline** | Levantar contenedores sidecar desde el Jenkinsfile |
| **Docker** | Soporte base de Docker |
| **Pipeline: Stage View** | Visualizar stages como cajitas en el job |
| **Embeddable Build Status** | Badge de Jenkins para el README |

### 2.2 Configurar JDK 21

1. Manage Jenkins -> Tools -> JDK installations
2. Add JDK:
   - **Name:** `jdk-21` (debe coincidir con el Jenkinsfile)
   - Configurar instalador de Adoptium/Temurin o apuntar a la ruta local

### 2.3 Token clasico de GitHub

Crear **un solo token** que sirve para todos los repos:

1. GitHub -> avatar -> Settings -> Developer settings -> Personal access tokens -> **Tokens (classic)**
2. Generate new token (classic):
   - **Note:** `jenkins`
   - **Expiration:** 1 anio
   - **Scopes:** `repo:status` (para checks en PRs)
3. Copiar el token

**Importante:** Usar token clasico, NO fine-grained. Los fine-grained no funcionan bien con repos de organizaciones.

### 2.4 Credenciales en Jenkins

Manage Jenkins -> Credentials -> Global -> + Add Credentials:

| ID | Kind | Valor | Para que sirve |
|----|------|-------|---------------|
| `github-readonly` | Username with password | user: tu GitHub username, password: el classic token | Branch Source (leer repos) |
| `github-status-token` | Secret text | el classic token | Commit status checks en PRs |
| `sonarqube-token` | Secret text | token de SonarQube | Analisis de calidad |

Para obtener el token de SonarQube:
1. SonarQube -> avatar -> My Account -> Security
2. Generate Tokens -> nombre: `jenkins`, tipo: Global Analysis Token -> Generate
3. Copiar el token

### 2.5 GitHub API rate limit

Manage Jenkins -> System -> GitHub API usage -> seleccionar **"Never check rate limit"**

Nota: aparece como "not recommended" pero para pocos repos no causa problemas. Si alguna vez da error 403 de rate limit, cambiar a "Throttle at/near rate limit".

---

## 3. Configuracion por proyecto

Estos pasos se repiten para **cada nuevo proyecto**.

### 3.1 Crear Multibranch Pipeline job

1. Jenkins -> + New Item
2. **Nombre:** nombre del repo (ej: `trip-agency-management-backend`)
3. Tipo: **Multibranch Pipeline** -> OK
4. Configuracion:

**Branch Sources:**
- Add source -> GitHub
- **Repository HTTPS URL:** `https://github.com/tu-org/tu-repo.git`
- **Credentials:** seleccionar `github-readonly`

**Behaviors:**
- Verificar que este **Discover branches**
- Agregar **Discover pull requests from origin** -> Strategy: The current pull request revision

**Build Configuration:**
- Mode: by Jenkinsfile
- Script Path: `Jenkinsfile`

5. Save

Jenkins hara un scan automatico y detectara ramas con Jenkinsfile.

### 3.2 Configurar webhook en GitHub

1. GitHub -> tu repo -> Settings -> Webhooks -> Add webhook
2. Configuracion:
   - **Payload URL:** `https://tu-jenkins.com/github-webhook/` (con `/` al final)
   - **Content type:** `application/json`
   - **Secret:** vacio
   - **Events:** Let me select -> marcar **Pushes** y **Pull requests**
   - **Active:** marcado
3. Add webhook

Verificar: GitHub muestra check verde si Jenkins respondio correctamente.

---

## 4. Archivos del repositorio

Agregar estos archivos al proyecto.

### 4.1 `Jenkinsfile` (en la raiz del repo)

```groovy
pipeline {
    agent any

    tools {
        jdk 'jdk-21'
    }

    triggers {
        githubPush()
    }

    environment {
        SPRING_PROFILES_ACTIVE = 'ci'
        DB_NAME     = 'root'
        DB_USER     = 'tripagencymanagement'
        DB_PASSWORD = 'root'
        GITHUB_REPO = 'tu-org/tu-repo'  // <-- CAMBIAR
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                withCredentials([string(credentialsId: 'github-status-token', variable: 'GH_TOKEN')]) {
                    sh """
                        curl -s -X POST \
                            -H "Authorization: token \$GH_TOKEN" \
                            -H "Accept: application/vnd.github+json" \
                            "https://api.github.com/repos/${GITHUB_REPO}/statuses/\$(git rev-parse HEAD)" \
                            -d '{"state":"pending","target_url":"${BUILD_URL}","description":"Build in progress","context":"Jenkins"}'
                    """
                }
            }
        }

        stage('Compile') {
            steps {
                sh 'chmod +x mvnw'
                sh './mvnw compile -B'
            }
        }

        stage('Test + Coverage') {
            steps {
                script {
                    def jenkinsContainerId = sh(
                        script: 'hostname',
                        returnStdout: true
                    ).trim()

                    docker.image('postgres:16').withRun(
                        '-e POSTGRES_DB=root ' +
                        '-e POSTGRES_USER=tripagencymanagement ' +
                        '-e POSTGRES_PASSWORD=root ' +
                        "--network container:${jenkinsContainerId}"
                    ) { postgres ->
                        sh """
                            echo 'Waiting for PostgreSQL to be ready...'
                            for i in \$(seq 1 30); do
                                if docker exec ${postgres.id} pg_isready -U tripagencymanagement -d root; then
                                    echo 'PostgreSQL is ready!'
                                    break
                                fi
                                echo "Attempt \$i/30 - waiting..."
                                sleep 2
                            done
                        """

                        sh """
                            SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/root \
                            SPRING_DATASOURCE_USERNAME=tripagencymanagement \
                            SPRING_DATASOURCE_PASSWORD=root \
                            ./mvnw verify -B -Pcoverage
                        """
                    }
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withCredentials([string(credentialsId: 'sonarqube-token', variable: 'SONAR_TOKEN')]) {
                    sh """
                        ./mvnw sonar:sonar -B \
                            -Dsonar.host.url=https://sonar.tudominio.com \
                            -Dsonar.token=\$SONAR_TOKEN
                    """
                }
            }
        }

        stage('Package') {
            steps {
                sh './mvnw package -B -DskipTests'
            }
        }

        stage('Archive Artifacts') {
            steps {
                sh './mvnw dependency:tree -B -DoutputFile=target/dependency-tree.txt'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                archiveArtifacts artifacts: 'target/site/jacoco/**', allowEmptyArchive: true
                archiveArtifacts artifacts: 'target/surefire-reports/**', allowEmptyArchive: true
                archiveArtifacts artifacts: 'target/dependency-tree.txt', allowEmptyArchive: true
            }
        }
    }

    post {
        always {
            junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
        }
        success {
            withCredentials([string(credentialsId: 'github-status-token', variable: 'GH_TOKEN')]) {
                sh """
                    curl -s -X POST \
                        -H "Authorization: token \$GH_TOKEN" \
                        -H "Accept: application/vnd.github+json" \
                        "https://api.github.com/repos/${GITHUB_REPO}/statuses/\$(git rev-parse HEAD)" \
                        -d '{"state":"success","target_url":"${BUILD_URL}","description":"Build passed","context":"Jenkins"}'
                """
            }
        }
        failure {
            withCredentials([string(credentialsId: 'github-status-token', variable: 'GH_TOKEN')]) {
                sh """
                    curl -s -X POST \
                        -H "Authorization: token \$GH_TOKEN" \
                        -H "Accept: application/vnd.github+json" \
                        "https://api.github.com/repos/${GITHUB_REPO}/statuses/\$(git rev-parse HEAD)" \
                        -d '{"state":"failure","target_url":"${BUILD_URL}","description":"Build failed","context":"Jenkins"}'
                """
            }
        }
    }
}
```

**Valores a cambiar por proyecto:**
- `GITHUB_REPO` -> `tu-org/tu-repo`
- `sonar.host.url` -> URL de tu SonarQube
- Credenciales de PostgreSQL si son diferentes

### 4.2 `src/test/resources/application-ci.properties`

```properties
# CI Profile — GitHub Actions & Jenkins

server.port=0

# PostgreSQL — configurable via env vars
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5434/root}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:tripagencymanagement}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:root}
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Liquibase
spring.liquibase.enabled=true
spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.yaml

# Disable Docker Compose auto-start in CI
spring.docker.compose.enabled=false

# Logging
logging.level.root=WARN
logging.level.org.springframework=WARN
logging.level.org.hibernate=WARN
```

### 4.3 Badge en el README

```markdown
![Jenkins Build](https://tu-jenkins.com/buildStatus/icon?job=nombre-del-job%2Fdevelop&subject=Jenkins)
```

Reemplazar:
- `tu-jenkins.com` -> URL de tu Jenkins
- `nombre-del-job` -> nombre del Multibranch Pipeline job
- `develop` -> rama que quieres mostrar

---

## 5. Verificacion

Despues de configurar todo:

1. Hacer push a una rama con Jenkinsfile
2. En Jenkins: Scan Multibranch Pipeline Now -> debe detectar la rama
3. Build Now -> debe correr todos los stages
4. En GitHub: el PR debe mostrar el check de Jenkins (bolita amarilla -> verde/roja)
5. El badge del README debe mostrar el estado del build
6. En SonarQube: debe aparecer el proyecto con metricas de cobertura

---

## 6. Solucion de problemas

### PostgreSQL no se conecta ("Connection refused" o "Connect timed out")

El Jenkinsfile usa `--network container:<jenkins-id>` para que PostgreSQL comparta la red de Jenkins. Esto permite usar `localhost:5432`. Si falla:

```bash
# Verificar que Jenkins tiene Docker socket montado
sudo docker inspect jenkins --format '{{json .Mounts}}' | grep docker.sock

# Verificar que Docker CLI funciona dentro de Jenkins
sudo docker exec jenkins docker ps
```

### GitHub API rate limit ("Sleeping until reset")

Se agota la cuota de 60 req/hora sin autenticacion. Soluciones:
- Verificar que el job usa la credencial `github-readonly` en Branch Sources
- Con token clasico el limite sube a 5000/hora

### Check de Jenkins no aparece en GitHub

- Usar token **clasico** (NO fine-grained) con scope `repo:status`
- El token debe tener acceso al repo de la organizacion
- Verificar que la credencial `github-status-token` esta creada como Secret text

### Badge de Jenkins no carga

- Instalar plugin **Embeddable Build Status**
- Verificar que existe un build en la rama que apunta el badge
- Verificar URL: `https://tu-jenkins.com/buildStatus/icon?job=nombre-job%2Frama`

### "No such DSL method 'jacoco'"

No usar `jacoco()` en el Jenkinsfile a menos que el plugin JaCoCo este instalado. El reporte se archiva como artifact sin necesidad del plugin.

### Maven Wrapper no ejecuta ("Permission denied")

El Jenkinsfile incluye `chmod +x mvnw` antes de usarlo. Si falla, verificar que `mvnw` existe en el repo.
