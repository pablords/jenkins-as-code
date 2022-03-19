
<p align="center">
  <a href="#-tecnologias">Tecnologias</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#-projeto">Projeto</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#-como-executar">Como executar</a>&nbsp;&nbsp;&nbsp;
  <a href="#-requisitos-funcionais">Requisitos funcionais</a>&nbsp;&nbsp;&nbsp;
</p>


<br>

<p align="center">
  <img alt="partners" src=".github/jenkins-logo.png" width="100%">
</p>

## ✨ Tecnologias

Esse projeto foi desenvolvido com as seguintes tecnologias:

- [Groovy](https://groovy-lang.org/)
- [Jenkins](https://www.jenkins.io/)
- [Docker](https://www.docker.com/)
- [Kubernetes](https://kubernetes.io)
- [HashCorpVault](https://www.vaultproject.io/)



## 📝 Projeto

jenkins configurado como código.
Esse projeto foi desenvolvido no ambiente [`https://github.com/pablords/lab-kubernetes-vagrant-ansible`](https://github.com/pablords/lab-kubernetes-vagrant-ansible)

## 💻  Como executar com docker-compose

- Clone o repositório.
- Para subir o jenkins com docker-compose é necessário adicionar o token root do vault para a variavel de ambiente `VAULT_TOKEN_ADMIN`
- Na raiz do projeto em um terminal digite `docker-compose up -d` e aguarde até que container suba.
- Execute o comando `kubectl create namespace jenkins && kubectl create serviceaccount jenkins --namespace=jenkins && kubectl describe secret $(kubectl describe serviceaccount jenkins --namespace=jenkins | grep Token | awk '{print $2}') --namespace=jenkins && kubectl create rolebinding jenkins-admin-binding --clusterrole=admin --serviceaccount=jenkins:jenkins --namespace=jenkins` atualize a credential `K8S_CREDENTIAL` no jenkins com o novo token gerado

Agora você pode acessar [`http://192.168.50.10:8080`](http://192.168.50.10:8080) do seu navegador.



## 💻  Como executar o projeto no k8s

- Clone o repositório.
- Crie a secret do vault com o comando `kubectl create secret generic vault-root-credential --from-literal=secret="{TOKEN_VAULT}" --namespace="jenkins"`
- Vá até a pasta k8s e digite `kubectl apply -k .` depois pegue o token criado para o service account do jenkins com `kubectl get secrets -n jenkins` + `kubectl describe secret {NOME-DA-SECRET}` atualize a credential `K8S_CREDENTIAL` no jenkins com o novo token gerado
- Para pegar o ip para acesso http ao jenkins digite `kubectl get svc ingress-nginx-controller -n ingress-nginx`


## 🔖  Requisitos funcionais

- Configuracao de agent k8s.
- Configuracao do Sonar.
- Configuracao do Vault.
- Criacao de usuario.
- Configuracao global. 
- Configuracao de jobs/pipelines



