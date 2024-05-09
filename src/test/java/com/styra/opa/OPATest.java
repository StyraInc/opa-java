package com.styra.opa;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Testcontainers

class OPATest {

    private int opaPort = 8181;

    // Checkstyle does not like magic numbers, but these are just test values.
    // The B value should be double the A value.
    private int testIntegerA = 8;
    private int testIntegerB = 16;
    private double testDoubleA = 3.14159;

    private String address;
    private Map<String, String> headers = Map.ofEntries(entry("Authorization", "Bearer supersecret"));

    @Container
    // Checkstyle is disabled here because it wants opac to 'be private and
    // have accessor methods', which seems pointless and will probably mess up
    // test containers.
    //
    // Checkstyle also complains that this is in the wrong order, because public
    // variables are supposed to be declared first. But then it would need to
    // have magic numbers since opaPort and friends are private.
    //CHECKSTYLE:OFF
    public GenericContainer opac = new GenericContainer(DockerImageName.parse("openpolicyagent/opa:latest"))
        .withExposedPorts(opaPort)
        .withFileSystemBind("./testdata/simple", "/policy", BindMode.READ_ONLY)
        .withCommand("run -s --authentication=token --authorization=basic --bundle /policy");
    //CHECKSTYLE:ON

    @BeforeEach
    public void setUp() {
        address = "http://" + opac.getHost() + ":" + opac.getMappedPort(opaPort);
    }

    @Test
    public void testOPAHealth() {
        // This test just makes sure that we can reach the OPAClient health endpoint
        // and that it returns the expected {} value.

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder().uri(URI.create(address + "/health")).build();
        HttpResponse<String> resp = null;

        try {
            resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        // This is a unit test, I will catch whatever exceptions I want.
        //CHECKSTYLE:OFF
        } catch (Exception e) {
            //CHECKSTYLE:ON
            System.out.println("exception: " + e);
            assertNull(e);
        }

        String body = resp.body();

        assertEquals("{}\n", body);
    }

    @Test
    public void testOPAHello() {
        OPAClient opa = new OPAClient(address, headers);
        String result = "";

        try {
            result = opa.evaluate("policy/hello", Map.ofEntries(
                entry("user", "alice"),
                entry("x", testIntegerA)
            ));
        } catch (OPAException e) {
            System.out.println("exception: " + e);
            assertNull(e);
        }

        assertEquals("Open Policy Agent", result);
    }

    @Test
    public void testOPACheck() {
        OPAClient opa = new OPAClient(address, headers);
        boolean result = false;

        try {
            result = opa.check("policy/user_is_alice", Map.ofEntries(
                entry("user", "alice"),
                entry("x", testIntegerA)
            ));
        } catch (OPAException e) {
            System.out.println("exception: " + e);
            assertNull(e);
        }

        assertEquals(true, result);

        try {
            result = opa.check("policy/user_is_alice", Map.ofEntries(
                entry("user", "bob"),
                entry("x", testIntegerA)
            ));
        } catch (OPAException e) {
            System.out.println("exception: " + e);
            assertNull(e);
        }

        assertEquals(false, result);
    }

    @Test
    public void testOPANumeric() {
        OPAClient opa = new OPAClient(address, headers);
        double result = 0;

        try {
            result = opa.evaluate("policy/input_x_times_2", Map.ofEntries(
                entry("user", "alice"),
                entry("x", testIntegerA)
            ));
        } catch (OPAException e) {
            System.out.println("exception: " + e);
            assertNull(e);
        }

        assertEquals(testIntegerB, result);
    }

    @Test
    public void testOPAWithoutInput() {
        OPAClient opa = new OPAClient(address, headers);
        Map result = Map.ofEntries(entry("unit", "test"));

        try {
            result = opa.evaluate("policy/echo");
        } catch (OPAException e) {
            System.out.println("exception: " + e);
            assertNull(e);
        }

        assertEquals(Map.ofEntries(), result);
    }

    @Test
    public void testOPAEcho() {
        OPAClient opa = new OPAClient(address, headers);
        Map result = Map.ofEntries(entry("unit", "test"));
        Map expect = Map.ofEntries(entry("hello", "world"), entry("foo", Map.ofEntries(entry("bar", testIntegerA))));

        try {
            result = opa.evaluate("policy/echo", Map.ofEntries(
                entry("hello", "world"),
                entry("foo", Map.ofEntries(entry("bar", testIntegerA)))
            ));
        } catch (OPAException e) {
            System.out.println("exception: " + e);
            assertNull(e);
        }

        assertEquals(expect, result);
    }

