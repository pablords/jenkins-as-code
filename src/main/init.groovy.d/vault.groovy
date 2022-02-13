
import com.datapipe.jenkins.vault.configuration.VaultConfiguration
import com.cloudbees.plugins.credentials.CredentialsScope
import jenkins.model.Jenkins
import com.datapipe.jenkins.vault.configuration.*


println "############################ VAULT CONFIG ############################"


def descriptor = Jenkins.instance.getDescriptorByType(com.datapipe.jenkins.vault.configuration.GlobalVaultConfiguration)


def VAULT_URL = System.getenv('SONARQUBE_URL') ?: 'http://devops-dev.ddns.net:8083'

def VAULT_APP_ROLE_CREDENTIAL = System.getenv('VAULT_APP_ROLE_CREDENTIAL') ?: ''

def config = new VaultConfiguration(VAULT_URL, VAULT_APP_ROLE_CREDENTIAL, false)

config.setEngineVersion(1)
config.setSkipSslVerification(true)

descriptor.setConfiguration(config)
descriptor.save()

println "DONE VAULT CONFIG"