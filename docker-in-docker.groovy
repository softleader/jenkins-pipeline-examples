/**
 * 本範例展示如何在 stage 中, 操作 agent 中的 docker
 */
pipeline {
  agent {
    kubernetes {
      cloud 'SLKE'
      defaultContainer 'docker'
      yaml """
kind: Pod
spec:
  # All containers should have the same UID
  securityContext:
    runAsUser: 0
  containers:
  - name: docker
    image: docker:dind
    command: ['cat']
    tty: true
    resources:
      limits:
        memory: "100Mi"
        cpu: "100m"
    volumeMounts:
    - name: dockersock
      mountPath: /var/run/docker.sock
  volumes:
  - name: dockersock
    hostPath:
      path: /var/run/docker.sock
"""
    }
  }

  stages {
    stage ('List images') {
      steps {
        sh "docker images"
      }
    }
  }
}
