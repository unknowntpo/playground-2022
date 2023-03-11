
variable "aws_region" {
  # Tokyo
  default = "ap-northest-1"
}

variable "aws_source_ami" {
  default = "ami-0c55b159cbfafe1f0"
}

packer {
  required_plugins {
    amazon = {
      version = ">= 0.0.2"
      source  = "github.com/hashicorp/amazon"
    }
  }
}

source "amazon-ebs" "ubuntu" {
  ami_name_regex = "ubuntu/images/hvm-ssd/ubuntu-focal-20.04-amd64-server-*"
  instance_type  = "t2.micro"
  region         = var.aws_region
  source_ami     = var.aws_source_ami
  ssh_username   = "ubuntu"
}

build {
  sources = [
    "source.amazon-ebs.ubuntu"
  ]

  provisioner "shell" {
    inline = [
      "sudo apt-get update",
      "sudo apt-get install -y redis-server"
    ]
  }

  post-processors {
    aws {
      region              = var.aws_region
      access_key          = var.aws_access_key
      secret_key          = var.aws_secret_key
      ami_name            = "redis-server {{timestamp}}"
      ami_description     = "Redis server"
      launch_block_device_mappings {
        device_name = "/dev/xvda"
        delete_on_termination = true
        volume_size = 8
        volume_type = "gp2"
      }
    }
  }
}