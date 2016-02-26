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

String REGEX = "\\s*\\S*\"tokenId\"\\s*:\"(.*)\",";
Pattern pattern = Pattern.compile(REGEX);
HttpURLConnection connection

try {
    // Invoke OpenAM REST authentication
    URL url = new URL(openAMURL)
    connection = (HttpURLConnection) url.openConnection()
    connection.setRequestMethod("POST")
    connection.setRequestProperty("Accept", "application/json")
    connection.setRequestProperty("X-OpenAM-Username", userId)
    connection.setRequestProperty("X-OpenAM-Password", password)

    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
        // If authentication fails, return UNAUTHORIZED status. This can be modified to return specific response status for different failures.
        response = new Response()
        response.status = Status.UNAUTHORIZED
        response.entity = "Authentication Failed"
        return response
    }

    // Read response
    BufferedReader br = new BufferedReader(new InputStreamReader(
            (connection.getInputStream())))
    String output = br.readLine()

    Matcher m = pattern.matcher(output)
    String tokenId
    if (m.find()) {
        tokenId = m.group(1)
    }

    // Set the tokenId in request header
    request.headers.add("tokenId", tokenId)

    // Call the next handler. This returns when the request has been handled.
    return next.handle(context, request)
} catch (Exception e) {
    // In case of any server issue, return UNAUTHORIZED status
    response = new Response()
    response.status = Status.UNAUTHORIZED
    response.entity = "Authentication Failed"
}
finally {
    connection.disconnect()
}

return response
