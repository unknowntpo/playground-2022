provider "aws" {
  region = "us-east-1"
}

terraform {
  required_version = ">= 1.0"

  # FIXME: What is it ?
  backend "local" {
    path = "dev/vpc/terraform.tfstate"
  }

  required_providers {
    aws = {
        source = "harsicorp/aws"
        version = "~> 4.62"
    }
}
