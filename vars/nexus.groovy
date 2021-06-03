def nexus () {
    command = "curl -v -u admin:admin --upload-file frontend.zip http://172.31.12.138:8081/repository/frontend/frontend.zip"
    def execute_state=sh(returnStdout: true, script: command)
}

def make_artifacts(APP_TYPE, COMPONENT) {
    if(APP_TYPE == "NGINX" ) {
        command = " zip -r ${COMPONENT}.zip * "
        def execute_com=sh(returnStdout: true, script: command )
        print execute_com
    }   else if(APP_TYPE == "MAVEN"){
        command = " cp target/*.jar user.jar && zip -r ${COMPONENT}.zip user.jar "
        def execute_com=sh(returnStdout: true, script: command )
        print execute_com
    }   else if(APP_TYPE == "GO") {
        command = " zip -r ${COMPONENT}.zip login-ci "
        def execute_com=sh(returnStdout: true, script: command )
        print execute_com
    }   else if(APP_TYPE == "NODEJS") {
        command = " zip -r ${COMPONENT}.zip node_modules server.js "
        def execute_com=sh(returnStdout: true, script: command )
        print execute_com
    }

}