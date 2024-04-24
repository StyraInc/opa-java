package com.styra.opa;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.styra.opa.openapi.OpaApiClient;
import com.styra.opa.openapi.models.operations.ExecutePolicyWithInputRequest;
import com.styra.opa.openapi.models.operations.ExecutePolicyWithInputRequestBody;
import com.styra.opa.openapi.models.operations.ExecutePolicyWithInputResponse;
import com.styra.opa.openapi.models.shared.Explain;
import com.styra.opa.openapi.models.shared.Input;
import com.styra.opa.openapi.utils.HTTPClient;
import com.styra.opa.utils.OPAHTTPClient;

import java.util.Map;

/**
 * The OPA class contains all the functionality and configuration needed to
 * make "happy path" queries against an OPA server. Internally, it instantiates
 * an instance of Speakeasy generated SDK (com.styra.opa.sdk.Opa) with
 * appropriate settings based on the parameters provided in the constructor for
 * this class.
 *
 * @author
 * @version
 */
public class OPAClient {

    /**
     * Stores the state needed to communicate with OPA, such as the HTTP client
     * and configuration information. This is re-used across requests.
     */
    private OpaApiClient sdk;

    // Values to use when generating requests.
    private boolean policyRequestPretty;
    private boolean policyRequestProvenance;
    private Explain policyRequestExplain = Explain.NOTES;
    private boolean policyRequestMetrics;
    private boolean policyRequestInstrument;
    private boolean policyRequestStrictBuiltinErrors;

    /**
     * Default OPA server URL to connect to.
     */
    private String sdkServerURL = "http://localhost:8181";

    /**
     * Instantiate a new OPA wrapper with default settings. This will
     * automatically try to connect to OPA on localhost:8181, which is a
     * suitable value for most sidecar style deployments of OPA.
     */
    public OPAClient() {
        this.sdk = OpaApiClient.builder().serverURL(sdkServerURL).build();
    }

    /**
     * Instantiates an OPA API wrapper with a custom OPA URL.
     *
     * @param opaURL URL at which OPA should be connected to.
     */
    public OPAClient(String opaURL) {
        this.sdkServerURL = opaURL;
        this.sdk = OpaApiClient.builder().serverURL(opaURL).build();
    }

    /**
     * This constructor allows instantiating the OPA wrapper with additional
     * HTTP headers to be injected into every request. This is intended to be
     * used with OPA bearer token authentication, which you can learn more
     * about here: https://www.openpolicyagent.org/docs/latest/rest-api/#authentication
     *
     * @param opaURL URL at which OPA should be connected to.
     * @param headers additional HTTP headers to inject into each request.
     */
    public OPAClient(String opaURL, Map<String, String> headers) {
        this.sdkServerURL = opaURL;
        HTTPClient client = new OPAHTTPClient(headers);
        this.sdk = OpaApiClient.builder().serverURL(opaURL).client(client).build();
    }

    /**
     * This constructor acts as an escape hatch, allowing you to provide your
     * own instance of the Speakeasy generated client. This may be useful if you
     * need to bring your own HTTP client implementation, or otherwise need to
     * configure the client in a more advanced way than this high level wrapper
     * permits.
     *
     * @param client
     */
    public OPAClient(OpaApiClient client) {
        this.sdk = client;
    }

    /**
     * Perform a query with an input document against a Rego rule head by its
     * path, and coerce the result to a boolean.
     *
     * The other overloaded variations of this method behave similar, but allow
     * any valid JSON value to be used as the input document.
     *
     * @param input Input document for OPA query.
     * @param path Path to rule head to query, for example to access a rule
     * head "allow" in a Rego file that starts with "package main", you would
     * provide the value "main/allow".
     * @return
     * @throws OPAException
     */
    public boolean check(java.util.Map<String, Object> input, String path) throws OPAException {
        return evaluate(input, path);
    }

    public boolean check(String input, String path) throws OPAException {
        return evaluate(input, path);
    }

    public boolean check(boolean input, String path) throws OPAException {
        return evaluate(input, path);
    }

    public boolean check(double input, String path) throws OPAException {
        return evaluate(input, path);
    }

    public boolean check(java.util.List<Object> input, String path) throws OPAException {
        return evaluate(input, path);
    }

    /**
     * Perform a query with an input document against a Rego rule head by its
     * path.
     *
     * The other overloaded variations of this method behave similar, but allow
     * any valid JSON value to be used as the input document.
     *
     * @param input Input document for OPA query.
     * @param path Path to rule head to query, for example to access a rule
     * head "allow" in a Rego file that starts with "package main", you would
     * provide the value "main/allow".
     * @return The return value is automatically coerced to have a type
     * matching the type parameter T using
     * com.fasterxml.jackson.databind.ObjectMapper.
     * @throws OPAException
     */
    public <T> T evaluate(java.util.Map<String, Object> input, String path) throws OPAException {
        return evaluateMachinery(Input.of(input), path);
    }

    public <T> T evaluate(String input, String path) throws OPAException {
        return evaluateMachinery(Input.of(input), path);
    }

    public <T> T evaluate(boolean input, String path) throws OPAException {
        return evaluateMachinery(Input.of(input), path);
    }

    public <T> T evaluate(double input, String path) throws OPAException {
        return evaluateMachinery(Input.of(input), path);
    }

    public <T> T evaluate(java.util.List<Object> input, String path) throws OPAException {
        return evaluateMachinery(Input.of(input), path);
    }

    /**
     * General-purpose wrapper around the Speakeasy generated
     * ExecutePolicyWithInputResponse API.
     *
     * @param input
     * @param path
     * @return
     * @throws OPAException
     */
    private <T> T evaluateMachinery(Input input, String path) throws OPAException {
        ExecutePolicyWithInputRequest req = ExecutePolicyWithInputRequest.builder()
            .path(path)
            .requestBody(ExecutePolicyWithInputRequestBody.builder()
                    .input(input).build())
            .pretty(policyRequestPretty)
            .provenance(policyRequestProvenance)
            .explain(policyRequestExplain)
            .metrics(policyRequestMetrics)
            .instrument(policyRequestInstrument)
            .strictBuiltinErrors(policyRequestStrictBuiltinErrors)
            .build();

        ExecutePolicyWithInputResponse res;

        try {
            res = sdk.executePolicyWithInput()
                .request(req)
                .call();

        // Although it is preferred not to catch Exception, the generated
        // Speakeasy SDK `throws Exception`, so we have to catch it. If the
        // caller really cares, they can always unwrap the OPAException.
        //CHECKSTYLE:OFF
        } catch (Exception e) {
            //CHECKSTYLE:ON
            e.printStackTrace(System.out);
            String msg = String.format("executing policy at '%s' with failed due to exception '%s'", path, e);
            throw new OPAException(msg, e);
        }

        if (res.successfulPolicyEvaluation().isPresent()) {
            Object out = res.successfulPolicyEvaluation().get().result().get().value();
            ObjectMapper mapper = new ObjectMapper();
            T typedResult = mapper.convertValue(out, new TypeReference<T>() {});
            return typedResult;
        } else {
            return null;
        }
    }
}
