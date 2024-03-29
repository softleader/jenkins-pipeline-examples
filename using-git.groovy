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
  # All containers should have the same UID
  securityContext:
    runAsUser: 0
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
    stage('Configure git') {
      steps {
        container('git') {
          script {
            env.GIT_PATH = sh(
              script: 'echo $GIT_URL | awk -F"github.com/" "{print \\\$2}"',
              returnStdout: true
            ).trim()
            sh """
            git remote set-url origin https://$CREDENTIAL_USR:"$CREDENTIAL_PSW"@github.com/$GIT_PATH".git"
            git config --global user.email "jenkins-bot@softleader.com.tw"
            git config --global user.name "jenkins[bot]"
            echo $CREDENTIAL_PSW | gh auth login --with-token
            """
          }
        }
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
        """
      }
    } 

    stage('Create PR') {
      steps {
        sh """
        gh pr create --title "Add hello message" --body "Add hello message"
        """
      }
    } 

    stage('Close PR and delete remote branch') {
      steps {
        sh """
        gh pr close $BUILD_TAG
        git push origin :$BUILD_TAG
        """
      }
    } 
  }
}
