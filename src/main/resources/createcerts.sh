#!/bin/sh

########## BOOTSTRAP ##########

# Factory <-> Datacenter

# 1. Generate a self-signed certificate for the broker key store
keytool -genkey -keyalg RSA -alias broker -keystore certs/bootstrap/factorydatacenter/broker.ks -storepass 123456 -validity 9999 -dname "CN=endpoint-service-kafka-*-svc-rte-manufacturing.apps.cluster-cf04.cf04.sandbox37.opentlc.com, OU=EMEA Partner Development, O=Red Hat, L=Milan, ST=Milan, C=IT" -ext "SAN=DNS:endpoint-service-kafka-0-svc-rte-manufacturing.apps.cluster-cf04.cf04.sandbox37.opentlc.com,DNS:endpoint-service-kafka-1-svc-rte-manufacturing.apps.cluster-cf04.cf04.sandbox37.opentlc.com"

# 2. Export the certificate from the broker key store, so that it can be shared with clients. Export the certificate in the Base64-encoded .pem format
keytool -export -alias broker -keystore certs/bootstrap/factorydatacenter/broker.ks -file certs/bootstrap/factorydatacenter/broker_cert.pem -storepass 123456

# 3. On the client, create a client trust store that imports the broker certificate
keytool -import -noprompt -alias broker -keystore certs/bootstrap/factorydatacenter/client.ts -file certs/bootstrap/factorydatacenter/broker_cert.pem -storepass 123456 -trustcacerts

# 4. On the client, generate a self-signed certificate for the client key store
keytool -genkey -alias broker -keyalg RSA -keystore certs/bootstrap/factorydatacenter/client.ks  -storepass 123456 -validity 9999 -dname "CN=qiot-station, OU=EMEA Partner Development, O=Red Hat, L=Milan, ST=Milan, C=IT"

# 5. On the client, export the certificate from the client key store, so that it can be shared with the broker. Export the certificate in the Base64-encoded .pem format
keytool -export -alias broker -keystore certs/bootstrap/factorydatacenter/client.ks -file certs/bootstrap/factorydatacenter/client_cert.pem -storepass 123456

# 6. Create a broker trust store that imports the client certificate
keytool -import -noprompt -alias broker -keystore certs/bootstrap/factorydatacenter/broker.ts -file certs/bootstrap/factorydatacenter/client_cert.pem -storepass 123456 -trustcacerts
  
  
# Machinery <-> Factory

# 1. Generate a self-signed certificate for the broker key store
keytool -genkey -keyalg RSA -alias broker -keystore certs/bootstrap/machineryfactory/broker.ks -storepass 123456 -validity 9999 -dname "CN=endpoint-service-kafka-*-svc-rte-manufacturing.apps.cluster-cf04.cf04.sandbox37.opentlc.com, OU=EMEA Partner Development, O=Red Hat, L=Milan, ST=Milan, C=IT" -ext "SAN=DNS:endpoint-service-kafka-0-svc-rte-manufacturing.apps.cluster-cf04.cf04.sandbox37.opentlc.com,DNS:endpoint-service-kafka-1-svc-rte-manufacturing.apps.cluster-cf04.cf04.sandbox37.opentlc.com"

# 2. Export the certificate from the broker key store, so that it can be shared with clients. Export the certificate in the Base64-encoded .pem format
keytool -export -alias broker -keystore certs/bootstrap/machineryfactory/broker.ks -file certs/bootstrap/machineryfactory/broker_cert.pem -storepass 123456

# 3. On the client, create a client trust store that imports the broker certificate
keytool -import -noprompt -alias broker -keystore certs/bootstrap/machineryfactory/client.ts -file certs/bootstrap/machineryfactory/broker_cert.pem -storepass 123456 -trustcacerts

# 4. On the client, generate a self-signed certificate for the client key store
keytool -genkey -alias broker -keyalg RSA -keystore certs/bootstrap/machineryfactory/client.ks  -storepass 123456 -validity 9999 -dname "CN=qiot-station, OU=EMEA Partner Development, O=Red Hat, L=Milan, ST=Milan, C=IT"

# 5. On the client, export the certificate from the client key store, so that it can be shared with the broker. Export the certificate in the Base64-encoded .pem format
keytool -export -alias broker -keystore certs/bootstrap/machineryfactory/client.ks -file certs/bootstrap/machineryfactory/client_cert.pem -storepass 123456

# 6. Create a broker trust store that imports the client certificate
keytool -import -noprompt -alias broker -keystore certs/bootstrap/machineryfactory/broker.ts -file certs/bootstrap/machineryfactory/client_cert.pem -storepass 123456 -trustcacerts
  
  
  
  
  
  
########## RUNTIME ##########

