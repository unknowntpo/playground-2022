


// Ref: https://registry.terraform.io/providers/RickFongGemini/twcc/latest/docs/resources/container

terraform {
  required_providers {
    twcc = {
      source  = "RickFongGemini/twcc"
      version = "0.1.2"
    }
  }
}

variable "twcc_api_key" {
  type = string
}


provider "twcc" {
  # Configuration options
  apikey    = var.twcc_api_key
  apigw_url = "https://apigateway.tmpstg.twcc.tw/"
}

data "twcc_project" "testProject" {
  name     = "ENT901289"
  platform = "k8s-taichung-default"
}

data "twcc_solution" "testSolution" {
  name     = "Tensorflow"
  project  = data.twcc_project.testProject.id
  category = "container"
}

resource "twcc_container" "container1" {
  extra_property = {
    flavor            = "1 GPU + 04 cores + 090GB memory"
    gpfs01-mount-path = "/mnt"
    gpfs02-mount-path = "/tmp"
    image             = "tensorflow-19.08-py3:latest"
    replica           = "1"
  }

  name     = "lawsnoteTestContainer"
  platform = data.twcc_project.testProject.platform
  project  = data.twcc_project.testProject.id
  solution = data.twcc_solution.testSolution.id
}


