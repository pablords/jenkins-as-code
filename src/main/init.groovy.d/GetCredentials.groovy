def jenkinsCredentials = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(
        com.cloudbees.plugins.credentials.Credentials.class,
        Jenkins.instance,
        null,
        null
);


for (creds in jenkinsCredentials) {
    if(creds.id == "VAULT_APP_ROLE_CREDENTIAL"){

        println(creds.id)
        println(creds.secretId)
    }
}