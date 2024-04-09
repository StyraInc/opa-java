package com.styra.opa;

import com.styra.opa.OPA;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.containers.BindMode;

import java.util.Map;
import static java.util.Map.entry;

@Testcontainers

class OPATest {

    private String address;

    @Container
    public GenericContainer opac = new GenericContainer(DockerImageName.parse("openpolicyagent/opa:latest"))
        .withExposedPorts(8181)
        .withFileSystemBind("./testdata/simple", "/policy", BindMode.READ_ONLY)
        .withCommand("run -s --bundle /policy");

    @BeforeEach
    public void setUp() {
        address = "http://" + opac.getHost() + ":" + opac.getMappedPort(8181);
    }

    @Test
    public void testOPAHealth() {
        // This test just makes sure that we can reach the OPA health endpoint
        // and that it returns the expected {} value.

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder().uri(URI.create(address + "/health")).build();
        HttpResponse<String> resp = null;

        try {
            resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.out.println("exception: " + e);
            assertNull(e);
        }

        String body = resp.body();

        assertEquals("{}\n", body);
    }

    @Test
    public void testOPAHello() {
        OPA opa = new OPA(address);
        String result = "";

        try {
            result = opa.query(Map.ofEntries(
                entry("user", "alice"),
                entry("x", 8)
            ), "policy/hello");
        } catch (Exception e) {
            System.out.println("exception: " + e);
            assertNull(e);
        }

        assertEquals("Open Policy Agent", result);
    }

    @Test
    public void testOPACheck() {
        OPA opa = new OPA(address);
        boolean result = false;

        try {
            result = opa.check(Map.ofEntries(
                entry("user", "alice"),
                entry("x", 8)
            ), "policy/user_is_alice");
        } catch (Exception e) {
            System.out.println("exception: " + e);
            assertNull(e);
        }

        assertEquals(true, result);

        try {
            result = opa.check(Map.ofEntries(
                entry("user", "bob"),
                entry("x", 8)
            ), "policy/user_is_alice");
        } catch (Exception e) {
            System.out.println("exception: " + e);
            assertNull(e);
        }

        assertEquals(false, result);
    }

    @Test
    public void testOPANumeric() {
        OPA opa = new OPA(address);
        double result = 0;

        try {
            result = opa.query(Map.ofEntries(
                entry("user", "alice"),
                entry("x", 8)
            ), "policy/input_x_times_2");
        } catch (Exception e) {
            System.out.println("exception: " + e);
            assertNull(e);
        }

        assertEquals(16, result);
    }

}
