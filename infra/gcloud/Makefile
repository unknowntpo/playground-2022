## help: print this help message
.PHONY: help
help:
	@echo 'Usage:'
	@sed -n 's/^##//p' ${MAKEFILE_LIST} | column -t -s ':' |  sed -e 's/^/ /'

## tf/gcloud/refresh: refresh the state
tf/gcloud/refresh:
		TF_LOG=ERROR terraform refresh

## tf/gcloud/plan: plan the change
tf/gcloud/plan: tf/gcloud/refresh
		TF_LOG=ERROR terraform plan

## tf/gcloud/apply: apply the plan
tf/gcloud/apply: tf/gcloud/plan
		TF_LOG=ERROR terraform apply -auto-approve
					 
## tf/gcloud/destroy: apply the plan
tf/gcloud/destroy: tf/gcloud/refresh
		TF_LOG=ERROR terraform destroy

## packer/gcloud/build: build the AMI
packer/gcloud/build:
			packer build packer.pkr.hcl



