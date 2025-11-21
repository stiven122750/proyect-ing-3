pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/stiven122750/proyect-ing-3', branch: 'main'
            }
        }

        stage('Test') {
            steps {
                sh 'chmod +x gradlew'
                sh './gradlew clean test'
            }
            post {
                always {
                    junit 'build/test-results/test/*.xml'
                }
            }
        }
    }

    post {
        success {
            echo "✔️ Pruebas unitarias pasaron exitosamente"
        }
        failure {
            echo "❌ Fallaron pruebas - Revisar resultados"
        }
    }
}
