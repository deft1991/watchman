#resource "aws_instance" "web-prod" {
#  ami                         = data.aws_ami.amazon_linux.id
#  associate_public_ip_address = true
#  instance_type               = "t2.micro"
#  key_name                    = aws_key_pair.ssh_key.key_name
#  subnet_id                   = module.vpc.public_subnets[0]
#  vpc_security_group_ids      = [aws_security_group.ec2_access.id]
#
#  tags                        = var.tags-prod
#  user_data                   = file("${path.module}/ec2-data/bootstrap.sh")
#  user_data_replace_on_change = true
#
#  # Copy the docker-compose.yml and nginx.conf file to the instance
#  provisioner "file" {
#    source      = "${path.module}/ec2-data/sync/docker-compose.yml"
#    destination = "/home/ec2-user/docker-compose.yml"
#
#    connection {
#      type        = "ssh"
#      user        = "ec2-user"
#      private_key = file("~/.ssh/id_rsa")
#      host        = self.public_ip
#    }
#  }
#  provisioner "file" {
#    source      = "${path.module}/ec2-data/sync/nginx.conf"
#    destination = "/home/ec2-user/nginx.conf"
#
#    connection {
#      type        = "ssh"
#      user        = "ec2-user"
#      private_key = file("~/.ssh/id_rsa")
#      host        = self.public_ip
#    }
#  }
#}
