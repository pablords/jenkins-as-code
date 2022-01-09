#!/usr/bin/env bash

# COLOQUE AQUI SEU USUARIO DOCKERHUB
dockerhub_user=pablords

jenkins_port=8080
image_name=jenkins-as-code
image_version=1.0.0
container_name=jenkins

docker pull jenkins


docker stop ${container_name}

docker build --no-cache -t ${dockerhub_user}/${image_name}:${image_version} . 

if [ -d jobs ]; then
    rm -rf jobs
fi
if [ ! -d jobs ]; then
    mkdir jobs
fi

docker run -p ${jenkins_port}:8080 \
    -e KUBERNETES_SERVER_URL='http://kubernetes:4433' \
    -e JENKINS_SERVER_URL='http://jenkins:8080' \
    -v `pwd`/jobs:/var/jenkins_home/jobs/ \
    -v $HOME/.ssh:/var/jenkins_home/.ssh/ \
    --rm --name ${container_name} \
    ${dockerhub_user}/${image_name}:${image_version}
