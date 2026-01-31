#!/usr/bin/env groovy

package com.example
// Serializable -> Serializable is a Java marker interface that tells Jenkins: “This object can be safely saved and restored when a pipeline is paused or restarted.”
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
}