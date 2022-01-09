import java.lang.System
import hudson.model.*
import jenkins.model.*
import javaposse.jobdsl.plugin.*
import hudson.triggers.TimerTrigger
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition

def home_dir = System.getenv("JENKINS_HOME")
GroovyShell shell = new GroovyShell()
def helpers = shell.parse(new File("$home_dir/init.groovy.d/Helpers.groovy"))
def properties = new ConfigSlurper().parse(new File("$home_dir/config/initial-jobs.properties").toURI().toURL())
def instance = Jenkins.getInstanceOrNull()

println "############################ STARTING INITIAL JOBS SETUP ############################"

properties.initialjobs.each { jobProperties ->
  def oldJob = instance.getJob(jobProperties.value.get('name'))
  if (oldJob != null) {
    println ">>> Removendo job antigo: ${jobProperties.value.name}"
    oldJob.delete()
  }
  println ">>> Criando JOB ${jobProperties.value.name}"

  def project = instance.createProject(WorkflowJob.class, jobProperties.value.name)
  String pipelineScript = new File("$home_dir/config/initials/${jobProperties.value.pipelineFile}").text

  project.setDefinition(new CpsFlowDefinition(pipelineScript))
  project.addTrigger(new TimerTrigger("@midnight"))
  jobProperties.value.parameters.each { key, value ->
    helpers.addBuildParameter(project, key, value)
  }
  project.save()
}
instance.reload()

properties.initialjobs.each { job ->
  println ">>> Schedule ${job.value.name} seed jod"
  instance.getJob(job.value.name).scheduleBuild()
}