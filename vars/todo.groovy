def call (Map params = [:]){

    def args =[
            NEXUS_IP        : '172.31.12.138',
    ]
    args << params

    pipeline {
        agent {
            label "${args.SLAVE_LABEL}"
        }

        environment {
            COMPONENT    = "$(args.COMPONENT)"
            NEXUS_IP     = "$(args.NEXUS_IP)"
            PROJECT_NAME = "$(args.PROJECT_NAME)"
            SLAVE_LABEL  = "$(args.SLAVE_LABEL)"
            APP_TYPE     = "$(args.APP_TYPE)"


        }

        stages {

            stage ('Prepare Artifacts'){
                when{
                    environment name : 'APP_TYPE', value : 'NGINX'

                }
                steps {
                    sh '''
                zip -r ${COMPONENT}.zip *
                '''

                }
            }
            stage ('Build code'){
                when {
                    environment name : 'APP_TYPE', value : 'MAVEN'
                }

                steps {
                    sh '''
                  mvn clean package
                '''

                }
            }
            stage ('Prepare Artifacts'){

                steps {
                    sh '''
                cp target/*.jar user.jar
                zip -r ${COMPONENT}.zip user.jar
                '''

                }
            }



            stage ('Upload Artifacts'){

                steps {
                    sh '''
                curl -v -u admin:admin --upload-file frontend.zip http://172.31.12.138:8081/repository/frontend/frontend.zip
                '''
                }
            }
        }
    }

}