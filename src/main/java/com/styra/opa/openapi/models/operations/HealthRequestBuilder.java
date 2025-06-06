/* 
 * Code generated by Speakeasy (https://speakeasy.com). DO NOT EDIT.
 */
package com.styra.opa.openapi.models.operations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.styra.opa.openapi.utils.LazySingletonValue;
import com.styra.opa.openapi.utils.Utils;
import java.lang.Boolean;
import java.lang.Exception;
import java.lang.String;
import java.util.List;
import java.util.Optional;

public class HealthRequestBuilder {

    private Optional<Boolean> bundles = Utils.readDefaultOrConstValue(
                            "bundles",
                            "false",
                            new TypeReference<Optional<Boolean>>() {});
    private Optional<Boolean> plugins = Utils.readDefaultOrConstValue(
                            "plugins",
                            "false",
                            new TypeReference<Optional<Boolean>>() {});
    private Optional<? extends List<String>> excludePlugin = Optional.empty();
    private final SDKMethodInterfaces.MethodCallHealth sdk;

    public HealthRequestBuilder(SDKMethodInterfaces.MethodCallHealth sdk) {
        this.sdk = sdk;
    }
                
    public HealthRequestBuilder bundles(boolean bundles) {
        Utils.checkNotNull(bundles, "bundles");
        this.bundles = Optional.of(bundles);
        return this;
    }

    public HealthRequestBuilder bundles(Optional<Boolean> bundles) {
        Utils.checkNotNull(bundles, "bundles");
        this.bundles = bundles;
        return this;
    }
                
    public HealthRequestBuilder plugins(boolean plugins) {
        Utils.checkNotNull(plugins, "plugins");
        this.plugins = Optional.of(plugins);
        return this;
    }

    public HealthRequestBuilder plugins(Optional<Boolean> plugins) {
        Utils.checkNotNull(plugins, "plugins");
        this.plugins = plugins;
        return this;
    }
                
    public HealthRequestBuilder excludePlugin(List<String> excludePlugin) {
        Utils.checkNotNull(excludePlugin, "excludePlugin");
        this.excludePlugin = Optional.of(excludePlugin);
        return this;
    }

    public HealthRequestBuilder excludePlugin(Optional<? extends List<String>> excludePlugin) {
        Utils.checkNotNull(excludePlugin, "excludePlugin");
        this.excludePlugin = excludePlugin;
        return this;
    }

    public HealthResponse call() throws Exception {
        if (bundles == null) {
            bundles = _SINGLETON_VALUE_Bundles.value();
        }
        if (plugins == null) {
            plugins = _SINGLETON_VALUE_Plugins.value();
        }
        return sdk.health(
            bundles,
            plugins,
            excludePlugin);
    }

    private static final LazySingletonValue<Optional<Boolean>> _SINGLETON_VALUE_Bundles =
            new LazySingletonValue<>(
                    "bundles",
                    "false",
                    new TypeReference<Optional<Boolean>>() {});

    private static final LazySingletonValue<Optional<Boolean>> _SINGLETON_VALUE_Plugins =
            new LazySingletonValue<>(
                    "plugins",
                    "false",
                    new TypeReference<Optional<Boolean>>() {});
}
