/*
 * The contents of this file are subject to the terms of the Common Development and
 * Distribution License (the License). You may not use this file except in compliance with the
 * License.
 *
 * You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the
 * specific language governing permission and limitations under the License.
 *
 * When distributing Covered Software, include this CDDL Header Notice in each file and include
 * the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
 * Header, with the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions copyright [year] [name of copyright owner]".
 *
 * Copyright 2016 Charan Mann
 */

/*
 * Groovy script for OpenAM basic authentication
 *
 * This script requires these arguments: userId, password, openamUrl
 */
@Grab(group = 'org.codehaus.groovy.modules.http-builder', module = 'http-builder', version = '0.7.1')

import groovyx.net.http.RESTClient
import org.forgerock.http.protocol.Response
import org.forgerock.http.protocol.Status

/**
 * Creates unauthorized error
 * @return Status.UNAUTHORIZED
 */
def getUnauthorizedError() {
    Response response = new Response()
    response.status = Status.UNAUTHORIZED
    response.entity = "Authentication Failed"
    return response
}

/**
 * Set tokenId in request header and call next handler
 * @param tokenId
 */
def callNextHandler(tokenId) {
    // Set the tokenId in attributes
    attributes.tokenId = tokenId

    // Call the next handler. This returns when the request has been handled.
    return next.handle(context, request)
}

def openAMRESTClient = new RESTClient(openamUrl)

// Check if valid session is present
if (null != request.cookies['iPlanetDirectoryPro']) {
    String openAMCookie = request.cookies['iPlanetDirectoryPro'][0].value

    // Perform cookie validation
    println("iPlanetDirectoryPro cookie found, performing validation")
    response = openAMRESTClient.post(path: 'sessions/' + openAMCookie, query: ['_action': 'validate'])
    isTokenValid = response.getData().get("valid")

    if (isTokenValid) {
        println("Valid OpenAM session, skipping authentication")

        return callNextHandler(openAMCookie)
    } else {
        println("Invalid OpenAM session")
    }
}

// Invoke OpenAM authentication
println("Authenticating user: " + userId)

try {
    response = openAMRESTClient.post(path: 'authenticate', headers: ['X-OpenAM-Username': userId, 'X-OpenAM-Password': password])
}
catch (Exception e) {
    // In case of any failure like authentication failure, server exception etc, return UNAUTHORIZED status. This can be modified to return specific response status for different failures.
    // No need to call next.handle() as we want to terminate handing here
    return getUnauthorizedError()
}

callNextHandler(response.getData().get("tokenId"))


