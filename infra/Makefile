help:
	@echo 'Help Message'

ansible/droplet:
	ansible-playbook -i inventory.yml --step --limit droplet playbook.yml

DO_TOKEN?=<DigitalOcean API Token>

terraform/droplet/setup:
	cd digitalocean && TF_LOG=WARN TF_VAR_DO_TOKEN=$(DO_TOKEN) terraform apply && cd ..

.PHONY: ansible/droplet terraform/droplet/setup