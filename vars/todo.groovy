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
            COMPONENT    = "${args.COMPONENT}"
            NEXUS_IP     = "${args.NEXUS_IP}"
            PROJECT_NAME = "${args.PROJECT_NAME}"
            SLAVE_LABEL  = "${args.SLAVE_LABEL}"
            APP_TYPE     = "${args.APP_TYPE}"


        }

        stages {

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

            stage ('Code Build'){
                when {
                    environment name : 'APP_TYPE', value : 'GO'
                }
                steps {
                    sh '''
                 go get github.com/dgrijalva/jwt-go && go get github.com/labstack/echo && go get github.com/labstack/echo/middleware && go get github.com/labstack/gommon/log && go get github.com/openzipkin/zipkin-go && go get github.com/openzipkin/zipkin-go/middleware/http && go get github.com/openzipkin/zipkin-go/reporter/http &&  go build
                 '''
                }
            }

            stage ('Code Build for nodejs'){
                when {
                    environment name : 'APP_TYPE', value : 'NODEJS'
                }
                steps {
                    sh '''
                 npm install
                 '''
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