import com.datapipe.jenkins.vault.credentials.VaultAppRoleCredential
import com.cloudbees.plugins.credentials.domains.Domain
import com.cloudbees.plugins.credentials.CredentialsScope
import hudson.util.Secret


println "############################ GET CREDENTIALS #######################################################"


role_id = null
secret_id = null

instance = Jenkins.instance
domain = Domain.global()
store = instance.getExtensionList("com.cloudbees.plugins.credentials.SystemCredentialsProvider")[0].getStore()

VAULT_URL = System.getenv('VAULT_URL') + "/v1/" ?: ''


def VAULT_TOKEN_ADMIN = System.getenv('VAULT_TOKEN_ADMIN') ?: ''

def getRoleId = new URL(VAULT_URL + "auth/approle/role/jenkins-role/role-id").openConnection();
getRoleId.setRequestMethod("GET")
getRoleId.setDoOutput(true);
getRoleId.setRequestProperty("Content-Type", "application/json")
getRoleId.setRequestProperty("Accept", "application/json");
getRoleId.setRequestProperty("X-Vault-Token", VAULT_TOKEN_ADMIN)
println(getRoleId.getResponseCode());

if(getRoleId.getResponseCode().equals(200)) {
    def pjson = new groovy.json.JsonSlurper().parse(getRoleId.getInputStream())
    assert pjson instanceof Map
    role_id = pjson.data['role_id']
}


def getSecretId = new URL(VAULT_URL + "auth/approle/role/jenkins-role/secret-id").openConnection()
getSecretId.setRequestMethod("POST")
getSecretId.setDoOutput(true);
getSecretId.setRequestProperty("Content-Type", "application/json")
getSecretId.setRequestProperty("Accept", "application/json");
getSecretId.setRequestProperty("X-Vault-Token", VAULT_TOKEN_ADMIN)
println(getSecretId.getResponseCode());

if(getSecretId.getResponseCode().equals(200)) {
    def pjson = new groovy.json.JsonSlurper().parse(getSecretId.getInputStream())
    assert pjson instanceof Map
    secret_id = pjson.data['secret_id']
}



secretText = new VaultAppRoleCredential(
        CredentialsScope.GLOBAL,
        "VAULT_APP_ROLE_CREDENTIAL",
        "VAULT_APP_ROLE_CREDENTIAL",
        role_id,
        Secret.fromString(secret_id),
        "")
store.addCredentials(domain, secretText)

