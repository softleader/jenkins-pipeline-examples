/**
 * 本範例展示如何將一個 spring boot 專案打包成 image
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
    image: ghcr.io/carlossg/maven:3-eclipse-temurin-11
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
    IMAGE_NAME="jenkins-pipeline-examples/spring-build-image:1.0.0"
  }

  stages {
    stage ('Download source code') {
     steps {
       container('curl') {
        sh """
        curl -SL https://start.spring.io/starter.zip -d javaVersion=11 -o ${DATA}/demo.zip
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
        mvn -f ${DATA}/pom.xml spring-boot:build-image -Dspring-boot.build-image.imageName=${IMAGE_NAME}
        """
       }
       container('docker') {
        sh "docker rmi -f ${IMAGE_NAME}"
       }
      }
    }
  }
}
