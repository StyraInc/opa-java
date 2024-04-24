package com.styra.opa;

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
    private int testNumberA = 8;
    private int testNumberB = 16;

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
            result = opa.query(Map.ofEntries(
                entry("user", "alice"),
                entry("x", testNumberA)
            ), "policy/hello");
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
            result = opa.check(Map.ofEntries(
                entry("user", "alice"),
                entry("x", testNumberA)
            ), "policy/user_is_alice");
        } catch (OPAException e) {
            System.out.println("exception: " + e);
            assertNull(e);
        }

        assertEquals(true, result);

        try {
            result = opa.check(Map.ofEntries(
                entry("user", "bob"),
                entry("x", testNumberA)
            ), "policy/user_is_alice");
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
            result = opa.query(Map.ofEntries(
                entry("user", "alice"),
                entry("x", testNumberA)
            ), "policy/input_x_times_2");
        } catch (OPAException e) {
            System.out.println("exception: " + e);
            assertNull(e);
        }

        assertEquals(testNumberB, result);
    }

}
