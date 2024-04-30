/* 
 * Code generated by Speakeasy (https://speakeasyapi.dev). DO NOT EDIT.
 */

package com.styra.opa.openapi.models.operations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.styra.opa.openapi.models.errors.SDKError;
import com.styra.opa.openapi.utils.LazySingletonValue;
import com.styra.opa.openapi.utils.Utils;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.stream.Stream;
import org.openapitools.jackson.nullable.JsonNullable;


public class ExecutePolicyWithInputRequestBuilder {

    private ExecutePolicyWithInputRequest request;
    private final SDKMethodInterfaces.MethodCallExecutePolicyWithInput sdk;

    public ExecutePolicyWithInputRequestBuilder(SDKMethodInterfaces.MethodCallExecutePolicyWithInput sdk) {
        this.sdk = sdk;
    }

    public ExecutePolicyWithInputRequestBuilder request(ExecutePolicyWithInputRequest request) {
        Utils.checkNotNull(request, "request");
        this.request = request;
        return this;
    }

    public ExecutePolicyWithInputResponse call() throws Exception {

        return sdk.executePolicyWithInput(
            request);
    }
}