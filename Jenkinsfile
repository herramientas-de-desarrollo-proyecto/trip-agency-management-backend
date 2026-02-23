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
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
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
                    docker.image('postgres:16').withRun(
                        '-e POSTGRES_DB=root ' +
                        '-e POSTGRES_USER=tripagencymanagement ' +
                        '-e POSTGRES_PASSWORD=root'
                    ) { postgres ->
                        // Get the container's IP (localhost doesn't work between sibling containers)
                        def dbHost = sh(
                            script: "docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' ${postgres.id}",
                            returnStdout: true
                        ).trim()

                        // Wait for PostgreSQL to be ready
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
                            SPRING_DATASOURCE_URL=jdbc:postgresql://${dbHost}:5432/root \
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
            jacoco(
                execPattern: 'target/jacoco.exec',
                classPattern: 'target/classes',
                sourcePattern: 'src/main/java',
                exclusionPattern: '**/Q*.class'
            )
        }
        failure {
            echo "Pipeline FAILED on branch: ${env.BRANCH_NAME ?: env.GIT_BRANCH}"
        }
        success {
            echo "Pipeline SUCCEEDED on branch: ${env.BRANCH_NAME ?: env.GIT_BRANCH}"
        }
    }
}
