---
sidebar_position: 1
sidebar_label: Troubleshooting
title: How To Troubleshoot the OPA Java SDK
---


# How to troubleshoot the OPA Java SDK


## OPA Connectivity Issues


If the SDK is unable to connect to OPA properly, you may see exceptions similar to the ones below:

```javastacktrace
java.net.ConnectException
      at java.net.http/jdk.internal.net.http.HttpClientImpl.send(HttpClientImpl.java:846)
      at java.net.http/jdk.internal.net.http.HttpClientFacade.send(HttpClientFacade.java:123)
      at com.styra.opa.openapi.utils.SpeakeasyHTTPClient.send(SpeakeasyHTTPClient.java:20)
      at com.styra.opa.openapi.OpaApiClient.executePolicyWithInput(OpaApiClient.java:508)
      at com.styra.opa.openapi.models.operations.ExecutePolicyWithInputRequestBuilder.call(ExecutePolicyWithInputRequestBuilder.java:37)
      at com.styra.opa.OPAClient.executePolicy(OPAClient.java:536)
      at com.styra.opa.OPAClient.evaluateMachinery(OPAClient.java:638)
      at com.styra.opa.OPAClient.evaluate(OPAClient.java:421)
      at com.styra.opa.OPAClient.check(OPAClient.java:119)
      at com.styra.tickethub.TicketHub.authz(TicketHub.java:207)
      at com.styra.tickethub.TicketHub.getTickets(TicketHub.java:65)
      at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:104)
      ...
      at org.eclipse.jetty.util.thread.QueuedThreadPool.runJob(QueuedThreadPool.java:894)
      at org.eclipse.jetty.util.thread.QueuedThreadPool$Runner.run(QueuedThreadPool.java:1038)
      at java.base/java.lang.Thread.run(Thread.java:1589)
Caused by: java.net.ConnectException
      at java.net.http/jdk.internal.net.http.common.Utils.toConnectException(Utils.java:1045)
      at java.net.http/jdk.internal.net.http.PlainHttpConnection.connectAsync(PlainHttpConnection.java:224)
      ...
      at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
      ... 1 more
Caused by: java.nio.channels.ClosedChannelException
      at java.base/sun.nio.ch.SocketChannelImpl.ensureOpen(SocketChannelImpl.java:202)
      at java.base/sun.nio.ch.SocketChannelImpl.beginConnect(SocketChannelImpl.java:786)
      at java.base/sun.nio.ch.SocketChannelImpl.connect(SocketChannelImpl.java:874)
      at java.net.http/jdk.internal.net.http.PlainHttpConnection.lambda$connectAsync$1(PlainHttpConnection.java:208)
      at java.base/java.security.AccessController.doPrivileged(AccessController.java:569)
      at java.net.http/jdk.internal.net.http.PlainHttpConnection.connectAsync(PlainHttpConnection.java:210)
      ... 9 more
```

If you encounter these types of errors, this typically indicates that the SDK was not able to reach OPA. This can have several causes:

- OPA is running, but the OPA URL the SDK is using is not correct.
- OPA is not running. You may have forgotten to start it, or it may have failed to start due to a bad configuration, syntax errors in your policy, etc.
- OPA is running and the OPA URL is configured correctly, but a network problem is preventing the SDK from communicating with OPA.


## Input / Output Schema Issues


If there is a problem with the structure of your policy input or output, it can cause errors similar to the ones below.

This exception was caused by the rule head `tickets/allow` was undefined with the input provided to the request. The SDK treats such situations as an error condition.

```javastacktrace
com.styra.opa.OPAException: executing policy at 'Optional[tickets/allow]' succeeded, but OPA did not reply with a result
      at com.styra.opa.OPAClient.executePolicy(OPAClient.java:559)
      at com.styra.opa.OPAClient.evaluateMachinery(OPAClient.java:638)
      at com.styra.opa.OPAClient.evaluate(OPAClient.java:421)
      at com.styra.opa.OPAClient.check(OPAClient.java:119)
      at com.styra.tickethub.TicketHub.authz(TicketHub.java:206)
      at com.styra.tickethub.TicketHub.getTickets(TicketHub.java:64)
      at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:104)
      at java.base/java.lang.reflect.Method.invoke(Method.java:578)
      at org.glassfish.jersey.server.model.internal.ResourceMethodInvocationHandlerFactory.lambda$static$0(ResourceMethodInvocationHandlerFactory.java:52)
      ...
      at org.eclipse.jetty.util.thread.QueuedThreadPool$Runner.run(QueuedThreadPool.java:1038)
      at java.base/java.lang.Thread.run(Thread.java:1589)
```

