import com.datapipe.jenkins.vault.credentials.VaultCredential
import com.datapipe.jenkins.vault.credentials.common.VaultStringCredentialBinding
import com.datapipe.jenkins.vault.configuration.VaultConfiguration
import com.datapipe.jenkins.vault.credentials
import jenkins.model.Jenkins

println "############################ VAULT CONFIG ############################"


def descriptor = Jenkins.instance.getDescriptorByType(com.datapipe.jenkins.vault.configuration.GlobalVaultConfiguration)


def VAULT_URL = System.getenv('VAULT_URL') ?: ''

def VAULT_APP_ROLE_CREDENTIAL = 'VAULT_APP_ROLE_CREDENTIAL' ?: ''

def config = new VaultConfiguration(VAULT_URL, VAULT_APP_ROLE_CREDENTIAL, false)

config.setEngineVersion(1)
config.setSkipSslVerification(true)

descriptor.setConfiguration(config)
descriptor.save()



println "DONE VAULT CONFIG"