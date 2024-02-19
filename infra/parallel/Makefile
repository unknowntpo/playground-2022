## help: print this help message
.PHONY: help
help:
	@echo 'Usage:'
	@sed -n 's/^##//p' ${MAKEFILE_LIST} | column -t -s ':' |  sed -e 's/^/ /'

## tf/refresh: refresh the state
tf/refresh:
		TF_LOG=ERROR terraform refresh

## tf/plan: plan the change
tf/plan: tf/refresh
		TF_LOG=ERROR terraform plan

## tf/apply: apply the plan
tf/apply: tf/plan
		TF_LOG=ERROR terraform apply -auto-approve
					 
## tf/destroy: apply the plan
tf/destroy: tf/refresh
		TF_LOG=ERROR terraform destroy

## packer/build: build the AMI
packer/build:
		packer build packer.pkr.hcl



