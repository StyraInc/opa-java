package com.styra.opa.utils;

// This is based on the SpeakeasyHTTPClient that was generated for the Java
// SDK, but has been modified to allow injecting additional headers.

import com.styra.opa.sdk.utils.HTTPClient;
import com.styra.opa.sdk.utils.HTTPRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * This HTTPClient implementation also injects additional headers provided to
 * its constructor into each HTTP request that it sends. This is meant to be
 * used as the HTTP client implementation for instances of the Speakeasy
 * generated OPA SDK.
 */
public class OPAHTTPClient implements HTTPClient {

    private Map<String, String> headers;

    /**
     * Instantiates a new HTTP client suitable for use with the Speakeasy SDK,
     * but with additional headers included in every request.
     *
     * @param extraHeaders The extra headers to include.
     */
    public OPAHTTPClient(Map<String, String> extraHeaders) {
        headers = extraHeaders;
    }

    /**
     * If instantiated with this constructor, OPAHTTPClient will be initialized
     * with an empty list of extra headers to inject.
     */
    public OPAHTTPClient() {
        headers = new HashMap<String, String>();
    }

    /**
     * This method implements compatibility with the
     * com.styra.opa.sdk.utils.HTTPClient interface.
     */
    @Override
    public HttpResponse<InputStream> send(HTTPRequest request)
            throws IOException, InterruptedException, URISyntaxException {
        HttpClient client = HttpClient.newHttpClient();

        for (String headerKey : headers.keySet()) {
            request.addHeader(headerKey, headers.get(headerKey));
        }

        HttpRequest req = request.build();

        HttpResponse<InputStream> response = client.send(req, HttpResponse.BodyHandlers.ofInputStream());
        return response;
    }
}
