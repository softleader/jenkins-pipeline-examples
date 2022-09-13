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
  # service account 需要先在 k8s cluster 中建立好
  # ref: https://github.com/softleader/slke/blob/main/manifests/namespaces/jenkins.yaml#L39
  serviceAccountName: jenkins
  containers:
  - name: sftp-image
    image: harbor.softleader.com.tw/library/sftp-image
    command: ['cat']
    tty: true
    resources:
      limits:
        memory: "200Mi"
        cpu: "500m"
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
		HARBOR_ROBOT_NAME=${harbor_robot_name}
		HARBOR_ROBOT_TOKEN=${harbor_robot_token}
		USER=${sftp_user}
		PASSWORD=${sftp_password}
		REMOTE_DIR=${remove_dir}
		TAR_NAME_PREFIX=${tar_name_prefix}
		IMAGES=${images}
  }

  stages {
    stage ('Harbor Login') {
      steps {
        sh "docker login -u ${HARBOR_ROBOT_NAME} -p ${HARBOR_ROBOT_TOKEN}"
      }
    }

    stage ('SFTP Image') {
      steps {
        sh "/sftp-image.sh ${IMAGES}"
      }
    }
  }
}
