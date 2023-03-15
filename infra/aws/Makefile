## help: print this help message
.PHONY: help
help:
	@echo 'Usage:'
	@sed -n 's/^##//p' ${MAKEFILE_LIST} | column -t -s ':' |  sed -e 's/^/ /'

## tf/aws/refresh: refresh the state
tf/aws/refresh:
		TF_LOG=ERROR terraform refresh \
			-var='aws_access_key=${AWS_ACCESS_KEY}' \
			-var='aws_secret_key=${AWS_SECRET_KEY}' \
			-var='ami-id=${AMI_ID}'

## tf/aws/plan: plan the change
tf/aws/plan: tf/aws/refresh
		TF_LOG=ERROR terraform plan \
			-var='aws_access_key=${AWS_ACCESS_KEY}' \
			-var='aws_secret_key=${AWS_SECRET_KEY}' \
			-var='ami-id=${AMI_ID}'


## tf/aws/apply: apply the plan
tf/aws/apply: tf/aws/plan
		TF_LOG=ERROR terraform apply -auto-approve \
			-var='aws_access_key=${AWS_ACCESS_KEY}' \
			-var='aws_secret_key=${AWS_SECRET_KEY}' \
			-var='ami-id=${AMI_ID}'

## tf/aws/destroy: apply the plan
tf/aws/destroy: tf/aws/refresh
		TF_LOG=ERROR terraform destroy \
			-var='aws_access_key=${AWS_ACCESS_KEY}' \
			-var='aws_secret_key=${AWS_SECRET_KEY}'

## packer/aws/build: build the AMI
packer/aws/build:
			packer build \
			-var='aws_access_key=${AWS_ACCESS_KEY}' \
			-var='aws_secret_key=${AWS_SECRET_KEY}' \
			packer.pkr.hcl



