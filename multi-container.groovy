pipeline {
  agent {
    kubernetes {
      cloud 'SLKE'
      yaml """
kind: Pod
spec:
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
    - name: home-volume
      mountPath: /home/jenkins
    env:
    - name: HOME
      value: /home/jenkins
  - name: jq
    image: stedolan/jq
    command: ['cat']
    tty: true
    resources:
      limits:
        memory: "10Mi"
        cpu: "10m"
    volumeMounts:
    - name: home-volume
      mountPath: /home/jenkins
    env:
    - name: HOME
      value: /home/jenkins
  volumes:
  - name: home-volume
    emptyDir: {}
"""
    }
  }

stages {
    stage ('Echo JSON file') {
     steps {
       container('busybox') {
          sh """
          echo '{"hello": "world"}' > ${HOME}/test.json
          """
       }
      }
    }
    stage ('Print JSON') {
     steps {
       container('jq') {
         sh "jq . ${HOME}/test.json"
       }
      }
    }
  }
}
