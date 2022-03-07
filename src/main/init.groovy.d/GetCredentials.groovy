import jenkins.model.*

def jenkinsCredentials = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(
        com.cloudbees.plugins.credentials.Credentials.class,
        Jenkins.instance,
        null,
        null
);


for (creds in jenkinsCredentials) {
    switch (creds.id){
        case "VAULT_APP_ROLE_CREDENTIAL":
            println("id: " + creds.id)
            println("secret: " + creds.secretId)
            break
        case "NEXUS_CREDENTIAL":
            println("user: " + creds.username)
            println("password: " + creds.password)
            break
        case "SONAR_CREDENTIAL":
            println("id: " + creds.id)
            println("secret: " + creds.secret)
            break
        case "K8S_CREDENTIAL":
            println("id: " + creds.id)
            println("secret: " + creds.secret)
            break
    }
}