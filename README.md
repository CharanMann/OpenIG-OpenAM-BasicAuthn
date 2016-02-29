# OpenIG-OpenAM-BasicAuthn

Curl commands:

1. Perform authentication by passing credentials
curl http://opendj-openig.sample.com:8280/opendj/users/demo?_prettyPrint=true --header "X-OpenAM-Username:demo" --header "X-OpenAM-Password:changeit" -v

2. Perform authentication by OpenAM cookie (Note user credentials are required for OpenDJ authentication)
curl http://opendj-openig.sample.com:8280/opendj/users/demo?_prettyPrint=true --header "X-OpenAM-Username:demo" --header "X-OpenAM-Password:changeit" --cookie "iPlanetDirectoryPro=AQIC5wM2LY4SfczXDHAzcfxI_by6QRFrFpMIIIpNpKe-8v8.*AAJTSQACMDEAAlNLABQtNDcxMDg3NzkyNDcyMTIzOTU2OAACUzEAAA..*" -v



