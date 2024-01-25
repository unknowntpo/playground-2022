## help: print this help message
.PHONY: help
help:
	@echo 'Usage:'
	@sed -n 's/^##//p' ${MAKEFILE_LIST} | column -t -s ':' |  sed -e 's/^/ /'

## ansible/droplet: use ansible to create droplet
ansible/droplet:
	ansible-playbook -i inventory.yml --step --limit droplet playbook.yml

DO_TOKEN?=<DigitalOcean API Token>

## tf/droplet/refresh: use terraform to refresh the state of droplets
tf/droplet/refresh:
	cd digitalocean && TF_LOG=WARN TF_VAR_DO_TOKEN=$(DO_TOKEN) terraform refresh && cd ..

## tf/droplet/plan: use terraform to plan the action on droplets
tf/droplet/plan: droplet/refresh
	cd digitalocean && TF_LOG=WARN TF_VAR_DO_TOKEN=$(DO_TOKEN) terraform plan && cd ..

## tf/droplet/apply: use terraform to apply the action on droplets
tf/droplet/apply: droplet/plan
	cd digitalocean && TF_LOG=WARN TF_VAR_DO_TOKEN=$(DO_TOKEN) terraform apply && cd ..

.PHONY: ansible/droplet tf/droplet/refresh tf/droplet/plan tf/droplet/apply