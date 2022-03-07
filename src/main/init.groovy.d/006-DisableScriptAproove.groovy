import org.jenkinsci.plugins.scriptsecurity.scripts.*

println "############################ DISABLED SCRIPT APPROVAL ################################################"

// Disable Job DSL script approval

toApprove = ScriptApproval.get().getPendingScripts().collect()
toApprove.each {pending -> ScriptApproval.get().approveScript(pending.getHash())}