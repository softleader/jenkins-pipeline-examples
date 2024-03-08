/**
 * 本範例展示如何將一個 Kapok 專案打包成 native image
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
  - name: curl
    image: curlimages/curl
    command: ['cat']
    tty: true
    volumeMounts:
    - name: shared-data
      mountPath: /data
  - name: maven
    image: harbor.softleader.com.tw/library/maven:3-graalvm-community-21
    command: ['cat']
    tty: true
    volumeMounts:
    - name: shared-data
      mountPath: /data
    - name: m2
      mountPath: /root/.m2
    - name: dockersock
      mountPath: /var/run/docker.sock
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
  - name: shared-data
    emptyDir: {}
  - name: m2
    persistentVolumeClaim:
      claimName: m2-claim
  - name: dockersock
    hostPath:
      path: /var/run/docker.sock
"""
    }
  }

  environment {
    DATA="/data"
    IMAGE_NAME="jenkins-pipeline-examples/kapok-build-native:1.0.0"
  }

  stages {
    stage ('Download source code') {
     steps {
       container('curl') {
        sh """
        curl -SL http://start-kapok.192.168.1.240.nip.io/starter.zip?dependencies=native -o ${DATA}/demo.zip
        unzip ${DATA}/demo.zip -d ${DATA}
        rm -f ${DATA}/demo.zip
        """
       }
      }
    }

    stage ('Compile and build image') {
     steps {
       container('maven') {
        sh """
        mvn -f ${DATA}/pom.xml spring-boot:build-image -Pnative -Dspring-boot.build-image.imageName=${IMAGE_NAME}
        """
       }
       container('docker') {
        sh """
        docker run --rm ${IMAGE_NAME}
        docker rmi -f ${IMAGE_NAME}
        """
       }
      }
    }
  }
}
