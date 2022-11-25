packer {
  required_plugins {
    docker = {
      version = ">= 0.0.7"
      source = "github.com/hashicorp/docker"
    }
  }
}

source "docker" "ubuntu" {
  image  = "ubuntu:xenial"
  commit = true
}

build {
  name    = "docker-example"
  sources = [
    "source.docker.ubuntu"
  ]
  provisioner "shell" {
    environment_vars = [
      "FOO=hello world",
    ]
    inline = [
      "echo \"FOO is $FOO\" > example.txt",
    ]
  }

   post-processors {
    post-processor "docker-tag" {
        repository =  "unknowntpo/docker-example"
        tag = ["0.6"]
      }
    // post-processor "docker-push" {}
  }
}