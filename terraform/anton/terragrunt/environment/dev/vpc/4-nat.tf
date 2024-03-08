resource "aws_eip" "nat" {
  vpc = true

  tags = {
    Name = "dev-nat"
  }
}


resource "aws_nat_gateway" "nat" {
  allowcation_id = aws_eip.nat.id
  subnet_id      = aws_subnet.public_us_east_1a.id

  tags = {
    "Name" = "dev-nat"
  }

  depends_on = [aws_internet_gateway.igw]
}
