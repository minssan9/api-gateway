node {
    //git_branch = sed -i 's|origin/|'"${params.GIT_BRANCH}"'|'
    git_branch = "${params.GIT_BRANCH.replace("origin/", "")}"
    stage("Parameter Check") {
        echo 'Start'
        echo "${env.JOB_NAME}"
        echo "${params.GIT_BRANCH}"
        echo "${git_branch}"

    }
    stage ('Clone'){
        git branch: "${git_branch}", credentialsId: 'gitlab_deploy', url: 'http://10.20.101.172:8111/hds_api/hds_api_gateway.git'
    }
    stage("Compilations") {
        sh "chmod +x gradlew"
        sh "./gradlew clean bootjar -x test"
    }

    stage("Staging") {
        sh "docker build -t 10.20.101.172:5000/hds_api_gateway_${git_branch}:${BUILD_NUMBER} ."
        sh "docker push 10.20.101.172:5000/hds_api_gateway_${git_branch}"

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
            "https://10.20.101.172/v3/project/c-5n4wx:p-l6bnp/workloads/deployment:api:hds-api-gateway-${git_branch}?action=redeploy" --insecure
            """
        } catch (e) {
            sh 'echo develop deploy Fail!!'
        }
    }
}
