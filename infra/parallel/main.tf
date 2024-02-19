
# https://registry.terraform.io/providers/Parallels/parallels-desktop/latest/docs/data-sources/vm
data "parallels-desktop_vm" "example" {
  host = "http://example.com:8080"

  filter {
    field_name = "name"
    value      = "exampe-vm"
  }
}
