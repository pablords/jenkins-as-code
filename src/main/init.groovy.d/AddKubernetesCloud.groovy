import jenkins.model.Jenkins
import org.csanchez.jenkins.plugins.kubernetes.ContainerTemplate
import org.csanchez.jenkins.plugins.kubernetes.KubernetesCloud
import org.csanchez.jenkins.plugins.kubernetes.PodTemplate
import groovy.json.JsonSlurper

println "############################ KUBERNETES CLOUDs SETUP ################################################"

if( ! System.getenv().containsKey('KUBERNETES_SERVER_URL') ) {
    println(">>> ENV VAR 'KUBERNETES_SERVER_URL' not found")
    println(">>> Please set them before start Jenkins")
    return
}

def jenkins = Jenkins.getInstanceOrNull()
def cloudList = jenkins.clouds

def home_dir = System.getenv("JENKINS_HOME")
def jsonSlurper = new JsonSlurper()
def properties = jsonSlurper.parse(new File("$home_dir/properties/clouds.json"))


properties.kubernetes.each { cloudKubernetes ->
    println ">>> Kubernetes Cloud Setting up: " + cloudKubernetes.name.toString()

    List<PodTemplate> podTemplateList = new ArrayList<PodTemplate>()
    cloudKubernetes.pods.each { podTemplate ->
        println ">>>>>> POD Template setup: " + podTemplate.name.toString()
        def newPodTemplate = createBasicPODTemplate(podTemplate)

        List<ContainerTemplate> containerTemplateList = new ArrayList<ContainerTemplate>()
        cloudKubernetes.containers.each { containerTemplate ->
            println ">>>>>>>>> Container Template setup: " + containerTemplate.name.toString()
            containerTemplateList.add( createBasicContainerTemplate(containerTemplate) )
        }

        newPodTemplate.setContainers(containerTemplateList)
        podTemplateList.add(newPodTemplate)
    }
    def kubernetesCloud = createKubernetesCloud(cloudKubernetes, podTemplateList)
    cloudList.add(kubernetesCloud)
}
jenkins.save()
println("Clouds Adicionadas: " + Jenkins.getInstanceOrNull().clouds.size())

// METODOS AUXILIARES
def createKubernetesCloud(cloudKubernetes, podTemplateList) {
    def serverUrl = System.getenv("KUBERNETES_SERVER_URL")
    def jenkinsUrl = System.getenv("JENKINS_SERVER_URL")
    def jenkinsTunnelUrl = System.getenv("JENKINS_TUNNEL_URL")
    def kubernetesCloud = new KubernetesCloud(
            cloudKubernetes.name.toString(),
            podTemplateList,
            serverUrl,
            cloudKubernetes.namespace.toString(),
            jenkinsUrl,
            cloudKubernetes.containerCapStr.toString(),
            Integer.parseInt(cloudKubernetes.connectTimeout.toString()),
            Integer.parseInt(cloudKubernetes.readTimeout.toString()),
            Integer.parseInt(cloudKubernetes.retentionTimeout.toString())
    )
    kubernetesCloud.setJenkinsTunnel(jenkinsTunnelUrl)
    kubernetesCloud.setSkipTlsVerify(true)
    kubernetesCloud.setCredentialsId("K8S_CREDENTIAL")
    return kubernetesCloud
}

def createBasicPODTemplate(podTemplate) {
    PodTemplate defaultPod = new PodTemplate()
    defaultPod.setName(podTemplate.name.toString())
    defaultPod.setNamespace(podTemplate.namespace.toString())
    defaultPod.setLabel(podTemplate.label.toString())
    return defaultPod
}

def createBasicContainerTemplate(containerTemplate) {
    ContainerTemplate basicContainerTemplate = new ContainerTemplate(
            containerTemplate.name.toString(),
            containerTemplate.image.toString(),
            containerTemplate.command.toString(),
            containerTemplate.args.toString())
    basicContainerTemplate.setTtyEnabled(Boolean.valueOf(containerTemplate.ttyEnabled.toString()))
    return basicContainerTemplate;
}
