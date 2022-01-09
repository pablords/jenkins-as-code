 liberar comando jenkins cli rmeoto
 ######
 <authorizationStrategy class="hudson.security.FullControlOnceLoggedInAuthorizationStrategy">  
    <denyAnonymousReadAccess>false</denyAnonymousReadAccess>  
  </authorizationStrategy>  


  docker run -p 8080:8080 -p 50000:50000 -d --name jenkins -e KUBERNETES_SERVER_URL=https://192.168.50.10:6443 -e JENKINS_SERVER_URL=http://192.168.50.10:8080 pablords/jenkins-as-code


  docker run -p 8080:8080 -p 50000:50000 -d --name jenkins pablords/jenkins-as-code