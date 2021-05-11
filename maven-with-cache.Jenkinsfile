pipeline {
  agent {
    kubernetes {
      cloud 'SLKE'
      defaultContainer 'maven'
      yaml """
kind: Pod
spec:
  containers:
  - name: maven
    image: harbor.softleader.com.tw/library/maven-git:3.6.2-jdk-11-slim
    command: ['cat']
    tty: true
    resources:
      limits:
        memory: "10Mi"
        cpu: "10m"
    volumeMounts:
    - name: m2
      mountPath: /root/.m2
  volumes:
  - name: m2
    persistentVolumeClaim:
      claimName: m2-claim
"""
    }
  }

  stages {
    stage ('List maven m2') {
      steps {
        sh "ls -l /root/.m2"
      }
    }
  }
}
