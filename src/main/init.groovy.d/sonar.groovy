import jenkins.model.Jenkins
import hudson.plugins.sonar.SonarRunnerInstallation
import hudson.plugins.sonar.SonarGlobalConfiguration
import hudson.plugins.sonar.SonarInstallation
import hudson.plugins.sonar.model.TriggersConfig



println "###################### SONAR CONFIG ######################"


def jenkins = Jenkins.getInstance()
descriptor    = jenkins.getDescriptor(SonarGlobalConfiguration.class)

def SONARQUBE_HTTP_ENDPOINT = System.getenv('SONARQUBE_HTTP_ENDPOINT') ?: 'http://devops-dev.ddns.net:9000'

def config = new SonarInstallation(
	"sonarqube",              //name
	SONARQUBE_HTTP_ENDPOINT,  //serverUrl
	null,                     //credentialsId
	null,                     //serverAuthenticationToken
	"",                       //webhookSecretId
	"",                       //additionalProperties
	"",                       //additionalAnalysisProperties
	"",                        //mojoVersion
	new TriggersConfig()       //triggers
)

descriptor.setInstallations(config)
descriptor.save()
jenkins.save()

println "DONE SONAR CONFIG"



