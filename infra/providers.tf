terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "4.67.0"
    }
    cloudflare = {
      source  = "cloudflare/cloudflare"
      version = "4.5.0"
    }
  }
}

provider "aws" {
  profile = "watchman"
  region  = "us-east-2"
}

provider "cloudflare" {
  email     = var.cloudflare_email
  api_token = var.cloudflare_api_token
}
