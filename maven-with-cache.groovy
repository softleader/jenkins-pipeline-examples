/**
 * 本範例展示了如何 cache maven m2, 讓每次的 maven build 不用重新下載之前載過的 dependencies
 * m2-claim 是事先建立的, 設定檔在 https://github.com/softleader/slke/blob/main/manifests/namespaces/jenkins.yaml
 */
pipeline {
  agent {
    kubernetes {
      cloud 'SLKE'
      workspaceVolume persistentVolumeClaimWorkspaceVolume(claimName: 'workspace-claim', readOnly: false)
      defaultContainer 'maven'
      yaml """
kind: Pod
spec:
  # All containers should have the same UID
  securityContext:
    runAsUser: 0
  containers:
  - name: maven
    image: harbor.softleader.com.tw/library/maven:3-eclipse-temurin-11
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
