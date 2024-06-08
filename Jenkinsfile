pipeline {
    agent any

    triggers {
            pollSCM('* * * * *')
    }

    environment {
        APPLICATION_NAME = 'redis-pojo'
    }

    stages {
        stage('Build') {
            steps {
                sh './gradlew clean build'
            }
        }
    }
}
