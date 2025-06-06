/* 
 * Code generated by Speakeasy (https://speakeasy.com). DO NOT EDIT.
 */
package com.styra.opa.openapi.models.operations;

import com.styra.opa.openapi.utils.Utils;
import java.lang.Exception;

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
