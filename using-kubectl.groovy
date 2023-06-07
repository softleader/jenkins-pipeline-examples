/**
 * 本範例展示如何在 stage 中, 透過 kubectl 跟 kubernetes 互動
 */
pipeline {
  agent {
    kubernetes {
      cloud 'SLKE'
      defaultContainer 'kubectl'
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
  - name: kubectl
    image: lachlanevenson/k8s-kubectl
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
    stage ('Run an nginx Deployment') {
      steps {
        sh "kubectl create deployment --image=nginx nginx-app -n ${NAMESPACE}"
      }
    }
    
    stage ('Get nginx Pod') {
      steps {
        sh "kubectl get po -l app=nginx-app -n ${NAMESPACE}"
      }
    }

    stage ('Remove nginx Deployment') {
      steps {
        sh "kubectl delete deployment nginx-app -n ${NAMESPACE}"
      }
    }
  }
}
