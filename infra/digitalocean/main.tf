# https://awstip.com/how-to-create-digitalocean-droplet-using-terraform-a-z-guide-df91716f6021
terraform {
  required_version = ">= 1.0.0"

  required_providers {
    digitalocean = {
      source  = "digitalocean/digitalocean"
      version = "~> 2.0"
    }
  }
}

variable "DO_TOKEN" {}

locals {
  // Map of pre-named sizes to look up from
  sizes = {
    nano      = "s-1vcpu-1gb"
    micro     = "s-2vcpu-2gb"
    small     = "s-2vcpu-4gb"
    medium    = "s-4vcpu-8gb"
    large     = "s-6vcpu-16gb"
    x-large   = "s-8vcpu-32gb"
    xx-large  = "s-16vcpu-64gb"
    xxx-large = "s-24vcpu-128gb"
    maximum   = "s-32vcpu-192gb"
  }
  // Map of regions
  regions = {
    new_york_1    = "nyc1"
    new_york_3    = "nyc3"
    san_francisco = "sfo3"
    amsterdam     = "ams3"
    singapore     = "sgp1"
    london        = "lon1"
    frankfurt     = "fra1"
    toronto       = "tor1"
    india         = "blr1"
  }
}

provider "digitalocean" {
  token = var.DO_TOKEN
}

# https://nickolasfisher.com/blog/How-to-Create-a-Digital-Ocean-Droplet-using-Terraform
data "digitalocean_ssh_key" "my_ssh_key" {
  name = "MacBook"
}

data "digitalocean_ssh_keys" "keys" {
  sort {
    key       = "name"
    direction = "asc"
  }
}

resource "digitalocean_droplet" "droplets" {
  count    = 3
  image    = "ubuntu-20-04-x64"
  name     = "kube${count.index}"
  region   = local.regions.singapore
  size     = local.sizes.nano
  tags     = [digitalocean_tag.kube.id]
  ssh_keys = [data.digitalocean_ssh_key.my_ssh_key.id]
  #   user_data = <<EOF
  # #cloud-config
  # groups:
  #   - ubuntu: [root,sys]
  # # Add users to the system. Users are added after groups are added.
  # users:
  #   - default
  #   - name: kube
  #     gecos: kube
  #     shell: /bin/bash
  #     primary_group: kube
  #     sudo: ALL=(ALL) NOPASSWD:ALL
  #     groups: users, admin, docker
  #     lock_passwd: false
  #     ssh_authorized_keys:
  #       - ssh-rsa 

  # runcmd:
  #   - sudo apt-get -y update
  #   - sudo apt -y install apt-transport-https ca-certificates curl software-properties-common net-tools
  #   - curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
  #   - sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu focal stable"
  #   - sudo apt -y update
  #   - sudo apt-cache policy docker-ce && apt-get -y install docker-ce
  #   - sudo usermod -aG docker lesha
  # EOF
}

# Create a new tag
resource "digitalocean_tag" "kube" {
  name = "kube"
}
