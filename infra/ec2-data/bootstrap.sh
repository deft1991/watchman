#!/bin/bash
# This file will be executed on the EC2 instance as a bootstrap script. Only first init.

yum update -y
# Install docker and docker-compose to ami linux
yum install -y docker
curl -SL https://github.com/docker/compose/releases/latest/download/docker-compose-linux-x86_64 -o /usr/bin/docker-compose
chmod +x /usr/bin/docker-compose
service docker start
chkconfig docker on

# Add a new user for rerun docker apps from CI/CD
useradd -m cicd-user
# Generate ssh key for cicd-user
su - cicd-user -c "ssh-keygen -t rsa -N '' -f ~/.ssh/id_rsa"

#copy files
cp /home/ec2-user/docker-compose.yml /home/cicd-user
cp /home/ec2-user/nginx.conf /home/cicd-user

# copy public key to authorized_keys
cat /home/cicd-user/.ssh/id_rsa.pub > /home/cicd-user/.ssh/authorized_keys

# change directory rights
chown cicd-user:cicd-user -R /home/cicd-user



usermod -a -G docker cicd-user
# Add cicd-user to ec2-user group to change dir to /home/ec2-user
usermod -a -G ec2-user cicd-user
# Allow any user in the group of ec2-user enter to the dir /home/ec2-user
chmod 750 /home/ec2-user
chmod 600 /home/cicd-user/.ssh/authorized_keys
usermod -a -G docker ec2-user
cd /home/ec2-user || exit 1
docker-compose up -d
