# Trip Agency Management Backend

![CI Pipeline](https://github.com/herramientas-de-desarrollo-proyecto/trip-agency-management-backend/actions/workflows/ci.yml/badge.svg)
![Branch Naming](https://github.com/herramientas-de-desarrollo-proyecto/trip-agency-management-backend/actions/workflows/check-branch-name.yml/badge.svg)
![Jenkins Build](https://jenkins.gonzalogtz.com/buildStatus/icon?job=trip-agency-management-backend%2Fdevelop&subject=Jenkins)

## Tech Stack

- **Java 21** (Temurin)
- **Spring Boot 4.0.1**
- **PostgreSQL 16**
- **Maven Wrapper** (no global Maven install required)
- **Liquibase** for database migrations
- **JaCoCo** for code coverage
- **SonarQube** for static analysis

## Prerequisites

- Java 21+
- Docker & Docker Compose (for PostgreSQL and SonarQube)

## Quick Start

```bash
# Clone the repository
git clone https://github.com/herramientas-de-desarrollo-proyecto/trip-agency-management-backend.git
cd trip-agency-management-backend

# Start PostgreSQL (via docker-compose already in the project)
docker compose up -d

# Run the application
./mvnw spring-boot:run
```

## Build Commands

```bash
# Compile
./mvnw compile

# Run tests
./mvnw verify

# Run tests with coverage report
./mvnw verify -Pcoverage

# Package JAR
./mvnw package

# Generate dependency tree
./mvnw dependency:tree
```

## CI/CD Workflows

| Workflow | Trigger | Description |
|----------|---------|-------------|
| **CI Pipeline** (`ci.yml`) | Push/PR to `develop`, `main` | Compile, test, JaCoCo coverage, SonarQube analysis, artifact upload |
| **Deploy to Production** (`deploy-prod.yml`) | Tag `v*.*.*` | Build, test, package JAR, create GitHub Release |
| **Branch Naming** (`check-branch-name.yml`) | Pull requests | Enforces branch naming conventions |
| **Jenkins Pipeline** (`Jenkinsfile`) | Push to `develop`, PRs | Compile, test, JaCoCo, SonarQube, package JAR (runs on VPS) |

## Jenkins (VPS)

El proyecto incluye un `Jenkinsfile` para ejecutar CI/CD en un servidor VPS con Jenkins.

### Prerequisitos en el VPS

1. **Jenkins** corriendo como contenedor Docker (`jenkins/jenkins:lts-jdk21`)
2. **Docker Pipeline** y **Docker** plugins instalados en Jenkins
3. **Docker socket** montado en el contenedor de Jenkins:
   ```bash
   sudo docker run -d \
     --name jenkins \
     --restart unless-stopped \
     -p 8080:8080 -p 50000:50000 \
     -v jenkins_home:/var/jenkins_home \
     -v /var/run/docker.sock:/var/run/docker.sock \
     jenkins/jenkins:lts-jdk21
   ```
4. **Docker CLI** instalado dentro del contenedor:
   ```bash
   sudo docker exec -u root jenkins bash -c "apt-get update && apt-get install -y docker.io"
   sudo docker exec -u root jenkins bash -c "usermod -aG docker jenkins"
   sudo docker restart jenkins
   ```
5. **JDK 21** configurado en Jenkins: Manage Jenkins → Tools → JDK → nombre: `jdk-21`

### Credenciales necesarias

| ID en Jenkins | Tipo | Valor |
|---|---|---|
| `sonarqube-token` | Secret Text | Token de SonarQube |

### Crear el Job

1. New Item → **Multibranch Pipeline**
2. Branch Sources → GitHub → URL del repositorio
3. Build Configuration → by Jenkinsfile
4. Configurar webhook en GitHub: Settings → Webhooks → `https://jenkins.tudominio.com/github-webhook/`

### Pipeline stages

`Checkout` → `Compile` → `Test + Coverage` (PostgreSQL 16 sidecar) → `SonarQube Analysis` → `Package` → `Archive Artifacts`

## SonarQube

### Setup

```bash
# 1. Create your .env from the template
cp .env.example .env

# 2. Edit .env with your values
#    - Local:  SONAR_HOST_URL=http://localhost:9000
#    - VPS:    SONAR_HOST_URL=http://YOUR_VPS_IP:9000
#    - Token:  SONAR_TOKEN=your_token_here
```

### Local (Docker)

```bash
# Start SonarQube
docker compose -f docker-compose.sonarqube.yml up -d

# Wait for it to be ready at http://localhost:9000
# Default credentials: admin / admin
```

### VPS

Si tienes Docker Compose en tu VPS, copia `docker-compose.sonarqube.yml` al servidor y ejecútalo allí. SonarQube quedará accesible en `http://YOUR_VPS_IP:9000`.

### Run Analysis

```bash
# Load env vars and run analysis
source .env
./mvnw verify -Pcoverage sonar:sonar \
  -Dsonar.host.url=$SONAR_HOST_URL \
  -Dsonar.token=$SONAR_TOKEN
```

## Despliegue a Produccion

### Arquitectura de despliegue

```
GitHub Push ──> Jenkins (VPS)
                  │
             ┌────┴────┐
             │ CI Jobs  │  (compile/test/sonar)
             └────┬────┘
                  │ (main branch)
             ┌────┴────┐
             │ CD Jobs  │  (docker build+push → ansible deploy)
             └────┬────┘
                  │
         Docker Hub (gonzagtz/ptc-backend)
                  │
             ┌────┴──────────────────┐
             │        VPS            │
             │  Nginx Proxy Manager  │──> ptc-api.gonzalogtz.com → backend:3091
             │  (jc21/nginx-proxy)   │──> ptc-app.gonzalogtz.com → frontend:3090
             │  PostgreSQL 16        │──> internal :5432
             └───────────────────────┘
```

### Infraestructura

| Componente | Detalle |
|---|---|
| **VPS** | Hetzner - Ubuntu 24.04 (noble) |
| **Usuario SSH** | `gonzalo` (NOPASSWD sudo) |
| **Docker Hub** | `gonzagtz/ptc-backend`, `gonzagtz/ptc-frontend` |
| **Reverse Proxy** | Nginx Proxy Manager (puerto 81 para UI) |
| **DNS** | Cloudflare |
| **SSL** | Gestionado por Nginx Proxy Manager |
| **Jenkins** | Contenedor Docker en el VPS |
| **SonarQube** | `https://sonar.gonzalogtz.com` |

### Configuracion inicial del VPS (una sola vez)

**1. Instalar Ansible dentro del contenedor de Jenkins:**
```bash
sudo docker exec -u root jenkins bash -c "apt-get update && apt-get install -y ansible"
```

**2. Instalar Docker Compose plugin en el VPS:**
```bash
# Agregar repositorio oficial de Docker
sudo install -m 0755 -d /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
sudo chmod a+r /etc/apt/keyrings/docker.gpg

echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] \
  https://download.docker.com/linux/ubuntu $(. /etc/os-release && echo "$VERSION_CODENAME") stable" \
  | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

sudo apt update && sudo apt install -y docker-compose-plugin

# Verificar
docker compose version
```

**3. Configurar sudo sin password para Ansible:**
```bash
sudo visudo
# Agregar al final:
gonzalo ALL=(ALL) NOPASSWD: ALL
```

**4. Configurar Nginx Proxy Manager (puerto 81):**

Para cada subdominio:
1. Acceder a `http://<IP_VPS>:81`
2. **Proxy Hosts** → **Add Proxy Host**
3. **Domain:** `ptc-api.gonzalogtz.com`, **Forward IP:** `172.19.0.1`, **Port:** `3091`
4. Pestana **SSL** → Request new certificate → Force SSL
5. Repetir para frontend: `ptc-app.gonzalogtz.com` → puerto `3090`

### Puertos de servicios

| Servicio | Puerto Host | Puerto Container |
|---|---|---|
| Backend (Spring Boot) | 3091 | 80 |
| Frontend (Next.js) | 3090 | 3000 |
| PostgreSQL | interno | 5432 |

### Pipeline CD (Continuous Deployment)

El pipeline CD se define en `+devops/+production/Jenkinsfile` y se ejecuta cuando hay cambios en la rama `main`.

**Stages:**

1. **Checkout** - Obtiene el codigo y genera version tag con git SHA
2. **Prepare Inventory** - Genera inventario Ansible con IP y usuario del VPS
3. **Prepare Vault Password** - Carga credenciales encriptadas desde Jenkins
4. **Build & Push / Setup Target** (paralelo):
   - Construye imagen Docker multi-stage y la sube a Docker Hub
   - Configura directorio en VPS y copia `docker-compose.base.yml`
5. **Pull Image on Target** - Descarga imagen en VPS, escribe `.env` con credenciales
6. **Up Services** - Ejecuta `docker compose up -d` en el VPS

### Credenciales Jenkins necesarias para CD

| ID en Jenkins | Tipo | Descripcion |
|---|---|---|
| `dockerhub-credentials` | Username/Password | Login Docker Hub (`gonzagtz`) |
| `ssh-id_vps` | SSH Private Key | Clave SSH para usuario `gonzalo` en VPS |
| `ansible-vault-ptc-key` | Secret text | Password para descifrar Ansible Vault |

> **Importante:** Las credenciales de vault deben ser tipo **Secret text**, no Secret file (problemas de encoding CRLF en Windows/Jenkins).

### Estructura de archivos DevOps

```
+devops/
├── docker/
│   └── Dockerfile                          # Multi-stage: JDK 21 builder → JRE 21 runtime
├── ansible/
│   ├── setup_target.yml                    # Crea directorio + copia docker-compose.base.yml
│   ├── service_pull_and_setup.yml          # Pull imagen + escribe .env y .version
│   └── start_service.yml                   # Copia compose template + docker compose up
└── +production/
    ├── Jenkinsfile                          # Pipeline CD
    ├── inventory.yml                        # Template de inventario Ansible
    ├── vault.yml                            # Credenciales encriptadas (ansible-vault)
    ├── docker-compose.base.yml             # PostgreSQL 16 + network
    ├── docker-compose.backend.yml.j2       # Template del servicio backend
    └── nginx/
        ├── ptc-api.gonzalogtz.com          # Config nginx (referencia)
        └── ptc-app.gonzalogtz.com          # Config nginx (referencia)
```

### Docker - Imagen del backend

La imagen usa un build multi-stage para minimizar el tamano:

| Stage | Base Image | Proposito |
|---|---|---|
| Builder | `eclipse-temurin:21-jdk-alpine` | Compila y empaqueta JAR |
| Runtime | `eclipse-temurin:21-jre-alpine` | Ejecuta la aplicacion |

- Ejecuta como usuario no-root (`appuser`)
- Health check: `GET http://localhost:80/ptc/api/api-docs`
- Spring profile activo: `prod`

### Variables de entorno en produccion

Las siguientes variables se inyectan desde el Ansible Vault al archivo `.env.backend`:

| Variable | Descripcion |
|---|---|
| `DATABASE_URL` | URL JDBC de PostgreSQL |
| `DATABASE_USERNAME` | Usuario de la base de datos |
| `DATABASE_PASSWORD` | Password de la base de datos |
| `JWT_SECRET_KEY` | Clave secreta para firmar tokens JWT |

### Configuracion de Nginx Proxy Manager

La configuracion del reverse proxy se realiza desde la UI web en el puerto 81:

1. Acceder a `http://<IP_VPS>:81`
2. **Proxy Hosts** → **Add Proxy Host**
3. Configurar:
   - **Domain:** `ptc-api.gonzalogtz.com`
   - **Scheme:** `http`
   - **Forward Hostname/IP:** `172.19.0.1`
   - **Forward Port:** `3091`
   - **SSL:** Request new certificate → Force SSL
4. Repetir para el frontend (`ptc-app.gonzalogtz.com` → puerto `3090`)

### Comandos utiles en el VPS

```bash
# Ver containers corriendo
docker ps | grep ptc

# Ver logs del backend
docker logs ptc-production-backend --tail 50

# Ver logs del frontend
docker logs ptc-production-frontend --tail 50

# Reiniciar un servicio
docker restart ptc-production-backend

# Ver archivos de configuracion
ls -la /opt/docker/compose/projects/ptc-production/

# Ver la version desplegada
cat /opt/docker/compose/projects/ptc-production/.version.backend
```

### Troubleshooting

| Problema | Causa | Solucion |
|---|---|---|
| `Decryption failed` en Ansible | Credencial vault como Secret file | Recrear como **Secret text** en Jenkins |
| `docker compose: unknown flag 'f'` | Plugin compose no instalado | `sudo apt install docker-compose-plugin` (repo oficial Docker) |
| `docker-compose.base.yml: no such file` | setup_target no copio el archivo | Ya corregido: setup_target.yml copia base.yml |
| `POSTGRES_PASSWORD not set` | Falta archivo .env | Ya corregido: service_pull_and_setup.yml crea .env |
| `relation "users" does not exist` | DB vacia sin migraciones Liquibase | `ddl-auto=update` en application-prod.properties |
| Frontend/Backend se matan entre si | `--remove-orphans` en docker compose | Removido de start_service.yml |
| CORS bloqueando requests | Backend solo permite localhost | Agregar dominio produccion a CorsConfig + SecurityConfig |
| `depends_on undefined service` | Frontend depende de backend en compose | Removido depends_on del compose frontend |
| Bad Gateway 502 | Container caido o no corriendo | `docker ps` para verificar, re-ejecutar CD |

## Endpoints de la API

- **Swagger UI:** `https://ptc-api.gonzalogtz.com/ptc/api/swagger-ui.html`
- **OpenAPI Docs:** `https://ptc-api.gonzalogtz.com/ptc/api/api-docs`
- **Health Check:** `https://ptc-api.gonzalogtz.com/ptc/api/api-docs`

### URLs de produccion

| Servicio | URL |
|---|---|
| Backend API | `https://ptc-api.gonzalogtz.com/ptc/api/` |
| Frontend App | `https://ptc-app.gonzalogtz.com` |
| Swagger UI | `https://ptc-api.gonzalogtz.com/ptc/api/swagger-ui.html` |
| Jenkins | `https://jenkins.gonzalogtz.com` |
| SonarQube | `https://sonar.gonzalogtz.com` |

## Project Structure

```
.github/workflows/     # CI/CD pipeline definitions (GitHub Actions)
+devops/               # DevOps: Dockerfile, Ansible playbooks, compose templates
src/main/java/         # Application source code
src/main/resources/    # Configuration files & Liquibase changelogs
src/test/              # Test source code & test configs
Jenkinsfile            # Jenkins CI pipeline
docs/                  # Additional documentation
```
