
import jenkins.model.*
import javaposse.jobdsl.plugin.*
import hudson.triggers.TimerTrigger
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition
import groovy.json.JsonSlurper


def home_dir = System.getenv("JENKINS_HOME")
GroovyShell shell = new GroovyShell()
def helpers = shell.parse(new File("$home_dir/init.groovy.d/Helpers.groovy"))

def jsonSlurper = new JsonSlurper()
def properties = jsonSlurper.parse(new File("$home_dir/properties/initial-jobs.json"))
def instance = Jenkins.getInstanceOrNull()

println "############################ STARTING INITIAL JOBS SETUP ############################"

properties.initialJobs.each { jobProperties ->
  def oldJob = instance.getJob(jobProperties.name)
  if (oldJob != null) {
    println ">>> Removendo job antigo: ${jobProperties.name}"
    oldJob.delete()
  }
  println ">>> Criando JOB ${jobProperties.name}"

  def project = instance.createProject(WorkflowJob.class, jobProperties.name)
  String pipelineScript = new File("$home_dir/config/initials/${jobProperties.pipelineFile}").text

  project.setDefinition(new CpsFlowDefinition(pipelineScript))
  project.addTrigger(new TimerTrigger("@midnight"))
  jobProperties.parameters.each { key, value ->
    helpers.addBuildParameter(project, key, value)
  }
  project.save()
}
instance.reload()

properties.initialJobs.each { job ->
  println ">>> Schedule ${job.name} seed jod"
  instance.getJob(job.name).scheduleBuild()
}