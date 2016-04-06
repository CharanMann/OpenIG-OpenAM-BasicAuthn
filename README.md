# OpenIG-OpenAM-BasicAuthn

*Protecting REST APIs using OpenIG with OpenAM*


This is sample for protecting REST APIs using OpenIG with OpenAM  

Disclaimer of Liability :
=========================
The code for this project is only meant for DEMO purposes only and is not PRODUCTION ready. I make no warranty, representation or undertaking whether expressed or implied, nor does it assume any 
legal liability, whether direct or indirect, or responsibility for the accuracy, completeness, or usefulness of any information. 

Further analysis on the detailed requirements, fine-tuning and validation of the proposed architecture is still required by the selected Systems Integrator and/or Architects in charge of 
architecting the IAM project.

Pre-requisites :
================
1. OpenIG & OpenAM is deployed and configured.
2. REST API server is up and running. This sample uses OpenDJ as REST API server.
3. "OpenDJ Authorization header" filter is required for this sample as OpenDJ is used as REST API server. This filter can be customized as required by specific REST application.
4. The server hosting OpenIG should have internet connectivity as first request tries to download required jars from maven repo. The custom groovy script uses @Grab and it downloads the required dependencies under <User-Home>/.groovy/grapes.
   
OpenIG Configuration:
=====================
1. Copy 07-opendj-openam under <User-Home>/.openig/config/routes
2. Update openamUrl parameter for "OpenAM Basic Authentication" filter in 07-opendj-openam
3. Update openamUrl, pepUsername, pepPassword, realm, application parameters for "OpenAM Authorization check filter" filter in 07-opendj-openam
4. Copy OpenAMBasicAuthFilter.groovy, BasicAuthFilter.groovy under <User-Home>/.openig/scripts/groovy
5. Restart OpenIG apache tomcat server.

Testing:
======== 
Curl command(s):
Note the first request may take few minutes as it downloads the required jars from maven repo.

- Perform authentication by passing credentials
curl http://opendj-openig.sample.com:8280/opendj/users/demo?_prettyPrint=true --header "X-OpenAM-Username:demo" --header "X-OpenAM-Password:changeit" -v

- Perform authentication by OpenAM cookie (Note user credentials are required for OpenDJ authentication)
curl http://opendj-openig.sample.com:8280/opendj/users/demo?_prettyPrint=true --header "X-OpenAM-Username:demo" --header "X-OpenAM-Password:changeit" --cookie "iPlanetDirectoryPro=AQIC5wM2LY4SfczXDHAzcfxI_by6QRFrFpMIIIpNpKe-8v8.*AAJTSQACMDEAAlNLABQtNDcxMDg3NzkyNDcyMTIzOTU2OAACUzEAAA..*" -v

* * *

The contents of this file are subject to the terms of the Common Development and
Distribution License (the License). You may not use this file except in compliance with the
License.

You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the
specific language governing permission and limitations under the License.

When distributing Covered Software, include this CDDL Header Notice in each file and include
the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
Header, with the fields enclosed by brackets [] replaced by your own identifying
information: "Portions copyright [year] [name of copyright owner]".

Copyright 2016 Charan Mann

Portions Copyrighted 2016 ForgeRock AS
