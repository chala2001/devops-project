pipeline {
    agent any
    
    tools {
        maven 'Maven3'
        jdk 'JDK21'
    }
    
    environment {
        APP_NAME = 'hello-devops'
        APP_VERSION = '1.0-SNAPSHOT'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo '=== Stage 1: Checking out code from GitHub ==='
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                echo '=== Stage 2: Compiling the application ==='
                bat 'mvn clean compile'
            }
        }
        
        stage('Test') {
            steps {
                echo '=== Stage 3: Running unit tests ==='
                bat 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Package') {
            steps {
                echo '=== Stage 4: Packaging application into JAR ==='
                bat 'mvn package -DskipTests'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
        
        stage('Deploy') {
            steps {
                echo '=== Stage 5: Deploying application ==='
                echo "Deploying ${APP_NAME} version ${APP_VERSION}"
                bat "java -jar target\\${APP_NAME}-${APP_VERSION}.jar"
            }
        }
    }
    
    post {
        success {
            echo 'Pipeline completed SUCCESSFULLY!'
        }
        failure {
            echo 'Pipeline FAILED! Check the logs above.'
        }
        always {
            echo 'Pipeline finished.'
            cleanWs()
        }
    }
}
