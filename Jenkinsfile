pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/TU_USUARIO/TU_REPO.git', branch: 'main'
            }
        }

        stage('Test') {
            steps {
                sh "./gradlew clean test"
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
