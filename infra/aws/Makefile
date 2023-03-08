## help: print this help message
.PHONY: help
help:
	@echo 'Usage:'
	@sed -n 's/^##//p' ${MAKEFILE_LIST} | column -t -s ':' |  sed -e 's/^/ /'

## tf/aws/refresh: refresh the state
tf/aws/refresh:
		TF_LOG=WARN terraform refresh \
			-var='aws_access_key=${AWS_ACCESS_KEY}' \
			-var='aws_secret_key=${AWS_SECRET_KEY}'
        # cd digitalocean && TF_LOG=WARN TF_VAR_DO_TOKEN=$(DO_TOKEN) terraform refresh && cd ..

## tf/aws/plan: plan the change
tf/aws/plan: tf/aws/refresh
		TF_LOG=WARN terraform plan \
			-var='aws_access_key=${AWS_ACCESS_KEY}' \
			-var='aws_secret_key=${AWS_SECRET_KEY}'

## tf/aws/apply: apply the plan
tf/aws/apply: tf/aws/plan
		TF_LOG=WARN terraform apply \
			-var='aws_access_key=${AWS_ACCESS_KEY}' \
			-var='aws_secret_key=${AWS_SECRET_KEY}'

## tf/aws/destroy: apply the plan
tf/aws/destroy: tf/aws/refresh
		TF_LOG=WARN terraform destroy \
			-var='aws_access_key=${AWS_ACCESS_KEY}' \
			-var='aws_secret_key=${AWS_SECRET_KEY}'
