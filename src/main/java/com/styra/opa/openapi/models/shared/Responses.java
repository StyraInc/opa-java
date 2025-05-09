/* 
 * Code generated by Speakeasy (https://speakeasy.com). DO NOT EDIT.
 */
package com.styra.opa.openapi.models.shared;

import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.lang.String;

@JsonTypeInfo(use = Id.NAME, property = "http_status_code", include = As.EXISTING_PROPERTY, visible = true)
@JsonSubTypes({
    @Type(value = SuccessfulPolicyResponseWithStatusCode.class, name="200"),
    @Type(value = ServerErrorWithStatusCode.class, name="500")})
public interface Responses {

    String httpStatusCode();

}

