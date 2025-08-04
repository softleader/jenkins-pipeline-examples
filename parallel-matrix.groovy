/**
 * 本範例展示了如何 parallel 的執行 matrix 不同的 container
 */
def javaVersions = ['17', '21']

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
  - name: maven-java17
    image: harbor.softleader.com.tw/library/maven:3-eclipse-temurin-17
    command: ['cat']
    tty: true
    resources:
      limits:
        memory: "10Mi"
        cpu: "10m"
  - name: maven-java21
    image: harbor.softleader.com.tw/library/maven:3-eclipse-temurin-21
    command: ['cat']
    tty: true
    resources:
      limits:
        memory: "10Mi"
        cpu: "10m"
"""
    }
  }

  stages {
    stage('Parallel Matrix') {
      steps {
        script {
          def matrixJobs = [:]
          for (def javaVersion : javaVersions) {
            def version = javaVersion // 需綁定 local scope 避免 closure 引用錯誤
            matrixJobs["JAVA = ${version}"] = {
              stage("Matrix - JAVA = ${version}") {
                container("maven-java${version}") {
                  sh "echo ${version}"
                }
              }
            }
          }
          parallel matrixJobs
        }
      }
    }
  }
}
