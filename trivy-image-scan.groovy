/**
 * 本範例展示如何在 stage 中, 透過 trivy 做 image 弱點掃描
 * trivy-claim 是事先建立的, 設定檔在 https://github.com/softleader/slke/blob/main/manifests/namespaces/jenkins.yaml
 */
pipeline {
  agent {
    kubernetes {
      cloud 'SLKE'
      defaultContainer 'trivy'
      yaml """
kind: Pod
spec:
  containers:
  - name: trivy
    image: aquasec/trivy:0.19.1
    command: ['cat']
    tty: true
    resources:
      limits:
        memory: "100Mi"
        cpu: "100m"
    volumeMounts:
    - name: trivy
      mountPath: /root/.cache/trivy
  volumes:
  - name: trivy
    persistentVolumeClaim:
      claimName: trivy-claim
"""
    }
  }
  
  environment {
    SCAN_IMAGE = "adoptopenjdk/openjdk11:alpine-jre"
  }

  stages {
     stage ('Image Scan') {
      steps {
        sh """
        trivy image --severity MEDIUM,HIGH,CRITICAL --exit-code 1 ${SCAN_IMAGE}
        """
      }
    }
  } 
}
