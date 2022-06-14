node {
    stage("Parameter Check") {
        echo "Start"
        echo "env.JOB_NAME - ${env.JOB_NAME}"
        echo "env.gitlabBranch - ${env.gitlabBranch}"
        echo "env.gitlabSourceBranch - ${env.gitlabSourceBranch}"
        echo "params.GIT_BRANCH - ${params.GIT_BRANCH}"

        JOB_NAME_SHORT = env.JOB_NAME.split('/')[1].toLowerCase()
        JOB_NAME = JOB_NAME.toLowerCase()
        echo "JOB_NAME - ${JOB_NAME}"

        if (env.ref.contains('refs/heads/') ){
            git_branch = "${ref.replace("refs/heads/", "")}"
        }else if (env.gitlabBranch != null){
            git_branch = "${env.gitlabBranch.replace("origin/", "")}"
        }else{
            git_branch = "${params.GIT_BRANCH.replace("origin/", "")}"
        }

        if (git_branch.contains('release')) {
            spring_active_profile = 'release'
        }else if ( git_branch.contains('develop')){
            spring_active_profile = 'develop'
        }
        RANCHER_DEPLOY_NAME = JOB_NAME_SHORT  + '-' + spring_active_profile
        echo "build with this branch : ${git_branch}"
    }
    stage ("Clone"){
        git branch: "${git_branch}", credentialsId: "gitlab_deploy", url: "http://10.20.101.173/hds_api/${JOB_NAME_SHORT}.git"
    }

    stage("Build Image"){
        docker_image = JOB_NAME + '-' + spring_active_profile
        sh "docker build -t 10.20.101.172:5000/${docker_image} --build-arg SPRING_PROFILES_ACTIVE=${spring_active_profile} -f docker-api.Dockerfile ."
        sh "docker push 10.20.101.172:5000/${docker_image}"
        sh "docker rmi 10.20.101.172:5000/${docker_image}"
    }


    stage("Staging") {
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

        try {
            // release redeploy
            sh """
            curl -u "${HOMS_CATTLE_ACCESS_KEY}:${HOMS_CATTLE_SECRET_KEY}" \
            -X POST \
            -H "Accept: application/json" \
            -H "Content-Type: application/json" \
            "https://10.20.101.172/v3/project/c-5n4wx:p-l6bnp/workloads/daemonset:api:hds-api-gateway-${git_branch}?action=redeploy" --insecure
            """
        } catch (e) {
            sh "echo develop deploy Fail!!"
        }
    }
}
