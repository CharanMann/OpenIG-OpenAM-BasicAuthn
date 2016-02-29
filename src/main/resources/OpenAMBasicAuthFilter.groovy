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
 * This script requires these arguments: userId, password, openAMURL
 */
import org.forgerock.http.protocol.Response
import org.forgerock.http.protocol.Status

import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * Extracts the token from REST response string from OpenAM
 * @param restResponse
 * @return tokenId
 */
def getToken(String restResponse) {
    String REGEX_TOKEN = "\\s*\\S*\"tokenId\"\\s*:\"(.*)\",";
    Pattern pattern = Pattern.compile(REGEX_TOKEN);
    Matcher m = pattern.matcher(restResponse)
    String tokenId
    if (m.find()) {
        tokenId = m.group(1)
    }
    return tokenId
}

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

def invokeOpenAMEndpoint(String openAMEndpoint, Map headers) {
    HttpURLConnection connection
    try {
        // Invoke OpenAM REST authentication
        URL url = new URL(openAMEndpoint)
        connection = (HttpURLConnection) url.openConnection()
        connection.setRequestMethod("POST")

        //Add headers
        headers.each { hName, hValue ->
            println("Adding header : " + hName + " , value: " + hValue)
            connection.setRequestProperty(hName, hValue)
        }

        int responseCode = connection.getResponseCode()
        println("Response Code: " + responseCode)
        if (responseCode != HttpURLConnection.HTTP_OK) {
            // If authentication fails, return UNAUTHORIZED status. This can be modified to return specific response status for different failures.
            return getUnauthorizedError()
        }

        // Read response
        BufferedReader br = new BufferedReader(new InputStreamReader(
                (connection.getInputStream())))
        String output = br.readLine()

        return output

    } catch (Exception e) {
        e.printStackTrace();
        // In case of any server issue, return UNAUTHORIZED status
        return getUnauthorizedError()
    }
    finally {
        connection.disconnect()
    }

    // Should not reach here
    return null;
}

//TODO Add OpenAM token presense check and validation
//if (request.cookies['iPlanetDirectoryPro']!=null)
//{
//  String openAMCookie = request.cookies['iPlanetDirectoryPro']

//}

// Invoke OpenAM authentication
def headers = ["Content-Type": "application/json", "X-OpenAM-Username": userId, "X-OpenAM-Password": password]
println("Authenticating user: " + userId)
String output = invokeOpenAMEndpoint(openAMURL+"authenticate", headers)
// Set the tokenId in request header
request.headers.add("tokenId", getToken(output))

// Call the next handler. This returns when the request has been handled.
return next.handle(context, request)


