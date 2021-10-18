# qiot-manufacturing-datacenter-registration

![Build](https://github.com/qiot-project/qiot-ubi-all-registration/actions/workflows/ci.yml/badge.svg)
![PR](https://github.com/qiot-project/qiot-ubi-all-registration/actions/workflows/feature.yml/badge.svg?event=pull_request)



## Build & Push

1. Local Build and Push
    `sh build.sh`
2. Trigger Github action.

## Install on OpenShift Cluster

1. Add redhat-charts repo
    `helm repo add redhat-charts https://redhat-developer.github.io/redhat-helm-charts`
2. Update the helm repo
    `helm repo update`
3. Give the right to administrate the namespace
    `oc adm policy add-role-to-user admin -z default -n manufacturing-dev`
3. Install the Registration Service
    `helm upgrade --install registration-service redhat-charts/quarkus -f cd/dev/values.yaml -n manufacturing-dev`