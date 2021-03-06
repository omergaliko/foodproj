def userInput

pipeline {

    agent {
        label 'docker-node'
        }
    stages {
        stage('Build Docker image') {
            steps {
                dir('./'){
                    script {
                        sh "sudo chmod 777 Dockerfile"
                        sh "sudo docker build -t foodproj ."
                    }
                }
            }
        }
        stage('Test Docker image') {
            steps {
                dir('./tests') {
                    script{
                       sh "sudo chmod 755 basic.test.sh"
                       sh "sudo ./basic.test.sh"
                    }
                }
            }
        }
        stage('Upload image to repository') {
            steps {
                sh "pwd"
            }
        }
        stage('Deploy to Prod') {
steps {
script {
dir('deployment') {
sh "ansible-playbook -i inventory.ini superhero.yml --extra-vars tag=${latestVersion}-${lastCommit} -vvv"
             }
          }
       }
    }
}
'''
1. make sure docker engine is installed and running
#apt -y install docker.io
#systemctl start docker
#systemctl enable docker

#2. copy the docker image to the target server
#docker save intservice:latest > /var/jenkins_home/intService/intservice.tar    || docker.hub docker push docker.io/intservice/intservice:latest
scp "/var/jenkins_home/intService/intservice.tar ubuntu@${prod_server_ip}:/tmp"

3. run the container
ssh ubuntu@${prod_server_ip} sudo docker load < /tmp/intservice.tar
ssh ubuntu@${prod_server_ip} sudo docker run -d intservice

4. make sure it is executed correctly
./bash.test.sh
'''