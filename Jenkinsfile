#!/usr/bin/env groovy

pipeline {
  agent {
    kubernetes {
      cloud 'SLKE'
      defaultContainer 'maven'
      yaml """
kind: Pod
spec:
  # All containers should have the same UID
  securityContext:
    runAsUser: 0
  containers:
  - name: maven
    image: harbor.softleader.com.tw/library/maven:3-azulzulu-11
    imagePullPolicy: Always
    command: ['cat']
    tty: true
    resources:
      limits:
        memory: "2.5Gi"
        cpu: "2"
    volumeMounts:
    - name: m2
      mountPath: /root/.m2
  - name: git
    image: alpine/git:v2.32.0
    command: ['cat']
    tty: true
    resources:
      limits:
        memory: "100Mi"
        cpu: "100m"
  volumes:
  - name: m2
    persistentVolumeClaim:
      claimName: m2-claim
"""
    }
  }

  environment {
    MAVEN_OPTS="-Xmx2048m -XX:MaxMetaspaceSize=128m"
  }

  stages {

    stage('Confirm Env') {
      steps {
        sh 'printenv'
        sh 'java -version'
        sh 'mvn --version'
        echo "${params}"
      }
    }

    stage('Compile and Style Check') {
      steps {
        sh "make compile"
        container('git') {
          sh '[ ! -z "$(git status -s)" ] && exit 1 || echo "Good to go!"'
        }
      }
    }

    stage('Unit Testing') {
      steps {
        sh "make test"
      }
    }
  }
}
