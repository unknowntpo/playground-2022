# Ref https://cloud.google.com/docs/terraform/get-started-with-terraform#permissions
provider "google" {
  project = "web-service-design"
  credentials = file("~/.gcloud/keys/web-service-design-d0d64b3d1239.json")
  region = "asia-east1"
  zone = "asia-east1a"
}

resource  "google_compute_network" "vpc_network" {
  name = "my-custom-network"
  auto_create_subnetworks = false
  mtu = 1460
} 

resource "google_compute_subnetwork" "default" {
  name          = "my-custom-subnet"
  ip_cidr_range = "10.0.1.0/24"
  region        = "asia-east1"
  network       = google_compute_network.vpc_network.id
}

# Create a single Compute Engine instance
resource "google_compute_instance" "default" {
  name         = "flask-vm"
  machine_type = "f1-micro"
  zone         = "asia-east1-a"
  tags         = ["ssh"]

  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-11"
    }
  }

  # Install Flask
  metadata_startup_script = "sudo apt-get update; sudo apt-get install -yq build-essential python3-pip rsync; pip install flask"

  network_interface {
    subnetwork = google_compute_subnetwork.default.id

    access_config {
      # Include this section to give the VM an external IP address
    }
  }
}
