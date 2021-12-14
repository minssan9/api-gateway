node {
    //git_branch = sed -i 's|origin/|'"${params.GIT_BRANCH}"'|'
    stage("Parameter Check") {
        echo 'Start'
        echo "env.JOB_NAME - ${env.JOB_NAME}"
        echo "env.gitlabBranch - ${env.gitlabBranch}"
        echo "params.GIT_BRANCH - ${params.GIT_BRANCH}"

        git_branch = "${env.gitlabBranch.replace("origin/", "")}"

        echo "build with this branch : ${git_branch}"
    }
    stage ('Clone'){
        git branch: "${git_branch}", credentialsId: 'gitlab_deploy', url: 'http://10.20.101.172:8111/hds_api/hds_api_gateway.git'
    }
    stage("Compilations") {
        sh "chmod +x gradlew"
        sh "./gradlew clean bootjar -x test"
    }

    stage("Staging") {
        // sh "pid=\$(lsof -i:8989 -t); kill -TERM \$pid "
        //   + "|| kill -KILL \$pid"
        // withEnv(['JENKINS_NODE_COOKIE=dontkill']) {
        //     sh 'nohup ./mvnw spring-boot:run -Dserver.port=8989 &'
        // }
        sh "docker build -t hds_api_gateway:${BUILD_NUMBER} ."
        sh "docker tag hds_api_gateway:${BUILD_NUMBER} 10.20.101.172:5000/hds_api_gateway"
        sh "docker push 10.20.101.172:5000/hds_api_gateway"

        try {
            // develop redeploy
            sh """
            curl -u "${HOMS_CATTLE_ACCESS_KEY}:${HOMS_CATTLE_SECRET_KEY}" \
            -X POST \\
            -H "Accept: application/json" \\
            -H "Content-Type: application/json" \\
            "https://10.20.101.172/v3/project/c-266jz:p-d7tbd/workloads/daemonset:erp-dev:hds-api-gateway-${git_branch}?action=redeploy" --insecure
            """
        } catch (e) {
            sh 'echo develop deploy Fail!!'
        }

        try {
            // release redeploy
            sh """
            curl -u "${HOMS_CATTLE_ACCESS_KEY}:${HOMS_CATTLE_SECRET_KEY}" \
            -X POST \
            -H "Accept: application/json" \
            -H "Content-Type: application/json" \
            "https://10.20.101.172/v3/project/c-hf5s6:p-qf444/workloads/daemonset:hds-web:hds-api-gateway-${git_branch}?action=redeploy" --insecure
            """
        } catch (e) {
            sh 'echo develop deploy Fail!!'
        }
    }
}
