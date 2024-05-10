## help: print this help message
.PHONY: help
help:
	@echo 'Usage:'
	@sed -n 's/^##//p' ${MAKEFILE_LIST} | column -t -s ':' |  sed -e 's/^/ /'

TF_COMMAND := docker run -v $(shell pwd):/app --rm -it hashicorp/terraform -chdir=/app

## tf/setup: refresh the state
tf/setup:
	$(TF_COMMAND) init

## tf/refresh: refresh the state
tf/refresh:
        # fixme: how to inject TF_LOG to container ?
		TF_LOG=ERROR $(TF_COMMAND) refresh \
			-var='twcc_api_key=${TWCC_API_KEY}'

## tf/plan: plan the change
tf/plan: tf//refresh
		TF_LOG=ERROR $(TF_COMMAND) plan \
			-var='twcc_api_key=${TWCC_API_KEY}'

## tf/apply: apply the plan
tf/apply: tf/plan
		TF_LOG=ERROR $(TF_COMMAND) apply -auto-approve
			-var='twcc_api_key=${TWCC_API_KEY}'
				 
## tf/destroy: apply the plan
tf/destroy: tf/refresh
		TF_LOG=ERROR $(TF_COMMAND) destroy
			-var='twcc_api_key=${TWCC_API_KEY}'

## packer/build: build the AMI
packer/build:
			packer build packer.pkr.hcl



