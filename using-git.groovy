/**
 * 本範例展示如何在 stage 中, 透過 git 指令跟 github 互動
 */
pipeline {
  agent {
    kubernetes {
      cloud 'SLKE'
      defaultContainer 'git'
      yaml """
kind: Pod
spec:
  containers:
  - name: git
    image: alpine/git
    command: ['cat']
    tty: true
    resources:
      limits:
        memory: "100Mi"
        cpu: "100m"
"""
    }
  }

  environment {
    // 在 Jenkins 中 System Configuration > Manage Credential
    // ref: https://docs.cloudbees.com/docs/cloudbees-ci/latest/cloud-secure-guide/injecting-secrets
    CREDENTIAL = credentials("a84db61d-b4a4-4e05-a368-c1b283860090")
  }

  stages {
    stage('Create branch') {
      steps {
        sh """
        pwd
        ls -l
        git checkout -b $BUILD_TAG
        """
      }
    } 

    stage('Modify file') {
      steps {
        sh """
        echo "> Hello from $BUILD_TAG" >> README.md
        """
      }
    } 

    stage('Commit and push to remote') {
      steps {
        sh """
        git commit -m "add hello message" README.md
        git push --set-upstream origin $BUILD_TAG
        """
      }
    } 

    stage('Delete remote branch') {
      steps {
        sh "git push origin :$BUILD_TAG"
      }
    } 
  }
}
