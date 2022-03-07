import jenkins.model.Jenkins
import org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl
import com.cloudbees.plugins.credentials.domains.Domain
import com.cloudbees.plugins.credentials.CredentialsScope
import hudson.util.Secret
import groovy.json.JsonSlurper



println "############################ ADD CREDENTIALS #######################################################"

def home_dir = System.getenv("JENKINS_HOME")
def jsonSlurper = new JsonSlurper()
def properties = jsonSlurper.parse(new File("$home_dir/properties/creds.json"))



instance = Jenkins.instance
domain = Domain.global()
store = instance.getExtensionList("com.cloudbees.plugins.credentials.SystemCredentialsProvider")[0].getStore()

VAULT_URL = System.getenv('VAULT_URL') + "/v1/" ?: ''


def VAULT_TOKEN_ADMIN = System.getenv('VAULT_TOKEN_ADMIN') ?: ''

properties.credentials.each { it ->

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











