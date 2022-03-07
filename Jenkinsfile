node {
    stage("Parameter Check") {
        echo "Start"
        echo "env.JOB_NAME - ${env.JOB_NAME}"
        echo "env.gitlabBranch - ${env.gitlabBranch}"
        echo "env.gitlabSourceBranch - ${env.gitlabSourceBranch}"
        echo "params.GIT_BRANCH - ${params.GIT_BRANCH}"

        git_branch = "${env.gitlabBranch.replace("origin/", "")}"
        custom_job_name = "hds_api_gateway"

        echo "git_branch - ${git_branch}"
        echo "job_name - ${job_name}"
        echo "custom_job_name - ${custom_job_name}"
    }
    stage ("Clone"){
        git branch: "${git_branch}", credentialsId: "gitlab_deploy", url: "http://10.20.101.172:8111/hds_api/${custom_job_name}.git"
    }
    stage("Compilations") {
        sh "chmod +x gradlew"
        sh "./gradlew clean bootjar -x test"
    }

    stage("Staging") {
        echo "docker build -t 10.20.101.172:5000/${custom_job_name}_${git_branch} . "   // :${BUILD_NUMBER}
        sh "docker build -t 10.20.101.172:5000/${custom_job_name}_${git_branch} ."
        sh "docker push 10.20.101.172:5000/${custom_job_name}_${git_branch}"
        sh "docker rmi 10.20.101.172:5000/${custom_job_name}_${git_branch}"

        if (git_branch == "develop"){
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
                sh "echo develop deploy Fail!!"
            }
        }

        if (git_branch == "release"){
            try {
                // release redeploy
                sh """
                curl -u "${HOMS_CATTLE_ACCESS_KEY}:${HOMS_CATTLE_SECRET_KEY}" \
                -X POST \
                -H "Accept: application/json" \
                -H "Content-Type: application/json" \
                "https://10.20.101.172/v3/project/c-4lp87:p-n5wcg/workloads/daemonset:glow:gateway?action=redeploy" --insecure
                """
            } catch (e) {
                sh "echo develop deploy Fail!!"
            }
        }
    }
}
