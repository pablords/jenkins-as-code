import com.datapipe.jenkins.vault.credentials.VaultAppRoleCredential
import jenkins.model.Jenkins
import com.cloudbees.plugins.credentials.domains.Domain
import org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl
import com.cloudbees.plugins.credentials.CredentialsScope
import hudson.util.Secret
import jenkins.model.*
import com.datapipe.jenkins.vault.credentials.VaultAppRoleCredential
import com.cloudbees.plugins.credentials.domains.Domain
import com.cloudbees.plugins.credentials.CredentialsScope
import hudson.util.Secret


credentials = [
        [
            id: "NEXUS_CREDENTIAL",
            description: "nexus credential",
            path: "kv/apps/nexus"
        ],
        [
            id: "SONAR_CREDENTIAL",
            description: "sonar credential",
            path: "kv/apps/sonar"
        ],
        [
            id: "K8S_CREDENTIAL",
            description: "k8s credential",
            path: "kv/apps/k8s"
        ],
]




instance = Jenkins.instance
domain = Domain.global()
store = instance.getExtensionList("com.cloudbees.plugins.credentials.SystemCredentialsProvider")[0].getStore()

VAULT_URL = System.getenv('VAULT_URL') + "/v1/" ?: ''


def VAULT_TOKEN_ADMIN = System.getenv('VAULT_TOKEN_ADMIN') ?: 's.2WxouO1uLR0aszXtY2iYaEdU'

credentials.each { it ->

    def getCredential = new URL(VAULT_URL + it.path).openConnection();
    getCredential.setRequestMethod("GET")
    getCredential.setDoOutput(true);
    getCredential.setRequestProperty("Content-Type", "application/json")
    getCredential.setRequestProperty("Accept", "application/json");
    getCredential.setRequestProperty("X-Vault-Token", VAULT_TOKEN_ADMIN)
    println(getCredential.getResponseCode());

    if(getCredential.getResponseCode().equals(200)) {
        def pjson = new groovy.json.JsonSlurper().parse(getCredential.getInputStream())
        assert pjson instanceof Map
        secret = pjson.data['secret']

        if(it.id == "NEXUS_CREDENTIAL"){
            usernameAndPassword = new UsernamePasswordCredentialsImpl(
                    CredentialsScope.GLOBAL,
                    it.id,
                    it.description,
                    "jenkins",
                    secret.toString()
            )
            store.addCredentials(domain, usernameAndPassword)
        }else {
            secretText = new StringCredentialsImpl(
                    CredentialsScope.GLOBAL,
                    it.id,
                    it.description,
                    Secret.fromString(secret)
            )
            store.addCredentials(domain, secretText)
        }

    }
}











