package com.styra.opa;

import com.styra.opa.sdk.Opa;

import com.styra.opa.sdk.models.operations.ExecutePolicyWithInputRequest;
import com.styra.opa.sdk.models.operations.ExecutePolicyWithInputRequestBody;
import com.styra.opa.sdk.models.operations.ExecutePolicyWithInputResponse;

import com.styra.opa.sdk.models.shared.Explain;
import com.styra.opa.sdk.models.shared.Input;

import com.styra.opa.sdk.utils.HTTPClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.styra.opa.utils.OPAHTTPClient;

import java.util.Map;


public class OPA {

    // Stores the state needed to communicate with OPA, such as the HTTP client
    // and configuration information. This is re-used across requests.
    private Opa sdk;

    // Values to use when generating requests.
    private boolean policyRequestPretty = false;
    private boolean policyRequestProvenance = false;
    private Explain policyRequestExplain = Explain.NOTES;
    private boolean policyRequestMetrics = false;
    private boolean policyRequestInstrument = false;
    private boolean policyRequestStrictBuiltinErrors = false;

    // Default values to use when creating the SDK instance.
    private String sdkServerURL = "http://localhost:8181";

    // Instantiates a new instance of the Speakeasy generated SDK internally
    // with default settings.
    public OPA() {
        this.sdk = Opa.builder().serverURL(sdkServerURL).build();
    }


    public OPA(String opaURL) {
        this.sdkServerURL = opaURL;
        this.sdk = Opa.builder().serverURL(opaURL).build();
    }

    // This constructor allows additional HTTP headers to be provided that will
    // be included with each request. This is intended to support bearer token
    // authorization:
    //
    // https://www.openpolicyagent.org/docs/latest/rest-api/#authentication
    public OPA(String opaURL, Map<String, String> headers) {
        this.sdkServerURL = opaURL;
        HTTPClient client = new OPAHTTPClient(headers);
        this.sdk = Opa.builder().serverURL(opaURL).client(client).build();
    }

    // Use a custom instance of the Speakeasy generated SDK. This can allow for
    // modifying configuration options that are not otherwise exposed in the
    // porcelain API.
    public OPA(Opa sdk) {
        this.sdk = sdk;
    }

    public boolean check(java.util.Map<String, Object> input, String path) throws OPAException {
        return query(input, path);
    }

    public boolean check(String input, String path) throws OPAException {
        return query(input, path);
    }

    public boolean check(boolean input, String path) throws OPAException {
        return query(input, path);
    }

    public boolean check(double input, String path) throws OPAException {
        return query(input, path);
    }

    public boolean check(java.util.List<Object> input, String path) throws OPAException {
        return query(input, path);
    }

    public <T> T query(java.util.Map<String, Object> input, String path) throws OPAException {
        return queryMachinery(Input.of(input), path);
    }

    public <T> T query(String input, String path) throws OPAException {
        return queryMachinery(Input.of(input), path);
    }

    public <T> T query(boolean input, String path) throws OPAException {
        return queryMachinery(Input.of(input), path);
    }

    public <T> T query(double input, String path) throws OPAException {
        return queryMachinery(Input.of(input), path);
    }

    public <T> T query(java.util.List<Object> input, String path) throws OPAException {
        return queryMachinery(Input.of(input), path);
    }

    private <T> T queryMachinery(Input input, String path) throws OPAException {
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
        } catch (Exception e) {
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
