
######
para configuracao fora do cluster
criacao de namespace / serviceaccount jenkins com output token

kubectl create namespace jenkins && kubectl create serviceaccount jenkins --namespace=jenkins && kubectl describe secret $(kubectl describe serviceaccount jenkins --namespace=jenkins | grep Token | awk '{print $2}') --namespace=jenkins && kubectl create rolebinding jenkins-admin-binding --clusterrole=admin --serviceaccount=jenkins:jenkins --namespace=jenkins



kubectl create namespace partners && kubectl create serviceaccount partners --namespace=partners && kubectl describe secret $(kubectl describe serviceaccount partners --namespace=partners | grep Token | awk '{print $2}') --namespace=partners && kubectl create rolebinding partners-admin-binding --clusterrole=admin --serviceaccount=partners:partners --namespace=partners

Criar secret VAULT_ROOT

kubectl create secret generic vault-root-credential \
--from-literal=secret="" \
--namespace="jenkins"


kubectl create clusterrolebinding jenkins --clusterrole cluster-admin --serviceaccount=jenkins:default

