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
        script.withCredentials([script.usernamePassword(credentialsId:"github-cred",usernameVariable: "USER", passwordVariable: "PWD")]) {
            script.sh 'git config -- user.email "jenkins@example.com"'
            script.sh 'git config -- user.name "jenkins"'

            script.sh 'git status'
            script.sh 'git branch'
            script.sh 'git config --list'

            script.sh "git remote set-url origin https://${USER}:${PWD}@github.com/prakhar7017/java-maven-app.git"
            script.sh 'git add .'
            script.sh 'git commit -m "ci:version bump"'
            script.sh 'git push origin HEAD:jenkins-jobs'
            script.sh "echo $script.PWD | docker login -u $script.USER --password-stdin"
        }
    }
}
