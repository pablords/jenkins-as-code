FROM jenkins/jenkins

LABEL Author="Pablo Ruan dos Santos" 

ARG master_image_version="v.1.0.0"
ENV master_image_version $master_image_version

ENV KUBERNETES_SERVER_URL "https://192.168.50.10:6443"
ENV JENKINS_SERVER_URL "http://192.168.50.10:8080"
ENV JENKINS_TUNNEL_URL "192.168.50.10:50000"

ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false"

USER root

RUN mkdir /srv/backup && chown jenkins:jenkins /srv/backup

USER jenkins

# Plugins Install
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

# Auto Setup Scripts
COPY src/main/scripts/* /usr/share/jenkins/ref/init.groovy.d/
COPY src/main/config/*.properties /usr/share/jenkins/ref/config/
COPY src/main/jobs/* /usr/share/jenkins/ref/config/initials/


EXPOSE 8080 50000

