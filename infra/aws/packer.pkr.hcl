
variable "aws_region" {
  # Tokyo
  default = "ap-northeast-1"
}

variable "aws_source_ami" {
  # Ref:
  # Amazon EC2 AMI Locator
  # https://cloud-images.ubuntu.com/locator/ec2/
  default = "ami-00fc5946ba8411dd2"
}

variable "aws_access_key" {
  type = string
}

variable "aws_secret_key" {
  type = string
}

packer {
  required_plugins {
    amazon = {
      version = ">= 0.0.2"
      source  = "github.com/hashicorp/amazon"
    }
  }
}

source "amazon-ebs" "ubuntu-bpftrace-k3s" {
  access_key    = var.aws_access_key
  secret_key    = var.aws_secret_key
  region        = var.aws_region
  source_ami    = var.aws_source_ami
  instance_type = "t2.micro"
  ssh_username  = "ubuntu"
  ami_name      = "k3s-bpftrace-{{timestamp}}"
}

build {
  sources = [
    "source.amazon-ebs.ubuntu-bpftrace-k3s"
  ]

  provisioner "shell" {
    inline = [
      "sudo apt-get update -y",
      "curl -sfL https://get.k3s.io | sh -",
      "sudo apt install -y bpftrace"
    ]
  }
}