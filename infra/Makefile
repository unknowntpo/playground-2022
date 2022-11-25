help:
	@echo 'Help Message'

ansible/droplet:
	ansible-playbook -i inventory.yml --step --limit droplet playbook.yml

DO_TOKEN?=<DigitalOcean API Token>

tf/droplet/refresh:
	cd digitalocean && TF_LOG=WARN TF_VAR_DO_TOKEN=$(DO_TOKEN) terraform refresh && cd ..

tf/droplet/plan: droplet/refresh
	cd digitalocean && TF_LOG=WARN TF_VAR_DO_TOKEN=$(DO_TOKEN) terraform plan && cd ..

tf/droplet/apply: droplet/plan
	cd digitalocean && TF_LOG=WARN TF_VAR_DO_TOKEN=$(DO_TOKEN) terraform apply && cd ..

.PHONY: ansible/droplet tf/droplet/refresh tf/droplet/plan tf/droplet/apply