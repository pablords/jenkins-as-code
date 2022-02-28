import jenkins.branch.BranchPropertyStrategy
import jenkins.branch.BranchSource
import jenkins.branch.MultiBranchProject
import jenkins.model.Jenkins
import jenkins.scm.api.SCMSource
import groovy.json.JsonSlurper

def home_dir = System.getenv("JENKINS_HOME")
def jsonSlurper = new JsonSlurper()
def properties = jsonSlurper.parse(new File("$home_dir/properties/initial-jobs.json"))
def jenkins = Jenkins.getInstanceOrNull()

println "############################ STARTING INITIAL PIPELINES SETUP ############################"

properties.pipelines.each { pipeline ->
    MultiBranchProject project = jenkins.createProject(MultiBranchProject.class, pipeline.name)

    project.displayName = pipeline.displayName

    // Setup dos Repositorios de Origem
    List<BranchSource> sources = new ArrayList<>()
    SCMSource source = null // SCM Core
    BranchPropertyStrategy strategy = null
    BranchSource branchSource = new BranchSource(source, strategy) // Branch
    project.setSourcesList()
    project.save()
}

jenkins.reload()
