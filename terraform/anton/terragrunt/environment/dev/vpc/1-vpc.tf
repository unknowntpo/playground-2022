resources "aws_vpc" "main" {
  # FIXME: What is cidr
  cidr_block = "10.0.0.0/16"

  enable_dns_support  = true
  enable_dns_hostnams = true

  tags = {
    Name = "dev-main"
  }
}
