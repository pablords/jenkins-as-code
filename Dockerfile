FROM jenkins/jenkins

LABEL Author="Pablo Ruan dos Santos" 

ARG master_image_version="v.1.0.0"
ENV master_image_version $master_image_version

ENV KUBERNETES_SERVER_URL "http://kubernetes:8443"
ENV JENKINS_SERVER_URL "http://jenkins:8080"
ENV JENKINS_TUNNEL_URL "192.168.50.10:50000"

USER root

RUN mkdir /srv/backup && chown jenkins:jenkins /srv/backup

USER jenkins

# Plugins Install
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

# Auto Setup Scripts
COPY src/main/groovy/* /usr/share/jenkins/ref/init.groovy.d/
COPY src/main/config/*.properties /usr/share/jenkins/ref/config/
COPY src/main/pipelines/* /usr/share/jenkins/ref/config/initials/


EXPOSE 8080 50000

