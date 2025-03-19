def call(Map config) {
    // Validate required parameters
    if (!config.gitUrl) error("Git URL is required")
    if (!config.dockerfilePath) error("Dockerfile path is required")

    def targetBranch = config.branch ?: 'main'

    def sanitizedJobName = env.JOB_NAME
        .replaceAll(/[^a-zA-Z0-9_.-]/, '-')
        .replaceAll(/-+/, '-')
        .replaceAll(/^-|-$/, '')
        .toLowerCase()

    def imageName = sanitizedJobName
    def buildTag = env.BUILD_NUMBER
    def imageWithTag = "${imageName}:${buildTag}"

    node {
        stage('Clone Repository') {
            checkout([
                $class: 'GitSCM',
                branches: [[name: "*/${targetBranch}"]],
                userRemoteConfigs: [[
                    url: config.gitUrl,
                    credentialsId: 'gitlab-credentials' // Use the stored credentials
                ]]
            ])
        }

        stage('Build Docker Image') {
            script {
                docker.build(imageWithTag, "--file ${config.dockerfilePath} .")
            }
        }

        stage('Push to Docker Hub') {
            script {
                docker.withRegistry('https://novacisdockerhub.hub.docker.com','docker-hub-cred') {
                    docker.image(imageWithTag).push()
                }
            }
        }

        currentBuild.description = """
            Built Image: ${imageWithTag}
            Branch: ${targetBranch}
        """
    }

    return imageWithTag
}
