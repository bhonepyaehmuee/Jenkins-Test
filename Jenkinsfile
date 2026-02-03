pipeline {
    agent any

    tools {
        maven "maven3.9"
    }

    environment {
        DOCKER_REPO = "bph/calculator-image"
        DOCKER_HOST_PORT = "8082"
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/bhonepyaehmuee/Spring_html.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Test + JaCoCo') {
            steps {
                sh 'mvn test jacoco:report'
            }
        }

        stage('JaCoCo Report') {
            steps {
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'target/site/jacoco',
                    reportFiles: 'index.html',
                    reportName: 'JaCoCo Coverage'
                ])
            }
        }

        stage('Static Code Analysis (Checkstyle)') {
            steps {
                sh 'mvn checkstyle:checkstyle'
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'target/site/checkstyle',
                    reportFiles: 'checkstyle.html',
                    reportName: 'Checkstyle Report'
                ])
            }
        }

        stage('Build WAR') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    def imageTag = env.BUILD_NUMBER
                    sh "docker build -t ${DOCKER_REPO}:${imageTag} ."
                    sh "docker tag ${DOCKER_REPO}:${imageTag} ${DOCKER_REPO}:latest"
                    env.IMAGE_TAG = imageTag
                }
            }
        }

        stage('Run Docker Container') {
            steps {
                echo "Running container locally (port ${DOCKER_HOST_PORT})..."
                sh """
                  docker stop calculator-container || true
                  docker rm calculator-container || true
                  docker run -d --name calculator-container \
                    -p ${DOCKER_HOST_PORT}:8080 \
                    ${DOCKER_REPO}:${env.IMAGE_TAG}
                """
            }
        }
    }

    post {
        success {
            echo "✅ Pipeline succeeded! App running at http://localhost:${DOCKER_HOST_PORT}/"
        }
        failure {
            echo "❌ Pipeline failed."
        }
        always {
            echo "🏁 Pipeline finished."
        }
    }
}
