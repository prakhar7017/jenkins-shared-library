#!/usr/bin/env groovy
import com.example.Docker

def call() {
    echo "building the application $BRANCH_NAME"
    sh 'mvn package'
}