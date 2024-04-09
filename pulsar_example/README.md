Follow local cluster

# https://github.com/apache/pulsar-helm-chart/blob/master/examples/values-local-cluster.yaml

```
$ helm repo add apache https://pulsar.apache.org/charts

$ helm install pulsar apache/pulsar -f pulsar.yaml
```

Steps:

- Clone pulsar-helm-chart 
- execute start.sh


# Docker Compose:

Prepare data dir:

```
sudo mkdir -p ./data/zookeeper ./data/bookkeeper
# this step might not be necessary on other than Linux platforms
sudo chown -R 10000 data

$ docker compose up -d
```

Ref: https://pulsar.apache.org/docs/next/getting-started-docker-compose/

