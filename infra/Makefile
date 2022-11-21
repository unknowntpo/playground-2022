vm/setup/parallels:
	VAGRANT_VAGRANTFILE=vagrant/Vagrantfile vagrant up --provider=parallels

droplet/setup:
	cd digitalocean && TF_LOG=WARN TF_VAR_DO_TOKEN=$(DO_TOKEN) terraform apply && cd ..
	cd ..
