/**
 * 本範例展示如何在 stage 中, 透過 helm 跟 kubernetes 互動
 */
pipeline {
  agent {
    kubernetes {
      cloud 'SLKE'
      defaultContainer 'helm'
      yaml """
kind: Pod
spec:
  # service account 需要先在 k8s cluster 中建立好
  # ref: https://github.com/softleader/slke/blob/main/manifests/namespaces/jenkins.yaml#L39
  serviceAccountName: jenkins
  containers:
  - name: helm
    image: lachlanevenson/k8s-helm
    command: ['cat']
    tty: true
    resources:
      limits:
        memory: "100Mi"
        cpu: "100m"
"""
    }
  }

  environment {
    NAMESPACE="default"
  }

  stages {
    stage ('Installing a Package') {
      steps {
        sh "helm install bitnami/nginx nginx-app -n ${NAMESPACE}"
      }
    }
    
    stage ('List released') {
      steps {
        sh "helm ls"
      }
    }

    stage ('Uninstall a Release') {
      steps {
        sh "helm uninstall nginx-app -n ${NAMESPACE}"
      }
    }
  }
}
