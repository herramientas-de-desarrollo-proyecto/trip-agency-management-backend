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
        GITHUB_REPO = 'herramientas-de-desarrollo-proyecto/trip-agency-management-backend'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                withCredentials([string(credentialsId: 'github-status-token', variable: 'GH_TOKEN')]) {
                    sh """
                        echo "Setting GitHub pending status..."
                        curl -v -X POST \
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
                    // Get Jenkins container ID to share its network with PostgreSQL
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
                        // Wait for PostgreSQL to be ready (now on localhost since sharing network)
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
                            -Dsonar.host.url=https://sonar.gonzalogtz.com \
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
