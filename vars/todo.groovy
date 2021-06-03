def call (Map params = [:]){

    def args =[
            NEXUS_IP        : '172.31.12.138',
    ]
    args << params

    pipeline {
        agent {
            label "${args.SLAVE_LABEL}"
        }

        triggers {
            pollSCM('*/2 * * * 1-5')
        }

        environment {
            COMPONENT    = "${args.COMPONENT}"
            NEXUS_IP     = "${args.NEXUS_IP}"
            PROJECT_NAME = "${args.PROJECT_NAME}"
            SLAVE_LABEL  = "${args.SLAVE_LABEL}"
            APP_TYPE     = "${args.APP_TYPE}"


        }

        stages {

            stage ('Build code & Download Dependencies'){
                steps {
                    script {
                        prepare = new nexus ()
                        prepare.code_build("${APP_TYPE}","${COMPONENT}")
                    }
                }

            }

            stage ('Prepare Artifacts'){
                steps {
                    script {
                        prepare = new nexus ()
                        prepare.make_artifacts("${APP_TYPE}","${COMPONENT}")
                    }

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