pipeline {
  agent {
    kubernetes {
      cloud 'SLKE'
      defaultContainer 'docker'
      yaml """
kind: Pod
spec:
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
