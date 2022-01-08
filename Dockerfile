FROM jenkins/jenkins

USER root

RUN mkdir /srv/backup && chown jenkins:jenkins /srv/backup
RUN mkdir /var/jenkins_home/config/

USER jenkins

# Plugins Install
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

# Auto Setup Scripts
COPY src/main/groovy/* /usr/share/jenkins/ref/init.groovy.d/
COPY src/main/config/*.properties ${JENKINS_HOME}/config/
COPY src/main/pipelines/* ${JENKINS_HOME}/config/initials/



EXPOSE 8080 50000

