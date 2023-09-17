node {
    aws_region = "ap-northeast-2"
    aws_s3_bucket =  params.aws_s3_bucket
    aws_ecrRegistry = env.aws_ecrRegistry
    aws_ec2_host = env.aws_ec2_host
    BUILD_ENVIRONMENT = "En9doorweb-1"
    EB_ENV_NAME = "En9doorweb-1"

    SOURCE_CODE_URL = "https://github.com/minssan9/api-gateway.git"

    stage("Parameter Check") {
        echo "JOB_NAME - ${JOB_NAME}"
        echo "scm.branches[0].name - ${scm.branches[0].name}"

        if (ref.contains('refs/heads/') ){
            sh "echo ref.replace - ${ref.replace("refs/heads/", "")}"
            git_branch = ref.replace("refs/heads/", "")
        }else if (scm.branches[0].name != null ){
            sh "echo scm.branches[0].name - ${scm.branches[0].name}"
            git_branch = scm.branches[0].name.replace("*/","")
        }else {
            sh "echo GIT_BRANCH - ${GIT_BRANCH}"
            git_branch = env.GIT_BRANCH.replace("origin/", "")
        }
        echo ("build with this branch : ${git_branch}")

        echo ("GIT_COMMIT : ${GIT_COMMIT} ")
        echo ("GIT_BRANCH : ${GIT_BRANCH} ")
        echo ("GIT_LOCAL_BRANCH : ${GIT_LOCAL_BRANCH} ")
        echo ("GIT_PREVIOUS_COMMIT : ${GIT_PREVIOUS_COMMIT} ")
        echo ("GIT_PREVIOUS_SUCCESSFUL_COMMIT : ${GIT_PREVIOUS_SUCCESSFUL_COMMIT} ")
        echo ("GIT_URL : ${GIT_URL} ")
        echo ("GIT_URL_N : - ${GIT_URL_N} ")
        echo ("GIT_AUTHOR_NAME : ${GIT_AUTHOR_NAME} ")
        echo ("GIT_COMMITTER_EMAIL : ${GIT_COMMITTER_EMAIL} ")
    }

    stage ('Clone'){
        def gitVars = git branch: "${git_branch}", url: "${SOURCE_CODE_URL}"
        // gitVars will contain the following keys: GIT_BRANCH, GIT_COMMIT, GIT_LOCAL_BRANCH, GIT_PREVIOUS_COMMIT, GIT_PREVIOUS_SUCCESSFUL_COMMIT, GIT_URL
        println gitVars
        gitCommitId = gitVars.GIT_PREVIOUS_SUCCESSFUL_COMMIT
        println "Previous successful commit is : ${gitCommitId}"
        echo ("Previous successful commit is : ${gitCommitId}")

        echo ("clone : ${git_branch}")
        git branch: "${git_branch}", credentialsId: 'githu_ssh_minssan9', url: 'https://github.com/minssan9/api-gateway.git'
    }

    if (git_branch != "develop"){
        git_branch = "release"
    }

    stage("Build Jar"){
        sh "chmod +x gradlew"
        sh "./gradlew :clean :${job_type}:build -x test --no-daemon --parallel --continue > /dev/null 2>&1 || true"
    }

    stage("Build Docker Image") {
        ecrRepository = JOB_NAME

        echo ("${aws_ecrRegistry}/${ecrRepository} --platform=linux/arm64  -f Dockerfile .")  //  --build-arg PROFILES=${git_branch}
        appImage = docker.build("${aws_ecrRegistry}/${ecrRepository}", "--platform=linux/arm64  -f Dockerfile . " )
//        docker build -t ${aws_ecrRegistry}/en9door_web_develop --build-arg PROFILES=develop ./
//        docker build -t ${aws_ecrRegistry}/en9door_web_release --build-arg PROFILES=release ./
//           docker build -t 293001004573.dkr.ecr.ap-northeast-2.amazonaws.com/en9door_web_develop -f docker/Dockerfile --build-arg PROFILES=develop ./
//          docker build -t 293001004573.dkr.ecr.ap-northeast-2.amazonaws.com/en9door_web_release -f docker/Dockerfile  --build-arg PROFILES=release ./
//       docker build --platform=linux/arm64  -t 293001004573.dkr.ecr.ap-northeast-2.amazonaws.com/en9door_web_develop:arm64 -f docker/Dockerfile  --build-arg PROFILES=develop ./
//        docker build --platform=linux/arm64  -t 293001004573.dkr.ecr.ap-northeast-2.amazonaws.com/en9door_web_release -f docker/Dockerfile  --build-arg PROFILES=release ./
//        docker build --platform=linux/arm64  -t 293001004573.dkr.ecr.ap-northeast-2.amazonaws.com/en9door_web_develop -f docker/Dockerfile  --build-arg PROFILES=develop ./

    }

    stage("Push Docker Image") {         // login to ECR - for now it seems that that the ECR Jenkins plugin is not performing the login as expected. I hope it will in the future.
        docker.withRegistry("https://${aws_ecrRegistry}", "ecr:ap-northeast-2:aws_en9door_credential"){
            appImage.push("${env.BUILD_NUMBER}")
            appImage.push("latest")
        }
//          aws ecr get-login-password --region ap-northeast-2 | docker login --username AWS --password-stdin 293001004573.dkr.ecr.ap-northeast-2.amazonaws.com
//          docker push 293001004573.dkr.ecr.ap-northeast-2.amazonaws.com/en9door_web
//          docker pull 293001004573.dkr.ecr.ap-northeast-2.amazonaws.com/en9door_web

    }

    stage('deploy in develop env') {
        if (git_branch == "develop") {
            echo "deploy in develop env"
            sh "docker rm -f ${ecrRepository}"       //        docker rm -f en9door_api
            appImage.run("--name ${ecrRepository} -it -d --restart=always -p  31000:31000 -v /var/log:/var/log -e SPRING_PROFILES_ACTIVE=${git_branch}")
//             sh "docker image prune -a -f"
//      sh "docker run -it -d -p 34001:34001 -v /var/log:/var/log -e SPRING_PROFILES_ACTIVE=${git_branch} -h ${env.JOB_NAME} --name ${env.JOB_NAME}_${git_branch} minssan9/${env.JOB_NAME}_${git_branch}:${BUILD_NUMBER} "
//      docker run -it -d --restart=always -p 34001:34001 -v d:/docker/log/en9door_api:/var/log --memory="2g" -e SPRING_PROFILES_ACTIVE=develop --name en9door_api 293001004573.dkr.ecr.ap-northeast-2.amazonaws.com/en9door_api
//      docker run --name ${ecrRepository} -it -d -p 34001:34001  -v /var/log:/var/log -e SPRING_PROFILES_ACTIVE=${git_branch}  ${ecrRegistry}/${ecrRepository}
//         docker run --name en9door_api_develop -it -d --restart=always -p 34001:34001  -v d:/docker/log/en9door_api:/var/log -e SPRING_PROFILES_ACTIVE=develop 293001004573.dkr.ecr.ap-northeast-2.amazonaws.com/en9door_api_develop
//         docker run --name en9door_api_release -it -d --restart=always -p 34001:34001 -m 1900m -v /var/log:/var/log -e SPRING_PROFILES_ACTIVE=release 293001004573.dkr.ecr.ap-northeast-2.amazonaws.com/en9door_api_release

        }

    }

    withCredentials([sshUserPrivateKey(credentialsId: 'en9door_aws_ssh', keyFileVariable: 'identity', passphraseVariable: 'passphrase', usernameVariable: 'userName')]) {
            def remote = [:]
            remote.name = 'en9door_ec2'
            remote.host = aws_ec2_host
            remote.allowAnyHosts = true
            remote.user = userName
            remote.identityFile = identity   //remote.passphrase = passphrase


            stage('deploy in release env') {
                if (git_branch == "release") {
                echo "deploy in release env"
                    sshCommand remote: remote, command: "aws ecr get-login-password --region ${aws_region} | sudo docker login --username AWS --password-stdin ${aws_ecrRegistry}"
                    sshCommand remote: remote, command: "sudo docker pull ${aws_ecrRegistry}/${ecrRepository}:latest"
                    sshCommand remote: remote, command: "sudo docker stop ${ecrRepository}"
                    sshCommand remote: remote, command: "sudo docker rm -f ${ecrRepository}"
                    sshCommand remote: remote, command: "sudo docker run --name ${env.JOB_NAME} -it -d --restart=always -p 31000:31000 -m 1900m -v /var/log:/var/log -e SPRING_PROFILES_ACTIVE=${git_branch} -h ${env.JOB_NAME}  ${aws_ecrRegistry}/${ecrRepository}:latest"
            }
        }
    }
}