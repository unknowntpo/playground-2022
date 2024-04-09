#!/bin/sh

./scripts/pulsar/prepare_helm_release.sh -n pulsar \
	-k pulsar-mini \
	-c

# ADMIN_IP=<pulsar-manager-admin-ip>
ADMIN_IP=192.168.194.197

CSRF_TOKEN=$(curl http://$ADMIN_IP:7750/pulsar-manager/csrf-token)
curl \
	-H 'X-XSRF-TOKEN: $CSRF_TOKEN' \
	-H 'Cookie: XSRF-TOKEN=$CSRF_TOKEN;' \
	-H "Content-Type: application/json" \
	-X PUT http://$ADMIN_IP:7750/pulsar-manager/users/superuser \
	-d '{"name": "admin", "password": "apachepulsar", "description": "test", "email": "username@test.org"}'
