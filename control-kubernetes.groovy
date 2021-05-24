/**
 * 本範例展示如何在 stage 中, 跟 kubernetes 互動, serviceAccountName 必須先在 k8s cluster 中設定好!
 * @see https://github.com/softleader/slke/blob/main/manifests/namespaces/jenkins.yaml#L39
 */
pipeline {
  agent {
    kubernetes {
      cloud 'SLKE'
      defaultContainer 'kubectl'
      yaml """
kind: Pod
spec:
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

  stages {
    stage ('Run an nginx Deployment in namespace: softleader-system') {
      steps {
        sh "kubectl create deployment --image=nginx nginx-app -n softleader-system"
      }
    }
    
    stage ('Get nginx pod in namespace: softleader-system') {
      steps {
        sh "kubectl get po -l app=nginx-app -n softleader-system"
      }
    }

    stage ('Remove nginx Deployment in namespace: softleader-system') {
      steps {
        sh "kubectl delete deployment nginx-app -n softleader-system"
      }
    }
  }
}
