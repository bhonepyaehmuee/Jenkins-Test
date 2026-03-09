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
                        -Dsonar.java.binaries=target/classes
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
                echo "Running container locally (port 9096)..."
                sh """
                docker stop bph-calculator-container || true
                docker rm bph-calculator-container || true
                docker run -d --name bph-calculator-container -p 9096:8080 ${DOCKER_REPO}:${env.IMAGE_TAG}
                """
            }
        }

        stage('Acceptance Test') {
            steps {
                // Run your acceptance test script
                sh 'mvn verify -Pacceptance'
            }
            post {
                always {
                    // JUnit XML reports (for test results)
                    junit allowEmptyResults: true, testResults: 'target/cucumber-reports/*.xml'
    
                    // HTML report from Cucumber
                    publishHTML(target: [
                        allowMissing: true,
                        keepAll: true,
                        alwaysLinkToLastBuild: true,
                        reportDir: 'target/cucumber-reports',      // folder where HTML is generated
                        reportFiles: 'cucumber-report.html',       // actual HTML file
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
                subject: 'Jenkins Pipeline Success',
                body: 'Your Jenkins pipeline has successfully completed ✅. The application is running at http://localhost:${DOCKER_HOST_PORT}/'
            )
        }
        failure {
            echo "❌ Pipeline failed."
            emailext(
                to: 'bhshi75@gmail.com',
                subject: 'Jenkins Pipeline Failure',
                body: 'The Jenkins pipeline failed. Please check the logs for more details ❌.'
            )
        }
        always {
            echo "🏁 Pipeline finished."
        }
    }
}
