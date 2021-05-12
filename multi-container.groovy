/**
 * 本範例展示使用多個 container, 並且讓這些 container 之間可以享有一個 shared volume
 */
pipeline {
  agent {
    kubernetes {
      cloud 'SLKE'
      yaml """
kind: Pod
spec:
  # All containers should have the same UID
  securityContext:
    runAsUser: 0
  containers:
  - name: busybox
    image: busybox
    command: ['cat']
    tty: true
    resources:
      limits:
        memory: "10Mi"
        cpu: "10m"
    volumeMounts:
    - name: shared-data
      mountPath: /data
  - name: jq
    image: stedolan/jq
    command: ['cat']
    tty: true
    resources:
      limits:
        memory: "10Mi"
        cpu: "10m"
    volumeMounts:
    - name: shared-data
      mountPath: /data
  volumes:
  - name: shared-data
    emptyDir: {}
"""
    }
  }

  environment {
    DATA="/data"
  }

  stages {
    stage ('Create file') {
     steps {
       container('busybox') {
          sh """
          echo '{"hello": "world"}' > ${DATA}/test.json
          """
       }
      }
    }
    stage ('Pretty print file') {
     steps {
       container('jq') {
         sh "jq . ${DATA}/test.json"
       }
      }
    }
  }
}
