# Trip Agency Management Backend

![CI Pipeline](https://github.com/herramientas-de-desarrollo-proyecto/trip-agency-management-backend/actions/workflows/ci.yml/badge.svg)
![Branch Naming](https://github.com/herramientas-de-desarrollo-proyecto/trip-agency-management-backend/actions/workflows/check-branch-name.yml/badge.svg)
![Jenkins](https://jenkins.gonzalogtz.com/buildStatus/icon?job=trip-agency-management-backend%2Fdevelop)

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

## Project Structure

```
.github/workflows/     # CI/CD pipeline definitions
src/main/java/         # Application source code
src/main/resources/    # Configuration files & Liquibase changelogs
src/test/              # Test source code & test configs
```
