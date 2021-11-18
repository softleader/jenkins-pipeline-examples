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

    stage('確認環境變數') {
      steps {
        sh 'printenv'
        sh 'java -version'
        sh 'mvn --version'
        echo "${params}"
      }
    }

    stage ('清理環境') {
      steps {
        sh "mvn clean"
      }
    }

    stage('程式編譯') {
      steps {
        sh "mvn compile -e"
      }
    }

    stage('檢查程式碼排版') {
      steps {
        container('git') {
          sh '[ ! -z "$(git status -s)" ] && exit 1 || echo "Good to go!"'
        }
      }
    }

    stage('單元測試') {
      steps {
        sh "mvn test -e"
      }
    }
  }
}