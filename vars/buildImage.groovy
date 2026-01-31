#!/usr/bin/env groovy
def call() {
    echo "building the docker image"
    withCredentials([usernamePassword(credentialsId:"docker-hub-repo",usernameVariable: "USER", passwordVariable: "PWD")]) {
        sh 'docker build -t prakhar7017/java-maven-repo:java-maven-app-1.1 .'
        sh "echo $PWD | docker login -u $USER --password-stdin"
        sh 'docker push prakhar7017/java-maven-repo:java-maven-app-1.1'

    }
}