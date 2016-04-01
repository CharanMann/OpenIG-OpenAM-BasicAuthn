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
 * Portions Copyrighted 2016 ForgeRock AS
 *
 * OpenIG-OpenAM-BasicAuthn: Created by Charan Mann on 4/1/16 , 6:12 AM.
 */

package org.cm.fr.openig;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * OpenAMBasicAuthenticatorTest for testing purposes only. Not required to be used in OpenAMBasicAuthFilter.groovy
 */
public class OpenAMBasicAuthenticatorTest {

    private String REGEX = "\\s*\\S*\"tokenId\"\\s*:\"(.*)\",";
    private Pattern pattern = Pattern.compile(REGEX);


    /**
     * User authentication using OpenAM basic authn
     *
     * @param userId    user id
     * @param password  user's password
     * @param openAMURL openAM URL
     * @return tokenId for authenticated user, null if authentication fails
     */
    public String authenticate(String userId, String password, String openAMURL) {

        try {
            URL url = new URL(openAMURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            Map headers = new HashMap();
            headers.put("Content-Type", "application/json");
            headers.put("X-OpenAM-Username", userId);
            headers.put("X-OpenAM-Password", password);
            setHeaders(conn, headers);

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String output = br.readLine();

            Matcher m = pattern.matcher(output);
            if (m.find()) {
                return m.group(1);
            }
            conn.disconnect();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public String tokenValidation(String tokenId, String openAMURL) {
        HttpURLConnection conn = null;

        try {
            URL url = new URL(openAMURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");


            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            return br.readLine();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            conn.disconnect();
        }
    }


    private void setHeaders(HttpURLConnection conn, Map headers) {
        for (Object headerName : headers.keySet()) {
            conn.setRequestProperty((String) headerName, (String) headers.get(headerName));
        }
    }

    public static void main(String[] args) {
        OpenAMBasicAuthenticatorTest openAMBasicAuthenticatorTest = new OpenAMBasicAuthenticatorTest();
        String tokenId = openAMBasicAuthenticatorTest.authenticate("testUser1", "password", "http://openam13.sample.com:8080/openam/json/authenticate");
        System.out.println("User authenticated successfully, tokenId: " + tokenId);

        System.out.println(openAMBasicAuthenticatorTest.tokenValidation(tokenId, "http://openam13.sample.com:8080/openam/json/sessions/" + tokenId + "?_action=validate").contains("\"valid\":true"));


    }

}

