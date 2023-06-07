/**
 * 本範例展示如何在 stage 中, 透過 sftp-image 上傳 image 到公司 sftp 中
 */
pipeline {
  agent {
    kubernetes {
      cloud 'SLKE'
      defaultContainer 'sftp-image'
      yaml """
kind: Pod
spec:
  # All containers should have the same UID
  securityContext:
    runAsUser: 0
  # service account 需要先在 k8s cluster 中建立好
  # ref: https://github.com/softleader/slke/blob/main/manifests/namespaces/jenkins.yaml#L39
  serviceAccountName: jenkins
  containers:
  - name: sftp-image
    image: harbor.softleader.com.tw/library/sftp-image
    imagePullPolicy: Always
    command: ['cat']
    tty: true
    resources:
      limits:
        memory: "500Mi"
        cpu: "1"
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

  environment {
		USER="${sftp_user}"
		PASSWORD="${sftp_password}"
		REMOTE_DIR="${sftp_remote_dir}"
		TAR_NAME="${upload_name}"
		TAR_NAME_PREFIX="${upload_name_prefix}"
  }

  stages {

    stage('確認環境變數') {
      steps {
        sh 'printenv'
      }
    }

    stage ('Harbor Login') {
      steps {
        sh "docker login harbor.softleader.com.tw -u '${harbor_robot_name}' -p '${harbor_robot_token}'"
      }
    }

    stage ('SFTP Image') {
      steps {
        sh "/sftp-image.sh ${images}"
      }
    }
  }
}
