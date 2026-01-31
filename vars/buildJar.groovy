#!/usr/bin/env groovy

def call() {
    echo "building the application $BRANCH_NAME"
    sh 'mvn package'
}