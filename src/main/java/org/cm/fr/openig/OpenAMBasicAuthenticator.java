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

package org.cm.fr.openig;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * OpenAMBasicAuthenticator for testing purposes only. Not required to be used in OpenAMBasicAuthFilter.groovy
 */
public class OpenAMBasicAuthenticator {

    private String REGEX = "\\s*\\S*\"tokenId\"\\s*:\"(.*)\",";
    private Pattern pattern = Pattern.compile(REGEX);


    /**
     * User authentication using OpenAM basic authn
     *
     * @param userId
     * @param password
     * @param openAMURL
     * @return tokenId for authenticated user, null if authentication fails
     */
    public String authenticate(String userId, String password, String openAMURL) {

        try {
            URL url = new URL(openAMURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("X-OpenAM-Username", userId);
            conn.setRequestProperty("X-OpenAM-Password", password);

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

    public static void main(String[] args) {
        OpenAMBasicAuthenticator openAMBasicAuthenticator = new OpenAMBasicAuthenticator();
        System.out.println("User authenticated successfully, tokenId: " + openAMBasicAuthenticator.authenticate("charan", "password", "http://openam13.sample.com:8080/openam/json/employees/authenticate"));


    }

}

