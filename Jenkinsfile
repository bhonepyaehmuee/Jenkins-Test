pipeline{
    agent any
    tools{
        maven "maven3.9"
    }
    environment {
            DOCKER_REPO = "bph/calculator-image"
            APP_JAR = "target\\demo-0.0.1-SNAPSHOT.jar"
            DOCKER_CREDENTIALS_ID = "dockerhub-credentials"
            DOCKER_HOST_PORT = "8082"
    }
    stages{
        stage('Checkout'){
            steps{
                git branch: 'main', url: 'https://github.com/bhonepyaehmuee/Spring_html.git'
            }
        }
       stage('Build'){
            steps{
                sh 'mvn clean compile'
            }
       }
       stage('Test'){
            steps{
                sh 'mvn test'
            }
       }
        stage('JaCoCo Report') {
            steps {
                // Publish JaCoCo HTML report in Jenkins
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
        stage("Static Code Analysis (Checkstyle)") {
            steps {
                sh "mvn checkstyle:checkstyle"
                publishHTML(target: [
                    reportDir: 'target/site',
                    reportFiles: 'checkstyle.html',
                    reportName: 'Checkstyle Report'
                ])
            }
        }
      
       stage('Build Jar'){
           steps{
                sh 'mvn clean package -DskipTests'
           }
       }
        stage('Build Docker Image') {
            steps {
                script {
                    // Build Docker image and tag it with build number
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
                docker stop calculator-container || true
                docker rm calculator-container || true
                docker run -d --name calculator-container -p 8082:8080 ${DOCKER_REPO}:${env.IMAGE_TAG}
                """
            }
        }
    }
    post {
        always{
                echo "✅ Pipeline finished."
            }
            success {
                echo "Pipeline succeeded! App running at http://localhost:${env.DOCKER_HOST_PORT}/"
            }
            failure {
                echo "Pipeline failed."
            }
          }
}
