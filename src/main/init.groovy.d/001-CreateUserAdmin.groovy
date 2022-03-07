import jenkins.model.*
import hudson.security.*

def instance = Jenkins.getInstance()

println "############################ CREATE USER ADMIN #######################################################"


def hudsonRealm = new HudsonPrivateSecurityRealm(false)
hudsonRealm.createAccount("admin","admin")
instance.setSecurityRealm(hudsonRealm)


def strategy = new GlobalMatrixAuthorizationStrategy()
strategy.add(Jenkins.ADMINISTER, "admin")
instance.setAuthorizationStrategy(strategy)


instance.save()