    @Test
    public void testObjectRoundtrip() {
        OPAClient opa = new OPAClient(address, headers);

        Map<String, String> sampleMap1 = Map.ofEntries(entry("hello", "world"));
        Map<String, String> sampleMap2 = Map.ofEntries(entry("hello", "world"));

        SampleObject<Double> input = new SampleObject<Double>();
        input.setBoolProperty(true);
        input.setIntProperty(testIntegerA);
        input.setCustomProperty(testDoubleA);
        input.setAnnotatedProperty(testIntegerB);
        input.setMapProperty(sampleMap1);

        SampleObject<Double> expect = new SampleObject<Double>();
        expect.setBoolProperty(true);
        expect.setIntProperty(testIntegerA);
        expect.setCustomProperty(testDoubleA);
        expect.setAnnotatedProperty(testIntegerB);
        expect.setMapProperty(sampleMap2);

        SampleObject<Double> actual = new SampleObject<Double>();

        try {
            //actual = opa.evaluate("policy/echo", input, new ObjectMapper().constructType(actual.getClass()));
            actual = opa.evaluate("policy/echo", input, new TypeReference<SampleObject<Double>>() {});
        } catch (OPAException e) {
            System.out.println("exception: " + e);
            assertNull(e);
        }

        assertEquals(input.getBoolProperty(), expect.getBoolProperty());
        assertEquals(input.getIntProperty(), expect.getIntProperty());
        assertEquals(input.getCustomProperty(), expect.getCustomProperty());
        assertEquals(input.getAnnotatedProperty(), expect.getAnnotatedProperty());
        assertEquals(input.getMapProperty(), expect.getMapProperty());
    }

    @Test
    public void testObjectRoundtripWithTypeChange() {
        OPAClient opa = new OPAClient(address, headers);

        Map<String, String> sampleMap1 = Map.ofEntries(entry("hello", "world"));
        Map<String, String> sampleMap2 = Map.ofEntries(entry("hello", "world"));

        Map<String, java.lang.Object> expectNestedMap = Map.ofEntries(
            entry("boolProperty", true),
            entry("intProperty", testIntegerA),
            entry("customProperty", testDoubleA),
            entry("customAnnotatedProperty", testIntegerB),
            entry("mapProperty", sampleMap1)
        );

        SampleObject<Double> input = new SampleObject<Double>();
        input.setBoolProperty(true);
        input.setIntProperty(testIntegerA);
        input.setCustomProperty(testDoubleA);
        input.setAnnotatedProperty(testIntegerB);
        input.setMapProperty(sampleMap1);

        AlternateSampleObject expect = new AlternateSampleObject();
        expect.setNestedMap(expectNestedMap);
        expect.setStringVal("hello, test suite!");

        AlternateSampleObject actual = new AlternateSampleObject();

        try {
            actual = opa.evaluate(
                    "policy/makeAlternateSampleClass",
                    input,
                    new TypeReference<AlternateSampleObject>() {}
            );
        } catch (OPAException e) {
            System.out.println("exception: " + e);
            assertNull(e);
        }

        assertEquals(actual.getNestedMap(), expect.getNestedMap());
        assertEquals(actual.getStringVal(), expect.getStringVal());
    }

    @Test
    public void testOPADefaultPathWithInput() {
        OPAClient opa = new OPAClient(address, headers);
        Map<String,java.lang.Object> input = Map.ofEntries(entry("hello", "world"));
        Map result = Map.ofEntries(entry("unit", "test"));
        Map expect = Map.ofEntries(
            entry("msg", "this is the default path"),
            entry("echo", Map.ofEntries(entry("hello", "world")))
        );

        try {
            result = opa.evaluate(input);
        } catch (OPAException e) {
            System.out.println("exception: " + e);
            assertNull(e);
        }

        assertEquals(expect, result);
    }

    @Test
    public void testOPADefaultPathWithoutInput() {
        OPAClient opa = new OPAClient(address, headers);
        Map expect = Map.ofEntries(
            entry("msg", "this is the default path"),
            entry("echo", Map.ofEntries())
        );
        Map result = Map.ofEntries(entry("unit", "test"));

        try {
            result = opa.evaluate();
        } catch (OPAException e) {
            System.out.println("exception: " + e);
            assertNull(e);
        }

        assertEquals(expect, result);
    }


}

