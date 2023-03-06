# Set the AWS provider and specify the access key and secret.
provider "aws" {
  access_key = var.aws_access_key
  secret_key = var.aws_secret_key
  region     = "us-west-2"
}

# Define the EC2 instance resource.
resource "aws_instance" "example" {
  ami           = "ami-0c55b159cbfafe1f0" # Specify the ID of the Amazon Machine Image you want to use.
  instance_type = "t2.micro"              # Specify the instance type.

  # Define the connection information to connect to the EC2 instance.
  connection {
    type        = "ssh"
    user        = "ubuntu"
    private_key = file("~/.ssh/my_key.pem") # Specify the path to your SSH private key.
    host        = self.public_ip
  }

  # Add a tag to the EC2 instance.
  tags = {
    Name = "example-instance"
  }
}

# Define the Route 53 record.
resource "aws_route53_record" "unknowntpo.net" {
  name    = "unknowntpo.net"
  type    = "A"
  zone_id = "Z1EXAMPLEZONEID" # Specify the ID of your Route 53 hosted zone.

  # Specify the EC2 instance IP address as the record value.
  records = [aws_instance.example.public_ip]

  # Set a TTL of 300 seconds.
  ttl = 300
}
