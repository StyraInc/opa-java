package com.styra.opa.utils;

// This is based on the SpeakeasyHTTPClient that was generated for the Java
// SDK, but has been modified to allow injecting additional headers.

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.InputStream;
import java.util.Map;

import com.styra.opa.sdk.utils.HTTPRequest;
import com.styra.opa.sdk.utils.HTTPClient;

public class OPAHTTPClient implements HTTPClient {

    private Map<String, String> headers;

    // Instantiates a new HTTP client suitable for use with the Speakeasy SDK,
    // but with additional headers included in every request.
    public OPAHTTPClient(Map<String, String> extraHeaders) {
        headers = extraHeaders;
    }

    @Override
    public HttpResponse<InputStream> send(HTTPRequest request)
            throws IOException, InterruptedException, URISyntaxException {
        HttpClient client = HttpClient.newHttpClient();

        HTTPRequest requestBuilder = new HTTPRequest();

        for (String headerKey : headers.keySet()) {
            requestBuilder = requestBuilder.addHeader(headerKey, headers.get(headerKey));
        }

        HttpRequest req =  requestBuilder.build();

        HttpResponse<InputStream> response = client.send(req, HttpResponse.BodyHandlers.ofInputStream());
        return response;
    }
}
