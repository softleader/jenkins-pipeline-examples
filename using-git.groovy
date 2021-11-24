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
    image: harbor.softleader.com.tw/library/git:2
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
     stage ('Configure git') {
      steps {
        sh """
        git remote set-url origin https://$CREDENTIAL_USR:"$CREDENTIAL_PSW"@github.com/softleader/jenkins-pipeline-examples.git
        git config --global user.email "jenkins-bot@softleader.com.tw"
        git config --global user.name "jenkins-bot"
        """
      }
    }
    
    stage('Create branch') {
      steps {
        sh "git checkout -b $BUILD_TAG"
      }
    }

    stage('Commit and push changes') {
      steps {
        sh """
        echo "> Hello from $BUILD_TAG" >> README.md
        git commit -m "add hello message" README.md
        git push --set-upstream origin $BUILD_TAG
        gh pr create --title "Bump version to ${TAG}" --body "Bump version to ${TAG}"
        """
      }
    } 

    // stage('Delete remote branch') {
    //   steps {
    //     sh "git push origin :$BUILD_TAG"
    //   }
    // } 
  }
}
