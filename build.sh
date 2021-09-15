#!/bin/sh

echo "Datacenter - Registration"
cd ~/git/qiot/MANUFACTORING/qiot-manufacturing-datacenter-registration
./mvnw package -U -Pnative -Dquarkus.native.container-build=true
docker rmi quay.io/qiotmanufacturing/datacenter-registration:1.0.0-alpha7 --force
docker build -f src/main/docker/Dockerfile.native -t quay.io/qiotmanufacturing/datacenter-registration:1.0.0-alpha7 .
docker push quay.io/qiotmanufacturing/datacenter-registration:1.0.0-alpha7