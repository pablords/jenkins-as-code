
Gere a imagem e rode o comando:

#####
docker run -p 8080:8080 -p 50000:50000 --name jenkins -e KUBERNETES_SERVER_URL=https://192.168.50.10:6443 -e JENKINS_SERVER_URL=http://192.168.50.10:8080 jenkins-dev