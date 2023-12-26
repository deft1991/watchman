resource "aws_instance" "web" {
  ami                         = data.aws_ami.amazon_linux.id
  associate_public_ip_address = true
  instance_type               = "t2.micro"
  key_name                    = aws_key_pair.ssh_key.key_name
  subnet_id                   = module.vpc.public_subnets[0]
  vpc_security_group_ids      = [aws_security_group.ec2_access.id]

  tags                        = var.tags
  user_data                   = file("${path.module}/ec2-data/bootstrap.sh")
  user_data_replace_on_change = true

  # Copy the docker-compose.yaml and nginx.conf file to the instance
  provisioner "file" {
    source      = "${path.module}/ec2-data/sync/docker-compose.yaml"
    destination = "/home/ec2-user/docker-compose.yaml"

    connection {
      type        = "ssh"
      user        = "ec2-user"
      private_key = file("~/.ssh/id_rsa")
      host        = self.public_ip
    }
  }
  provisioner "file" {
    source      = "${path.module}/ec2-data/sync/proxy/nginx.conf"
    destination = "/home/ec2-user/nginx.conf"

    connection {
      type        = "ssh"
      user        = "ec2-user"
      private_key = file("~/.ssh/id_rsa")
      host        = self.public_ip
    }
  }
}

# Supporting Resources
data "aws_ami" "amazon_linux" {
  most_recent = true
  owners      = ["amazon"]

  filter {
    name   = "name"
    values = ["amzn-ami-hvm-*-x86_64-gp2"]
  }
}

module "vpc" {
  source  = "terraform-aws-modules/vpc/aws"
  version = "~> 4.0"

  name = var.stack_name
  cidr = var.vpc_cidr

  azs             = var.azs
  private_subnets = [for k, v in var.azs : cidrsubnet(var.vpc_cidr, 4, k)]
  public_subnets  = [for k, v in var.azs : cidrsubnet(var.vpc_cidr, 8, k + 48)]

  tags = var.tags
}

resource "aws_security_group" "ec2_access" {
  name        = "ec2_access"
  description = "Allow inbound traffic to ec2"
  vpc_id      = module.vpc.vpc_id

  ingress {
    description = "HTTP from anywhere"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  ingress {
    description = "SSH from anywhere"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port        = 0
    to_port          = 0
    protocol         = "-1"
    cidr_blocks      = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
  }

  tags = var.tags
}

resource "aws_key_pair" "ssh_key" {
  key_name   = "my_tf_key"
  public_key = file("~/.ssh/id_rsa.pub")
}
