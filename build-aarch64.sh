#!/bin/sh

docker login quay.io -u ${QUAY_ALL_USERNAME} -p ${QUAY_ALL_PASSWORD}
PV=$(mvn org.apache.maven.plugins:maven-help-plugin:3.2.0:evaluate -Dexpression=project.version -q -DforceStdout)-aarch64
docker run --rm --privileged multiarch/qemu-user-static:register --reset
docker rmi quay.io/qiotproject/all-registration:$PV --force
docker build -t quay.io/qiotproject/all-registration:$PV -f src/main/docker/Dockerfile.native.multiarch .
docker push quay.io/qiotproject/all-registration:$PV