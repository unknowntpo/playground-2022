vm/setup/parallels:
	VAGRANT_VAGRANTFILE=vagrant/Vagrantfile vagrant up --provider=parallels

droplet/refresh:
	cd digitalocean && TF_LOG=WARN TF_VAR_DO_TOKEN=$(DO_TOKEN) terraform refresh && cd ..

droplet/plan: droplet/refresh
	cd digitalocean && TF_LOG=WARN TF_VAR_DO_TOKEN=$(DO_TOKEN) terraform plan && cd ..

droplet/setup: droplet/plan
	cd digitalocean && TF_LOG=WARN TF_VAR_DO_TOKEN=$(DO_TOKEN) terraform apply && cd ..
