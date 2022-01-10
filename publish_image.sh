#!/usr/bin/env bash

dockerhub_user=pablords
image_name=jenkins-as-code
image_version=1.0.0

docker pull jenkins

docker build --no-cache -t ${dockerhub_user}/${image_name}:${image_version} . 

