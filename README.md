# Local development

## Minikube

Start minikube

```shell
minikube start --cpus=8 --memory=10g
minikube addons enable ingress
```

## Setup CDRs

```shell
mvn clean package -DskipTests
kubectl apply -f target/kubernetes/windups.windup.jboss.org-v1.yml
```

## Start server in dev mode

```shell
mvn compile quarkus:dev
```

## Instantiate Windup

```shell
kubectl apply -f scripts/windup.yaml
```

At this point the container images will be generated by the operator.

# Test Operator in OCP

Create operator container:

```shell
mvn clean package -DskipTests \
-Dquarkus.native.container-build=true \
-Dquarkus.container-image.build=true \
-Dquarkus.container-image.push=false \
-Dquarkus.container-image.registry=quay.io \
-Dquarkus.container-image.group=$USER \
-Dquarkus.container-image.name=windup-operator \
-Dquarkus.operator-sdk.bundle.package-name=windup-operator \
-Dquarkus.operator-sdk.bundle.channels=alpha \
-Dquarkus.application.version=test \
-P native
podman push quay.io/$USER/windup-operator:test
```

Enrich bundle with cluster permissions (util only if generating a catalog for OCP)

```shell
groovy scripts/enrichCSV.groovy target/bundle/windup-operator/manifests/windup-operator.clusterserviceversion.yaml
```

Create bundle:

```shell
BUNDLE_IMAGE=quay.io/$USER/windup-operator-bundle:test
podman build -t $BUNDLE_IMAGE -f target/bundle/windup-operator/bundle.Dockerfile target/bundle/windup-operator
podman push $BUNDLE_IMAGE
```

Create catalog image:

```shell
CATALOG_IMAGE=quay.io/$USER/windup-operator-catalog:test
opm index add \
  --bundles $BUNDLE_IMAGE \
  --tag $CATALOG_IMAGE \
  --build-tool podman
podman push $CATALOG_IMAGE
```

Create catalog:

```shell
cat <<EOF | kubectl apply -f -
apiVersion: operators.coreos.com/v1alpha1
kind: CatalogSource
metadata:
  name: windup-catalog-source
  namespace: openshift-marketplace
spec:
  sourceType: grpc
  image: $CATALOG_IMAGE
EOF
```

At this point you can see the Operator in the marketplace of OCP ready for you to test it.