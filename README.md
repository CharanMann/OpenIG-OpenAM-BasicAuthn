# OpenIG-OpenAM-BasicAuthn

Instructions:

1. Copy these jars in <OpenIG_Tomcat>/webapps/ROOT/WEB-INF/lib folder:
 http-builder-0.7.1.jar
 json-lib-2.4-jdk15.jar
 xml-resolver-1.2.jar
 commons-collections-3.2.1.jar
2. Copy 07-opendj-openam under <User-Home>/.openig/config/routes
3. Update openAMURL in 07-opendj-openam
4. Copy OpenAMBasicAuthFilter.groovy, BasicAuthFilter.groovy under <User-Home>/.openig/scripts/groovy
5. Restart OpenIG apache tomcat server


Curl commands:

1. Perform authentication by passing credentials
curl http://opendj-openig.sample.com:8280/opendj/users/demo?_prettyPrint=true --header "X-OpenAM-Username:demo" --header "X-OpenAM-Password:changeit" -v

2. Perform authentication by OpenAM cookie (Note user credentials are required for OpenDJ authentication)
curl http://opendj-openig.sample.com:8280/opendj/users/demo?_prettyPrint=true --header "X-OpenAM-Username:demo" --header "X-OpenAM-Password:changeit" --cookie "iPlanetDirectoryPro=AQIC5wM2LY4SfczXDHAzcfxI_by6QRFrFpMIIIpNpKe-8v8.*AAJTSQACMDEAAlNLABQtNDcxMDg3NzkyNDcyMTIzOTU2OAACUzEAAA..*" -v



