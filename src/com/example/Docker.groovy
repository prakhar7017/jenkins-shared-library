#!/usr/bin/env groovy

package com.example
// Serializable -> Serializable is a Java marker interface that tells Jenkins: "This object can be safely saved and restored when a pipeline is paused or restarted."
class Docker implements Serializable {
    def script

    Docker(script) {
        this.script = script
    }

    def buildDockerImage(String imageName) {
        script.echo "building the docker image"
        script.sh "docker build -t $imageName ."
    }    

    def dockerLogin(){
        script.withCredentials([script.usernamePassword(credentialsId:"docker-hub-repo",usernameVariable: "USER", passwordVariable: "PWD")]) {
            script.sh "echo $script.PWD | docker login -u $script.USER --password-stdin"
        }
    }

    def dockerPush(String imageName) {
        script.sh "docker push $imageName"
    }

   def commitVersion() {
    script.withCredentials([
        script.string(credentialsId: 'Github-Token', variable: 'GITHUB_TOKEN')
    ]) {

        script.sh '''
            git config --global user.email "jenkins@example.com"
            git config --global user.name "jenkins"

            git status
            git branch
            git config --list

            git add .
            git commit -m "ci:version bump" || echo "Nothing to commit"

            git push https://prakhar7017:${GITHUB_TOKEN}@github.com/prakhar7017/java-maven-app.git HEAD:jenkins-jobs
        '''
    }
}

}
