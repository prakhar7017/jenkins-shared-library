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
        script.withCredentials([script.usernamePassword(credentialsId: "github-cred", usernameVariable: "USER", passwordVariable: "PASSWORD")]) {
            
            script.sh 'git config --global user.email "jenkins@example.com"'
            script.sh 'git config --global user.name "jenkins"'

            script.sh 'git status'
            script.sh 'git branch'
            script.sh 'git config --list'

            // URL-encode credentials to handle special characters like @ in password
            script.sh '''
                ENCODED_PASSWORD=$(echo -n "$PASSWORD" | python3 -c "import sys, urllib.parse; print(urllib.parse.quote(sys.stdin.read(), safe=''))")
                git remote set-url origin "https://${USER}:${ENCODED_PASSWORD}@github.com/prakhar7017/java-maven-app.git"
            '''
            
            script.sh 'git add .'
            script.sh 'git commit -m "ci:version bump"'
            script.sh 'git push origin HEAD:jenkins-jobs'
        }
    }
}
