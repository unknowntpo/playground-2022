all:

ansible/droplet:
	ansible-playbook -i inventory.yml --step --limit droplet playbook.yml

terraform/droplet/setup:
       cd digitalocean && TF_LOG=WARN TF_VAR_DO_TOKEN=$(DO_TOKEN) terraform apply && cd ..