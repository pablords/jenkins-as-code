FROM jenkins/jenkins

LABEL Author="Pablo Ruan dos Santos" 

ARG master_image_version="v.1.0.4"
ENV master_image_version $master_image_version

ENV KUBERNETES_SERVER_URL=https://192.168.50.10:6443
ENV JENKINS_SERVER_URL=http://192.168.50.10:8080
ENV JENKINS_TUNNEL_URL=192.168.50.10:50000
ENV SONARQUBE_URL=http://dev-testdevops.ddns.net:9000
ENV VAULT_URL=http://dev-testdevops.ddns.net:8083

ENV HOST_URL=http://dev-testdevops.ddns.net

ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false"
ENV JAVA_OPTS="-Dpermissive-script-security.enabled=true"

USER jenkins

# Plugins Install
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

# Auto Setup Scripts
COPY src/main/init.groovy.d/* /usr/share/jenkins/ref/init.groovy.d/
COPY src/main/properties/* /usr/share/jenkins/ref/properties/
COPY src/main/jobs/* /usr/share/jenkins/ref/config/initials/








