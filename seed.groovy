folder('CI-Pipelines') {
    displayName('CI Pipelines')
    description('CI Pipelines')
}

def component = ["frontend", "login","users","todo"]

def count=(component.size()-1)
for (i in 0..count) {
    def j=component[i]
    pipelineJob("CI-Pipelines/${j}-ci") {
        configure { flowdefinition ->
            flowdefinition << delegate.'definition'(class:'org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition',plugin:'workflow-cps') {
                'scm'(class:'hudson.plugins.git.GitSCM',plugin:'git') {
                    'userRemoteConfigs' {
                        'hudson.plugins.git.UserRemoteConfig' {
                            'url'('https://github.com/PradeepreddyKapa/'+j+'.git')
                            'refspec'('\'+refs/tags/*\':\'refs/remotes/origin/tags/*\'')
                        }
                    }
                    'branches' {
                        'hudson.plugins.git.BranchSpec' {
                            'name'('*/main')
                        }
                    }
                }
                'scriptPath'('jenkinsfile')
                'lightweight'(true)
            }
        }
    }
}
