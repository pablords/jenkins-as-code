
######
para configuracao fora do cluster
criacao de namespace / serviceaccount jenkins com output token

kubectl create namespace jenkins && kubectl create serviceaccount jenkins --namespace=jenkins && kubectl describe secret $(kubectl describe serviceaccount jenkins --namespace=jenkins | grep Token | awk '{print $2}') --namespace=jenkins && kubectl create rolebinding jenkins-admin-binding --clusterrole=admin --serviceaccount=jenkins:jenkins --namespace=jenkins



Criar secret VAULT_ROOT

kubectl create secret generic vault-root-credential \
--from-literal=secret="" \
--namespace="jenkins"


kubectl create clusterrolebinding jenkins --clusterrole cluster-admin --serviceaccount=jenkins:default


helm upgrade --install jenkins-as-code .