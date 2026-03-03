pipeline {
    agent any

    tools {
        maven "maven3.9"
    }

    environment {
        DOCKER_REPO = "bph-calculator-image"
        DOCKER_HOST_PORT = "9096"
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/bhonepyaehmuee/Jenkins-Test.git'
            }
        }

        stage('Build, Test & Coverage') {
            steps {
                // This generates JaCoCo HTML at target/site/jacoco
                sh 'mvn clean verify'
                junit 'target/surefire-reports/*.xml'
            }
        }

        stage('JaCoCo Report') {
            steps {
                publishHTML([
                    reportDir: 'target/site/jacoco',
                    reportFiles: 'index.html',
                    reportName: 'JaCoCo Coverage',
                    allowMissing: false,   // Required parameter
                    alwaysLinkToLastBuild: true,   // Required parameter
                    keepAll: true   // Required parameter
                ])
            }
        }

        stage("Static Code Analysis (Checkstyle)") {
            steps {
                sh 'mvn checkstyle:checkstyle'
                publishHTML([
                    reportDir: 'target/site',
                    reportFiles: 'checkstyle.html',
                    reportName: 'Checkstyle Report',
                    allowMissing: false,   // Required parameter
                    alwaysLinkToLastBuild: true,   // Required parameter
                    keepAll: true   // Required parameter
                ])
            }
        }

        stage('Code Analysis') {
            environment {
                scannerHome = tool 'sonar'
            }
            steps {
                withSonarQubeEnv('sonar') {
                     
                        sh """
                            ${scannerHome}/bin/sonar-scanner \
                            -Dsonar.projectKey=calculator-demo \
                            -Dsonar.projectName=calculator-demo \
                            -Dsonar.sources=. \
                            -Dsonar.java.binaries=target/classes \
                          
                        """
                    
                }
            }
        }


        stage('Build Jar') {
            steps {
                // Jar build AFTER coverage
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    def imageTag = "${env.BUILD_NUMBER}"
                    sh "docker build -t ${DOCKER_REPO}:${imageTag} ."
                    sh "docker tag ${DOCKER_REPO}:${imageTag} ${DOCKER_REPO}:latest"
                    env.IMAGE_TAG = imageTag
                }
            }
        }

        stage('Run Docker Container') {
            steps {
                echo "Running container locally (port 8081)..."
                sh """
                docker stop bph-calculator-container || true
                docker rm bph-calculator-container || true
                docker run -d --name bph-calculator-container -p 9096:8080 ${DOCKER_REPO}:${env.IMAGE_TAG}
                """
            }
        }

        stage('Acceptance Test') {
            steps {
                sh 'bash acceptance_test.sh'
            }
            post {
                always {
                    // Make sure this folder matches your Maven configuration
                    junit allowEmptyResults: true, testResults: 'target/acceptance-reports/*.xml'
        
                    publishHTML(target: [
                        allowMissing: true,
                        keepAll: true,
                        alwaysLinkToLastBuild: true,
                        reportDir: 'target',
                        reportFiles: 'cucumber-report.html',
                        reportName: 'Acceptance Report'
                    ])
                }
            }
        }
    }

    post {
        success {
            echo "✅ Pipeline succeeded! App running at http://localhost:${DOCKER_HOST_PORT}/"
            emailext(
                to: 'bhshi75@gmail.com',
                subject: 'Pipeline Email Test',
                body: 'Pipeline Success email sent successfully ✅'
            )
        }
        failure {
            echo "❌ Pipeline failed."
            emailext(
                to: 'bhshi75@gmail.com',
                subject: 'Pipeline Email Test',
                body: 'Pipeline Fail email sent successfully ✅'
            )
        }
        always {
            echo "🏁 Pipeline finished."
        }
    }
}