This exception was caused by a rule head `nonexistant/path` which was not defined in the policy at all. Observe that this looks the same as the example above. Considering a rule head not existing, as opposed being conditionally defined based on the `input` object -- these two situations may manifest the same from the perspective of the SDK, but the latter case may appear "flaky" depending on how often the application encounters an input for which the configured rule head is defined.

```javastacktrace
com.styra.opa.OPAException: executing policy at 'Optional[nonexistant/path]' succeeded, but OPA did not reply with a result
      at com.styra.opa.OPAClient.executePolicy(OPAClient.java:559)
      at com.styra.opa.OPAClient.evaluateMachinery(OPAClient.java:638)
      at com.styra.opa.OPAClient.evaluate(OPAClient.java:421)
      at com.styra.opa.OPAClient.check(OPAClient.java:119)
      at com.styra.tickethub.TicketHub.authz(TicketHub.java:207)
      at com.styra.tickethub.TicketHub.getTickets(TicketHub.java:64)
      at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:104)
      at java.base/java.lang.reflect.Method.invoke(Method.java:578)
      ...
      at org.eclipse.jetty.util.thread.QueuedThreadPool$Runner.run(QueuedThreadPool.java:1038)
      at java.base/java.lang.Thread.run(Thread.java:1589)
```

This exception was caused by the OPA policy returning an object, when a boolean value was expected.

```javastacktrace
java.lang.ClassCastException: class java.util.LinkedHashMap cannot be cast to class java.lang.Boolean (java.util.LinkedHashMap and java.lang.Boolean are in module java.base of loader 'bootstrap')
      at com.styra.opa.OPAClient.check(OPAClient.java:119)
      at com.styra.tickethub.TicketHub.authz(TicketHub.java:206)
      at com.styra.tickethub.TicketHub.getTickets(TicketHub.java:64)
      at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:104)
      at java.base/java.lang.reflect.Method.invoke(Method.java:578)
      at org.glassfish.jersey.server.model.internal.ResourceMethodInvocationHandlerFactory.lambda$static$0(ResourceMethodInvocationHandlerFactory.java:52)
      at org.glassfish.jersey.server.model.internal.AbstractJavaResourceMethodDispatcher$1.run(AbstractJavaResourceMethodDispatcher.java:146)
      ...
      at org.eclipse.jetty.util.thread.QueuedThreadPool$Runner.run(QueuedThreadPool.java:1038)
      at java.base/java.lang.Thread.run(Thread.java:1589)
```

This exception was caused by the OPA policy returning a boolean when an object was expected.

```javastacktrace
java.lang.ClassCastException: class java.lang.Boolean cannot be cast to class java.util.HashMap (java.lang.Boolean and java.util.HashMap are in module java.base of loader 'bootstrap')
      at com.styra.tickethub.TicketHub.authz(TicketHub.java:207)
      at com.styra.tickethub.TicketHub.getTickets(TicketHub.java:64)
      at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:104)
      at java.base/java.lang.reflect.Method.invoke(Method.java:578)
      at org.glassfish.jersey.server.model.internal.ResourceMethodInvocationHandlerFactory.lambda$static$0(ResourceMethodInvocationHandlerFactory.java:52)
      at org.glassfish.jersey.server.model.internal.AbstractJavaResourceMethodDispatcher$1.run(AbstractJavaResourceMethodDispatcher.java:146)
      ...
      at org.eclipse.jetty.util.thread.QueuedThreadPool$Runner.run(QueuedThreadPool.java:1038)
      at java.base/java.lang.Thread.run(Thread.java:1589)
```

The exact errors you may see due to input/output schema problems can vary dramatically, depending on the type that the policy returned compared to what the Java code was expecting.

Some areas you should investigate if you are seeing this type of problem include:

- Check that the input to your OPA policy is correct. Many policies can evaluate to undefined or other unexpected results if the input is malformed.
- Ensure that the output from your OPA policy matches the Java type you are trying to assign it to.
- Make certain the `toValueType` parameter to your `evaluate()` call correctly reflects the type you are assigning `evaluate()`'s return to.
- If you are calling `evaluate()` without an explicit `toValueType`, try adding one. Type erasure can cause deserialization to fail when `evaluate()` is called in this way.

:::tip
An easy way to have OPA dump human-readable decision logs to the console is to add the `--log-format=text --log-level=error --set decision_logs.console=true` arguments to your `opa run -s` command. This is especially useful for checking the inputs OPA is seeing, and the responses it is yielding for them.
:::