# Factory <-> Datacenter

# 1. Generate a self-signed certificate for the broker key store
keytool -genkey -keyalg RSA -alias broker -keystore certs/runtime/factorydatacenter/broker.ks -storepass 123456 -validity 9999 -dname "CN=endpoint-service-kafka-*-svc-rte-manufacturing.apps.cluster-cf04.cf04.sandbox37.opentlc.com, OU=EMEA Partner Development, O=Red Hat, L=Milan, ST=Milan, C=IT" -ext "SAN=DNS:endpoint-service-kafka-0-svc-rte-manufacturing.apps.cluster-cf04.cf04.sandbox37.opentlc.com,DNS:endpoint-service-kafka-1-svc-rte-manufacturing.apps.cluster-cf04.cf04.sandbox37.opentlc.com"

# 2. Export the certificate from the broker key store, so that it can be shared with clients. Export the certificate in the Base64-encoded .pem format
keytool -export -alias broker -keystore certs/runtime/factorydatacenter/broker.ks -file certs/runtime/factorydatacenter/broker_cert.pem -storepass 123456

# 3. On the client, create a client trust store that imports the broker certificate
keytool -import -noprompt -alias broker -keystore certs/runtime/factorydatacenter/client.ts -file certs/runtime/factorydatacenter/broker_cert.pem -storepass 123456 -trustcacerts

# 4. On the client, generate a self-signed certificate for the client key store
keytool -genkey -alias broker -keyalg RSA -keystore certs/runtime/factorydatacenter/client.ks  -storepass 123456 -validity 9999 -dname "CN=qiot-station, OU=EMEA Partner Development, O=Red Hat, L=Milan, ST=Milan, C=IT"

# 5. On the client, export the certificate from the client key store, so that it can be shared with the broker. Export the certificate in the Base64-encoded .pem format
keytool -export -alias broker -keystore certs/runtime/factorydatacenter/client.ks -file certs/runtime/factorydatacenter/client_cert.pem -storepass 123456

# 6. Create a broker trust store that imports the client certificate
keytool -import -noprompt -alias broker -keystore certs/runtime/factorydatacenter/broker.ts -file certs/runtime/factorydatacenter/client_cert.pem -storepass 123456 -trustcacerts
  
  
# Machinery <-> Factory

# 1. Generate a self-signed certificate for the broker key store
keytool -genkey -keyalg RSA -alias broker -keystore certs/runtime/machineryfactory/broker.ks -storepass 123456 -validity 9999 -dname "CN=endpoint-service-kafka-*-svc-rte-manufacturing.apps.cluster-cf04.cf04.sandbox37.opentlc.com, OU=EMEA Partner Development, O=Red Hat, L=Milan, ST=Milan, C=IT" -ext "SAN=DNS:endpoint-service-kafka-0-svc-rte-manufacturing.apps.cluster-cf04.cf04.sandbox37.opentlc.com,DNS:endpoint-service-kafka-1-svc-rte-manufacturing.apps.cluster-cf04.cf04.sandbox37.opentlc.com"

# 2. Export the certificate from the broker key store, so that it can be shared with clients. Export the certificate in the Base64-encoded .pem format
keytool -export -alias broker -keystore certs/runtime/machineryfactory/broker.ks -file certs/runtime/machineryfactory/broker_cert.pem -storepass 123456

# 3. On the client, create a client trust store that imports the broker certificate
keytool -import -noprompt -alias broker -keystore certs/runtime/machineryfactory/client.ts -file certs/runtime/machineryfactory/broker_cert.pem -storepass 123456 -trustcacerts

# 4. On the client, generate a self-signed certificate for the client key store
keytool -genkey -alias broker -keyalg RSA -keystore certs/runtime/machineryfactory/client.ks  -storepass 123456 -validity 9999 -dname "CN=qiot-station, OU=EMEA Partner Development, O=Red Hat, L=Milan, ST=Milan, C=IT"

# 5. On the client, export the certificate from the client key store, so that it can be shared with the broker. Export the certificate in the Base64-encoded .pem format
keytool -export -alias broker -keystore certs/runtime/machineryfactory/client.ks -file certs/runtime/machineryfactory/client_cert.pem -storepass 123456

# 6. Create a broker trust store that imports the client certificate
keytool -import -noprompt -alias broker -keystore certs/runtime/machineryfactory/broker.ts -file certs/runtime/machineryfactory/client_cert.pem -storepass 123456 -trustcacerts