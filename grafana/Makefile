# check installation of githooks and display help message when typing make
all: help

#Ref: https://medium.com/aeturnuminc/configure-prometheus-and-grafana-in-dockers-ff2a2b51aa1d

# ==================================================================================== #
# HELPERS
# ==================================================================================== #

## help: print this help message
.PHONY: help
help:
	@echo 'Usage:'
	@sed -n 's/^##//p' ${MAKEFILE_LIST} | column -t -s ':' |  sed -e 's/^/ /'

## run: run prometheus and grafana
run: run/prometheus run/grafana

## run/prometheus: run prometheus
run/prometheus: 
	docker run -d --name prometheus -p 9090:9090 \
	-v $(pwd):/etc/prometheus \
	prom/prometheus \
	--config.file=/etc/prometheus/prometheus.yml

## run/grafana: run grafana
run/grafana:
	docker run -d --name grafana -p 3000:3000 grafana/grafana

## run/client: run example go client
run/client:
	cd ./example/go/ && go run main.go

## clean: clean up everything
clean:
	docker rm -f grafana prometheus
