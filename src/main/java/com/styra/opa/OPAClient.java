package com.styra.opa;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.styra.opa.openapi.OpaApiClient;
import com.styra.opa.openapi.models.errors.SDKError;
import com.styra.opa.openapi.models.errors.ServerError;
import com.styra.opa.openapi.models.errors.ServerErrorErrors;
import com.styra.opa.openapi.models.errors.ServerErrorLocation;
import com.styra.opa.openapi.models.operations.ExecuteBatchPolicyWithInputRequest;
import com.styra.opa.openapi.models.operations.ExecuteBatchPolicyWithInputRequestBody;
import com.styra.opa.openapi.models.operations.ExecuteBatchPolicyWithInputResponse;
import com.styra.opa.openapi.models.operations.ExecuteDefaultPolicyWithInputRequest;
import com.styra.opa.openapi.models.operations.ExecuteDefaultPolicyWithInputResponse;
import com.styra.opa.openapi.models.operations.ExecutePolicyRequest;
import com.styra.opa.openapi.models.operations.ExecutePolicyResponse;
import com.styra.opa.openapi.models.operations.ExecutePolicyWithInputRequest;
import com.styra.opa.openapi.models.operations.ExecutePolicyWithInputRequestBody;
import com.styra.opa.openapi.models.operations.ExecutePolicyWithInputResponse;
import com.styra.opa.openapi.models.shared.BatchMixedResults;
import com.styra.opa.openapi.models.shared.BatchSuccessfulPolicyEvaluation;
import com.styra.opa.openapi.models.shared.Explain;
import com.styra.opa.openapi.models.shared.Input;
import com.styra.opa.openapi.models.shared.Responses;
import com.styra.opa.openapi.models.shared.ResponsesErrors;
import com.styra.opa.openapi.models.shared.ResponsesLocation;
import com.styra.opa.openapi.models.shared.Result;
import com.styra.opa.openapi.models.shared.ServerErrorWithStatusCode;
import com.styra.opa.openapi.models.shared.SuccessfulPolicyResponse;
import com.styra.opa.openapi.models.shared.SuccessfulPolicyResponseWithStatusCode;
import com.styra.opa.openapi.utils.HTTPClient;
import com.styra.opa.utils.OPAHTTPClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

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

    // The first time a batch request fails, we trip this and subsequently
    // always use the fallback method.
    private boolean enableBatchFallback; // implicitly initialized to false

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
        this.sdk = OpaApiClient.builder().client(new OPAHTTPClient()).serverURL(sdkServerURL).build();
    }

    /**
     * Instantiates an OPA API wrapper with a custom OPA URL.
     *
     * @param opaURL URL at which OPA should be connected to.
     */
    public OPAClient(String opaURL) {
        this.sdkServerURL = opaURL;
        this.sdk = OpaApiClient.builder().client(new OPAHTTPClient()).serverURL(opaURL).build();
    }

    /**
     * This constructor can be used to instantiate an OPAClient which uses a
     * custom HTTP client implementation for the underlying OpaApiClient.
     *
     * @param opaURL URL at which OPA should be connected to.
     * @param httpclient custom HTTP client to use
     */
    public OPAClient(String opaURL, HTTPClient httpclient) {
        this.sdkServerURL = opaURL;
        this.sdk = OpaApiClient.builder().serverURL(opaURL).client(httpclient).build();
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

    private Optional<Input> defaultInput() {
        return Optional.empty();
    }

    /**
     * Bypass batch mode support detection and force the API client to
     * enable or disable fallback mode.
     *
     * If newEnableBatchFallback is 'true', then fallback mode is always used.
     * If it is 'false', then batch support detection will be reset, though
     * subsequent batch evaluation calls may detect if the batch API is not
     * supported and re-enable the fallback later. You should not need to use
     * this method during normal usage.
     */
    public void forceBatchFallback(boolean newEnableBatchFallback) {
        this.enableBatchFallback = newEnableBatchFallback;
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
    public boolean check(String path, java.util.Map<String, Object> input) throws OPAException {
        return evaluate(path, input);
    }

    public boolean check(String path, String input) throws OPAException {
        return evaluate(path, input);
    }

    public boolean check(String path, boolean input) throws OPAException {
        return evaluate(path, input);
    }

    public boolean check(String path, double input) throws OPAException {
        return evaluate(path, input);
    }

    public boolean check(String path, java.util.List<Object> input) throws OPAException {
        return evaluate(path, input);
    }

    public boolean check(String path, java.lang.Object input) throws OPAException {
        ObjectMapper om = new ObjectMapper();
        Map<String, Object> iMap = om.convertValue(input, new TypeReference<Map<String, Object>>() {});
        return evaluate(path, iMap);
    }

    // check with optional path

    public boolean check(java.util.Map<String, Object> input) throws OPAException {
        return evaluate(input);
    }

    public boolean check(boolean input) throws OPAException {
        return evaluate(input);
    }

    public boolean check(double input) throws OPAException {
        return evaluate(input);
    }

    public boolean check(java.util.List<Object> input) throws OPAException {
        return evaluate(input);
    }

    public boolean check(java.lang.Object input) throws OPAException {
        ObjectMapper om = new ObjectMapper();
        Map<String, Object> iMap = om.convertValue(input, new TypeReference<Map<String, Object>>() {});
        return evaluate(iMap);
    }

    /**
     * Perform a query with an input document against a Rego rule head by its
     * path.
     *
     * The other overloaded variations of this method behave similar, but allow
     * any valid JSON value to be used as the input document.
     *
     * If the input value is omitted, then an empty object is implicitly used
     * as the input.
     *
     * Due to limitations in Java's generics, the type parameter T alone is not
     * always sufficient to determine the correct type to coerce the output to
     * (specifically with constructing the TypeReference to use with
     * fasterxml's ObjectMapper). In particular, the compiler needs a little
     * extra help when assigning the return of this method to an object. In
     * such situations, you will need to also provide a toValueType. Some ways
     * this might be accomplished are shown below:
     *
     * <pre>
     * // likely to fail at compile time with:
     * //     java.lang.ClassCastException ... cannot be cast to class MyObject
     * MyObject obj = evaluate("/foo", "bar");
     *
     * // using a TypeReference (recommended method)
     * MyObject obj = evaluate("/foo", "bar", TypeReference&#60;MyObject&#62;() {});
     *
     * // using ObjectMapper
     * MyObject obj = evaluate("/foo", "bar", new ObjectMapper().constructType(instanceOfMyObject.getClass()));
     *
     * // using .class (can cause checking issue with classes that have type parameters)
     * MyObject obj = evaluate("/foo", "bar", MyObject.class);
     * </pre>
     *
     * If the path parameter is omitted, then the default path `/` is used,
     * which will cause OPA to use the default decision at /data/system/main.
     * To avoid ambiguity with the function overload, if only a string argument
     * is provided, it is taken to be the path rather than an input to be used
     * with the default path. This is to simplify accessing the default path
     * without an input.
     *
     * @param input Input document for OPA query.
     * @param path Path to rule head to query, for example to access a rule
     * head "allow" in a Rego file that starts with "package main", you would
     * provide the value "main/allow".
     * @param toValueType May optionally be used to provide an alternative
     * type for output conversion. This is especially useful when reading
     * the result of an OPA request into an object.
     * @return The return value is automatically coerced to have a type
     * matching the type parameter T using
     * com.fasterxml.jackson.databind.ObjectMapper.
     * @throws OPAException
     */
    public <T> T evaluate(String path, java.util.Map<String, Object> input, Class<T> toValueType) throws OPAException {
        return evaluateMachinery(Optional.of(Input.of(input)), Optional.of(path), toValueType);
    }

    public <T> T evaluate(String path, String input, Class<T> toValueType) throws OPAException {
        return evaluateMachinery(Optional.of(Input.of(input)), Optional.of(path), toValueType);
    }

    public <T> T evaluate(String path, boolean input, Class<T> toValueType) throws OPAException {
        return evaluateMachinery(Optional.of(Input.of(input)), Optional.of(path), toValueType);
    }

    public <T> T evaluate(String path, double input, Class<T> toValueType) throws OPAException {
        return evaluateMachinery(Optional.of(Input.of(input)), Optional.of(path), toValueType);
    }

    public <T> T evaluate(String path, java.util.List<Object> input, Class<T> toValueType) throws OPAException {
        return evaluateMachinery(Optional.of(Input.of(input)), Optional.of(path), toValueType);
    }

    public <T> T evaluate(String path, java.lang.Object input, Class<T> toValueType) throws OPAException {
        ObjectMapper om = new ObjectMapper();
        Map<String, Object> iMap = om.convertValue(input, new TypeReference<Map<String, Object>>() {});
        return evaluateMachinery(Optional.of(Input.of(iMap)), Optional.of(path), toValueType);
    }

    // evaluate with Class<T> toTypeValue, but paths elided

    public <T> T evaluate(java.util.Map<String, Object> input, Class<T> toValueType) throws OPAException {
        return evaluateMachinery(Optional.of(Input.of(input)), Optional.empty(), toValueType);
    }

    /**
     * Special case where a single string argument is used as the path rather
     * than the input, unlike other single argument overloads.
     *
     * @param path
     * @param toValueType
     * @return
     * @throws OPAException
     */
    public <T> T evaluate(String path, Class<T> toValueType) throws OPAException {
        return evaluateMachinery(defaultInput(), Optional.of(path), toValueType);
    }

    public <T> T evaluate(boolean input, Class<T> toValueType) throws OPAException {
        return evaluateMachinery(Optional.of(Input.of(input)), Optional.empty(), toValueType);
    }

    public <T> T evaluate(double input, Class<T> toValueType) throws OPAException {
        return evaluateMachinery(Optional.of(Input.of(input)), Optional.empty(), toValueType);
    }

    public <T> T evaluate(java.util.List<Object> input, Class<T> toValueType) throws OPAException {
        return evaluateMachinery(Optional.of(Input.of(input)), Optional.empty(), toValueType);
    }

    public <T> T evaluate(java.lang.Object input, Class<T> toValueType) throws OPAException {
        ObjectMapper om = new ObjectMapper();
        Map<String, Object> iMap = om.convertValue(input, new TypeReference<Map<String, Object>>() {});
        return evaluateMachinery(Optional.of(Input.of(iMap)), Optional.empty(), toValueType);
    }

    // evaluate, but with JavaType toValueTypes

    public <T> T evaluate(String path, java.util.Map<String, Object> input, JavaType toValueType) throws OPAException {
        return evaluateMachinery(Optional.of(Input.of(input)), Optional.of(path), toValueType);
    }

    public <T> T evaluate(String path, String input, JavaType toValueType) throws OPAException {
        return evaluateMachinery(Optional.of(Input.of(input)), Optional.of(path), toValueType);
    }

    public <T> T evaluate(String path, boolean input, JavaType toValueType) throws OPAException {
        return evaluateMachinery(Optional.of(Input.of(input)), Optional.of(path), toValueType);
    }

    public <T> T evaluate(String path, double input, JavaType toValueType) throws OPAException {
        return evaluateMachinery(Optional.of(Input.of(input)), Optional.of(path), toValueType);
    }

    public <T> T evaluate(String path, java.util.List<Object> input, JavaType toValueType) throws OPAException {
        return evaluateMachinery(Optional.of(Input.of(input)), Optional.of(path), toValueType);
    }

    public <T> T evaluate(String path, java.lang.Object input, JavaType toValueType) throws OPAException {
        ObjectMapper om = new ObjectMapper();
        Map<String, Object> iMap = om.convertValue(input, new TypeReference<Map<String, Object>>() {});
        return evaluateMachinery(Optional.of(Input.of(iMap)), Optional.of(path), toValueType);
    }

    // evaluate, but with JavaType toValueTypes and paths elided

    public <T> T evaluate(java.util.Map<String, Object> input, JavaType toValueType) throws OPAException {
        return evaluateMachinery(Optional.of(Input.of(input)), Optional.empty(), toValueType);
    }

    /**
     * Special case where a single string argument is used as the path rather
     * than the input, unlike other single argument overloads.
     *
     * @param path
     * @param toValueType
     * @return
     * @throws OPAException
     */
    public <T> T evaluate(String path, JavaType toValueType) throws OPAException {
        return evaluateMachinery(defaultInput(), Optional.of(path), toValueType);
    }

    public <T> T evaluate(boolean input, JavaType toValueType) throws OPAException {
        return evaluateMachinery(Optional.of(Input.of(input)), Optional.empty(), toValueType);
    }

    public <T> T evaluate(double input, JavaType toValueType) throws OPAException {
        return evaluateMachinery(Optional.of(Input.of(input)), Optional.empty(), toValueType);
    }

    public <T> T evaluate(java.util.List<Object> input, JavaType toValueType) throws OPAException {
        return evaluateMachinery(Optional.of(Input.of(input)), Optional.empty(), toValueType);
    }

    public <T> T evaluate(java.lang.Object input, JavaType toValueType) throws OPAException {
        ObjectMapper om = new ObjectMapper();
        Map<String, Object> iMap = om.convertValue(input, new TypeReference<Map<String, Object>>() {});
        return evaluateMachinery(Optional.of(Input.of(iMap)), Optional.empty(), toValueType);
    }

    // evaluate, but with TypeReference toValueTypes

    public <T> T evaluate(
            String path,
            java.util.Map<String, Object> input,
            TypeReference<T> toValueType) throws OPAException {
        return evaluateMachinery(Optional.of(Input.of(input)), Optional.of(path), toValueType);
    }

    public <T> T evaluate(String path, String input, TypeReference<T> toValueType) throws OPAException {
        return evaluateMachinery(Optional.of(Input.of(input)), Optional.of(path), toValueType);
    }

    public <T> T evaluate(String path, boolean input, TypeReference<T> toValueType) throws OPAException {
        return evaluateMachinery(Optional.of(Input.of(input)), Optional.of(path), toValueType);
    }

    public <T> T evaluate(String path, double input, TypeReference<T> toValueType) throws OPAException {
        return evaluateMachinery(Optional.of(Input.of(input)), Optional.of(path), toValueType);
    }

    public <T> T evaluate(String path, java.util.List<Object> input, TypeReference<T> toValueType) throws OPAException {
        return evaluateMachinery(Optional.of(Input.of(input)), Optional.of(path), toValueType);
    }

    public <T> T evaluate(String path, java.lang.Object input, TypeReference<T> toValueType) throws OPAException {
        ObjectMapper om = new ObjectMapper();
        Map<String, Object> iMap = om.convertValue(input, new TypeReference<Map<String, Object>>() {});
        return evaluateMachinery(Optional.of(Input.of(iMap)), Optional.of(path), toValueType);
    }

    // evaluate with TypeReference toTypeValues and paths elided

    public <T> T evaluate(
            java.util.Map<String, Object> input,
            TypeReference<T> toValueType) throws OPAException {
        return evaluateMachinery(Optional.of(Input.of(input)), Optional.empty(), toValueType);
    }

    /**
     * Special case where a single string argument is used as the path rather
     * than the input, unlike other single argument overloads.
     *
     * @param path
     * @param toValueType
     * @return
     * @throws OPAException
     */
    public <T> T evaluate(String path, TypeReference<T> toValueType) throws OPAException {
        return evaluateMachinery(defaultInput(), Optional.of(path), toValueType);
    }

    public <T> T evaluate(boolean input, TypeReference<T> toValueType) throws OPAException {
        return evaluateMachinery(Optional.of(Input.of(input)), Optional.empty(), toValueType);
    }

    public <T> T evaluate(double input, TypeReference<T> toValueType) throws OPAException {
        return evaluateMachinery(Optional.of(Input.of(input)), Optional.empty(), toValueType);
    }

    public <T> T evaluate(java.util.List<Object> input, TypeReference<T> toValueType) throws OPAException {
        return evaluateMachinery(Optional.of(Input.of(input)), Optional.empty(), toValueType);
    }

    public <T> T evaluate(java.lang.Object input, TypeReference<T> toValueType) throws OPAException {
        ObjectMapper om = new ObjectMapper();
        Map<String, Object> iMap = om.convertValue(input, new TypeReference<Map<String, Object>>() {});
        return evaluateMachinery(Optional.of(Input.of(iMap)), Optional.empty(), toValueType);
    }

    // omit the toTypeValue and try to be smart

    public <T> T evaluate(String path, java.util.Map<String, Object> input) throws OPAException {
        return evaluateMachinery(Optional.of(Input.of(input)), Optional.of(path));
    }

    public <T> T evaluate(String path, String input) throws OPAException {
        return evaluateMachinery(Optional.of(Input.of(input)), Optional.of(path));
    }

    public <T> T evaluate(String path, boolean input) throws OPAException {
        return evaluateMachinery(Optional.of(Input.of(input)), Optional.of(path));
    }

    public <T> T evaluate(String path, double input) throws OPAException {
        return evaluateMachinery(Optional.of(Input.of(input)), Optional.of(path));
    }

    public <T> T evaluate(String path, java.util.List<Object> input) throws OPAException {
        return evaluateMachinery(Optional.of(Input.of(input)), Optional.of(path));
    }

    public <T> T evaluate(String path, java.lang.Object input) throws OPAException {
        ObjectMapper om = new ObjectMapper();
        Map<String, Object> iMap = om.convertValue(input, new TypeReference<Map<String, Object>>() {});
        return evaluateMachinery(Optional.of(Input.of(iMap)), Optional.of(path));
    }

    public <T> T evaluate(java.util.Map<String, Object> input) throws OPAException {
        return evaluateMachinery(Optional.of(Input.of(input)), Optional.empty());
    }

    /**
     * Special case where a single string argument is used as the path rather
     * than the input, unlike other single argument overloads.
     *
     * @param path
     * @return
     * @throws OPAException
     */
    public <T> T evaluate(String path) throws OPAException {
        return evaluateMachinery(defaultInput(), Optional.of(path));
    }

    public <T> T evaluate(boolean input) throws OPAException {
        return evaluateMachinery(defaultInput(), Optional.empty());
    }

    public <T> T evaluate(double input) throws OPAException {
        return evaluateMachinery(defaultInput(), Optional.empty());
    }

    public <T> T evaluate(java.util.List<Object> input) throws OPAException {
        return evaluateMachinery(defaultInput(), Optional.empty());
    }

    public <T> T evaluate(java.lang.Object input) throws OPAException {
        ObjectMapper om = new ObjectMapper();
        Map<String, Object> iMap = om.convertValue(input, new TypeReference<Map<String, Object>>() {});
        return evaluateMachinery(Optional.of(Input.of(iMap)), Optional.empty());
    }

    private ExecutePolicyWithInputRequest makeRequestForEvaluateWithInput(Input input, String path) {
        return ExecutePolicyWithInputRequest.builder()
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
    }

    private ExecutePolicyRequest makeRequestForEvaluate(String path) {
        return ExecutePolicyRequest.builder()
            .path(path)
            .pretty(policyRequestPretty)
            .provenance(policyRequestProvenance)
            .explain(policyRequestExplain)
            .metrics(policyRequestMetrics)
            .instrument(policyRequestInstrument)
            .strictBuiltinErrors(policyRequestStrictBuiltinErrors)
            .build();
    }

    private ExecuteDefaultPolicyWithInputRequest makeRequestForDefaultEvaluate(Input input) {
        return ExecuteDefaultPolicyWithInputRequest.builder()
            .input(input)
            .pretty(policyRequestPretty)
            .build();
    }

    /**
     * This wrapper abstracts over making "default" requests, that is requests
     * that POST to OPA's / in order to access the default policy, as well as
     * requests that have a specific path in /v1/data.
     *
     * At the end of the day, in both of these situations, the end result
     * is either an object containing the successful policy execution, null, or
     * an exception. This avoids having to duplicate this code in each of
     * the overloaded cases of evaluateMachinery().
     *
     * @param input
     * @param path
     * @return
     * @throws OPAException
     */
    private java.lang.Object executePolicy(Optional<Input> input, Optional<String> path) throws OPAException {
        if (path.isPresent() && input.isPresent()) {
            ExecutePolicyWithInputRequest req = makeRequestForEvaluateWithInput(input.get(), path.get());
            ExecutePolicyWithInputResponse res;

            try {
                res = sdk.executePolicyWithInput()
                    .request(req)
                    .call();

            //CHECKSTYLE:OFF
            } catch (Exception e) {
                //CHECKSTYLE:ON
                e.printStackTrace(System.out);
                String msg = String.format(
                    "executing policy at '%s' with failed due to exception '%s'",
                    path,
                    e
                );
                throw new OPAException(msg, e);
            }

            if (res.successfulPolicyResponse().isPresent()) {
                if (res.successfulPolicyResponse().get().result().isPresent()) {
                    Object out = res.successfulPolicyResponse().get().result().get().value();
                    return out;
                } else {
                    String msg = String.format(
                        "executing policy at '%s' succeeded, but OPA did not reply with a result",
                        path
                    );
                    throw new OPAException(msg);
                }
            }

            return null;

        } else if (path.isPresent() && !input.isPresent()) {
            ExecutePolicyRequest req = makeRequestForEvaluate(path.get());
            ExecutePolicyResponse res;

            try {
                res = sdk.executePolicy()
                    .request(req)
                    .call();

            //CHECKSTYLE:OFF
            } catch (Exception e) {
                //CHECKSTYLE:ON
                e.printStackTrace(System.out);
                String msg = String.format("executing policy at '%s' with failed due to exception '%s'", path, e);
                throw new OPAException(msg, e);
            }

            if (res.successfulPolicyResponse().isPresent()) {
                if (res.successfulPolicyResponse().get().result().isPresent()) {
                    Object out = res.successfulPolicyResponse().get().result().get().value();
                    return out;
                } else {
                    String msg = String.format(
                        "executing policy at '%s' succeeded, but OPA did not reply with a result",
                        path
                    );
                    throw new OPAException(msg);
                }
            }

            return null;

        } else {
            if (!input.isPresent()) {
                throw new OPAException("at least one of path or input must be provided");
            }

            ExecuteDefaultPolicyWithInputResponse res;

            try {
                res = sdk.executeDefaultPolicyWithInput(
                    Optional.of(
                        policyRequestPretty),
                        Optional.empty(),
                        input.get()
                );

            //CHECKSTYLE:OFF
            } catch (Exception e) {
                //CHECKSTYLE:ON
                String msg = String.format("executing default policy with failed due to exception '%s'", e);
                throw new OPAException(msg, e);
            }

            if (res.result().isPresent()) {
                Object out = res.result().get().value();
                return out;
            }

            return null;
        }
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
    private <T> T evaluateMachinery(Optional<Input> input, Optional<String> path) throws OPAException {
        Object out = executePolicy(input, path);
        if (out != null) {
            ObjectMapper mapper = new ObjectMapper();
            T typedResult = mapper.convertValue(out, new TypeReference<T>() {});
            return typedResult;
        } else {
            return null;
        }
    }

    private <T> T evaluateMachinery(
        Optional<Input> input,
        Optional<String> path,
        Class<T> toValueType
    ) throws OPAException {
        Object out = executePolicy(input, path);
        if (out != null) {
            ObjectMapper mapper = new ObjectMapper();
            T typedResult = mapper.convertValue(out, toValueType);
            return typedResult;
        } else {
            return null;
        }
    }

    private <T> T evaluateMachinery(
        Optional<Input> input,
        Optional<String> path,
        JavaType toValueType
    ) throws OPAException {
        Object out = executePolicy(input, path);
        if (out != null) {
            ObjectMapper mapper = new ObjectMapper();
            T typedResult = mapper.convertValue(out, toValueType);
            return typedResult;
        } else {
            return null;
        }
    }

    private <T> T evaluateMachinery(
        Optional<Input> input,
        Optional<String> path,
        TypeReference<T> toValueType
    ) throws OPAException {
        Object out = executePolicy(input, path);
        if (out != null) {
            ObjectMapper mapper = new ObjectMapper();
            T typedResult = mapper.convertValue(out, toValueType);
            return typedResult;
        } else {
            return null;
        }
    }

    /**
     * This method performs a scalar policy evaluation similar to evaluate(),
     * but does not immediately handle the result, instead packing it into an
     * OPAResult similar to how the batch evaluation methods work. This means
     * that final output type conversion as well as throwing of any exceptions
     * that occurred during policy evaluation is deferred until OPAResult.get()
     * is used.
     *
     * @param path
     * @param input
     * @return
     */
    public OPAResult evaluateDeferred(
            String path,
            java.util.Map<String, Object> input) {
        return evaluateMachineryDeferred(Optional.of(Input.of(input)), Optional.of(path));
    }

    public OPAResult evaluateDeferred(String path, String input) {
        return evaluateMachineryDeferred(Optional.of(Input.of(input)), Optional.of(path));
    }

    public OPAResult evaluateDeferred(String path, boolean input) {
        return evaluateMachineryDeferred(Optional.of(Input.of(input)), Optional.of(path));
    }

    public OPAResult evaluateDeferred(String path, double input) {
        return evaluateMachineryDeferred(Optional.of(Input.of(input)), Optional.of(path));
    }

    public OPAResult evaluateDeferred(String path, java.util.List<Object> input) {
        return evaluateMachineryDeferred(Optional.of(Input.of(input)), Optional.of(path));
    }

    public OPAResult evaluateDeferred(String path, Object input) {
        ObjectMapper om = new ObjectMapper();
        Map<String, Object> iMap = om.convertValue(input, new TypeReference<Map<String, Object>>() {});
        return evaluateMachineryDeferred(Optional.of(Input.of(iMap)), Optional.of(path));
    }

    public OPAResult evaluateDeferred(
            java.util.Map<String, Object> input) {
        return evaluateMachineryDeferred(Optional.of(Input.of(input)), Optional.empty());
    }

    private OPAResult evaluateMachineryDeferred(Optional<Input> input, Optional<String> path) {
        try {
            Object out = executePolicy(input, path);
            return new OPAResult(out);
        } catch (OPAException e) {
            return new OPAResult(null, e);
        }
    }

    /**
     * Shorthand for evaluateBatch(path, input, true).
     *
     * @param path
     * @param input
     * @return
     * @throws OPAException
     */
    public Map<String, OPAResult> evaluateBatch(String path, Map<String, Object> input) throws OPAException {
        return evaluateBatch(path, input, true);
    }

    /**
     * Evaluate a batch of multiple different inputs and paths at once using
     * Enterprise OPA's batch API. If this method is used while connected to a
     * standard OPA, it will transparently fall back to sequential scalar
     * evaluation calls.
     *
     * @param path
     * @param input
     * @param rejectMixed if true, any request in the batch failing causes an exception to be thrown immediately
     * @return
     * @throws OPAException
     */
    public Map<String, OPAResult> evaluateBatch(
        String path,
        Map<String, Object> input,
        boolean rejectMixed
    ) throws OPAException {
        Map<String, Input> iMap = new HashMap<String, Input>();
        ObjectMapper om = new ObjectMapper();
        for (Map.Entry<String, Object> entry : input.entrySet()) {
            Input converted = Input.of(om.convertValue(entry.getValue(), new TypeReference<Map<String, Object>>() {}));
            iMap.put(entry.getKey(), converted);
        }

        return executePolicyBatch(iMap, path, rejectMixed);
    }

    private Map<String, OPAResult> executePolicyBatch(
        Map<String, Input> inputs,
        String path,
        boolean rejectMixed
    ) throws OPAException {

        if (this.enableBatchFallback) {
            return this.executePolicyBatchFallback(inputs, path, rejectMixed);
        }

        ExecuteBatchPolicyWithInputRequestBody rb = new ExecuteBatchPolicyWithInputRequestBody(inputs);
        ExecuteBatchPolicyWithInputRequest req = new ExecuteBatchPolicyWithInputRequest(path, rb);
        ExecuteBatchPolicyWithInputResponse resp = null;

        try {
            resp = sdk.executeBatchPolicyWithInput(req);
        //CHECKSTYLE:OFF
        } catch (Exception e) {
            if (e instanceof SDKError) {
                if (((SDKError) e).code() == 404) {
                    this.enableBatchFallback = true;
                    return this.executePolicyBatchFallback(inputs, path, rejectMixed);
                }
            }

            //CHECKSTYLE:ON
            String msg = String.format(
                "batch executing policy at '%s' with failed due to exception '%s'",
                path,
                e
            );
            throw new OPAException(msg, e);
        }

        Map<String, OPAResult> out = new HashMap<String, OPAResult>();
        Optional<BatchMixedResults> mrbox = resp.batchMixedResults();
        Optional<BatchSuccessfulPolicyEvaluation> sbox = resp.batchSuccessfulPolicyEvaluation();

        if (mrbox.isPresent() && mrbox.get().responses().isPresent()) {
            Optional<Map<String, Responses>> respsbox = mrbox.get().responses();
            Map<String, Responses> resps = respsbox.get();

            for (Map.Entry<String, Responses> entry : resps.entrySet()) {
                Object responsesValue = entry.getValue();
                if (
                        !(responsesValue instanceof ServerErrorWithStatusCode)
                        && !(responsesValue instanceof SuccessfulPolicyResponseWithStatusCode)
                ) {
                    // If this ever happens, then the SE-generated code has
                    // changed in an incompatible way.
                    throw new OPAException(
                        String.format(
                            "unexpected response type '%s', this should never happen",
                            responsesValue.getClass().getSimpleName()
                        )
                    );
                }

                if (responsesValue instanceof ServerErrorWithStatusCode && rejectMixed) {
                    throw new OPAException(
                        "OPA error in batch response",
                        convertStatusCodeError((ServerErrorWithStatusCode) responsesValue)
                    );
                }

                if (responsesValue instanceof ServerErrorWithStatusCode && !rejectMixed) {
                    out.put(
                        entry.getKey(),
                        new OPAResult(
                            null,
                            new OPAException(
                                "OPA error in batch response",
                                convertStatusCodeError((ServerErrorWithStatusCode) responsesValue)
                            )
                        )
                    );
                    continue;
                }

                if (responsesValue instanceof SuccessfulPolicyResponseWithStatusCode) {
                    Optional<Result> resultBox = ((SuccessfulPolicyResponseWithStatusCode) responsesValue).result();
                    if (!resultBox.isPresent()) {
                        continue;
                    }
                    Object resultValue = resultBox.get().value();
                    out.put(entry.getKey(), new OPAResult(resultValue));
                }
            }
        }

        if (sbox.isPresent() && sbox.get().responses().isPresent()) {
            Optional<Map<String, SuccessfulPolicyResponse>> respsbox = sbox.get().responses();
            Map<String, SuccessfulPolicyResponse> resps = respsbox.get();

            for (Map.Entry<String, SuccessfulPolicyResponse> entry : resps.entrySet()) {
                Optional<Result> resultBox = entry.getValue().result();
                if (!resultBox.isPresent()) {
                    continue;
                }
                Object resultValue = resultBox.get().value();
                out.put(entry.getKey(), new OPAResult(resultValue));
            }
        }

        return out;
    }

    private Map<String, OPAResult> executePolicyBatchFallback(
        Map<String, Input> inputs,
        String path, boolean rejectMixed
    ) throws OPAException {
        Map<String, OPAResult> out = new HashMap<String, OPAResult>();

        for (Map.Entry<String, Input> entry : inputs.entrySet()) {
            try {
                Object result = this.executePolicy(Optional.of(entry.getValue()), Optional.of(path));
                out.put(entry.getKey(), new OPAResult(result));
            } catch (OPAException e) {
                if (rejectMixed) {
                    throw new OPAException(String.format("OPA error in batch response (fallback mode)", e));
                }
                out.put(entry.getKey(), new OPAResult(null, e));
            }
        }

        return out;
    }

    private static Optional<ServerErrorLocation> convertErrorLocation(Optional<ResponsesLocation> loc) {
        if (loc.isPresent()) {
            return Optional.of(new ServerErrorLocation(loc.get().file(), loc.get().row(), loc.get().col()));
        } else {
            return Optional.empty();
        }
    }

    private static Optional<List<ServerErrorErrors>> convertErrorList(Optional<List<ResponsesErrors>> errs) {
        if (errs.isPresent()) {
            ArrayList<ServerErrorErrors> out = new ArrayList<ServerErrorErrors>();
            for (ResponsesErrors e : errs.get()) {
                out.add(new ServerErrorErrors(e.code(), e.message(), convertErrorLocation(e.location())));
            }
            return Optional.of(out);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Creates a ServerError from a ServerErrorWithStatusCode, because the
     * former is throwable and the latter is not.
     *
     * @param err
     * @return
     */
    private static ServerError convertStatusCodeError(ServerErrorWithStatusCode err) {
        return new ServerError(
            err.code(),
            err.message(),
            convertErrorList(err.errors()),
            err.decisionId()
        );
    }

}
