pipeline {
agent any


tools {
    maven 'Maven3'
    jdk 'JDK21'
}

environment {
    APP_NAME = 'hello-devops'
    APP_VERSION = '1.0-SNAPSHOT'
    JAR_FILE = "${APP_NAME}-${APP_VERSION}.jar"
    EC2_HOST = 'YOUR_EC2_PUBLIC_IP'
    EC2_USER = 'ec2-user'
    REMOTE_DIR = '/home/ec2-user/app'
}

stages {

    stage('Checkout') {
        steps {
            echo '=== Checking out code from GitHub ==='
            checkout scm
        }
    }

    stage('Build') {
        steps {
            echo '=== Compiling the application ==='
            bat 'mvn clean compile'
        }
    }

    stage('Test') {
        steps {
            echo '=== Running unit tests ==='
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
            echo '=== Packaging into JAR ==='
            bat 'mvn package -DskipTests'
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
        }
    }

    stage('Deploy to EC2') {
        steps {
            echo '=== Deploying to AWS EC2 ===='

            sshPublisher(
                publishers: [
                    sshPublisherDesc(
                        configName: 'ec2-devops',
                        transfers: [
                            sshTransfer(
                                sourceFiles: "target/${JAR_FILE}",
                                removePrefix: 'target',
                                remoteDirectory: '',
                                execCommand: """
                                    mkdir -p /home/ec2-user/app
                                    pkill -f ${JAR_FILE} || true
                                    sleep 2
                                    mv ${JAR_FILE} /home/ec2-user/app/
                                    cd /home/ec2-user/app
                                    nohup java -jar ${JAR_FILE} > app.log 2>&1 &
                                    echo Application started successfully!
                                """
                            )
                        ]
                    )
                ]
            )
        }
    }
}

post {
    success {
        echo 'Pipeline completed SUCCESSFULLY!'
        echo "Application deployed to: http://${EC2_HOST}:8081"
    }

    failure {
        echo 'Pipeline FAILED! Check logs above.'
    }

    always {
        cleanWs()
    }
}


}
