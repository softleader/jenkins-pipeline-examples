/**
 * 本範例展示了如何 cache maven m2, 讓每次的 maven build 不用重新下載之前載過的 dependencies
 */
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
    image: csanchez/maven:azulzulu-11
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
