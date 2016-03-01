# Protecting REST APIs using OpenIG with OpenAM

This is sample for protecting REST APIs using OpenIG with OpenAM

Assumptions:

1. OpenIG & OpenAM is deployed and configured.
2. REST API server is up and running. This sample uses OpenDJ as REST API server.
3. "OpenDJ Authorization header" filter is required for this sample as OpenDJ is used as REST API server. This filter can be customized as required by specific REST application.
4. The server hosting OpenIG should have internet connectivity as first request tries to download required jars from maven repo. The custom groovy script uses @Grab and it downloads the required dependencies under <User-Home>/.groovy/grapes.

Instructions:

1. Copy 07-opendj-openam under <User-Home>/.openig/config/routes
2. Update openamUrl parameter for "OpenAM Basic Authentication" filter in 07-opendj-openam
3. Update openamUrl, pepUsername, pepPassword, realm, application parameters for "OpenAM Authorization check filter" filter in 07-opendj-openam
4. Copy OpenAMBasicAuthFilter.groovy, BasicAuthFilter.groovy under <User-Home>/.openig/scripts/groovy
5. Restart OpenIG apache tomcat server.


Curl commands:

Note the first request may take few minutes as it downloads the required jars from maven repo.

1. Perform authentication by passing credentials
curl http://opendj-openig.sample.com:8280/opendj/users/demo?_prettyPrint=true --header "X-OpenAM-Username:demo" --header "X-OpenAM-Password:changeit" -v

2. Perform authentication by OpenAM cookie (Note user credentials are required for OpenDJ authentication)
curl http://opendj-openig.sample.com:8280/opendj/users/demo?_prettyPrint=true --header "X-OpenAM-Username:demo" --header "X-OpenAM-Password:changeit" --cookie "iPlanetDirectoryPro=AQIC5wM2LY4SfczXDHAzcfxI_by6QRFrFpMIIIpNpKe-8v8.*AAJTSQACMDEAAlNLABQtNDcxMDg3NzkyNDcyMTIzOTU2OAACUzEAAA..*" -v



