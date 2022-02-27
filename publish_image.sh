#!/usr/bin/env bash

dockerhub_user=pablords
image_name=jenkins-as-code
image_version=1.0.3

docker pull jenkins/jenkins

docker build --no-cache -t ${dockerhub_user}/${image_name}:${image_version} . 

docker push ${dockerhub_user}/${image_name}:${image_version}


