package com.styra.opa;

import com.fasterxml.jackson.core.type.TypeReference;
import com.styra.opa.utils.OPALatencyMeasuringHTTPClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.logging.Logger;

import static java.util.Map.entry;
import static java.util.logging.Level.ALL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
class EOPATest {

    private int opaPort = 8181;
    private int altPort = 8282;

    // Checkstyle does not like magic numbers, but these are just test values.
    // The B value should be double the A value.
    private int testIntegerA = 8;
    private int testIntegerB = 16;
    private double testDoubleA = 3.14159;
    private long msInZeroSeconds; // implicit initialization to 0
    private long msInOneSeconds = 1000;

    private String eopaAddress;
    private String eopaAltAddress;
    private Map<String, String> headers = Map.ofEntries(entry("Authorization", "Bearer supersecret"));

    private RecordingHandler recordingHandler;
    private Logger logger;

    @Container
    //CHECKSTYLE:OFF
    public GenericContainer<?> eopac = new GenericContainer<>(
            new ImageFromDockerfile()
                .withFileFromClasspath("Dockerfile", "eopa.Dockerfile")
                .withFileFromClasspath("nginx.conf", "nginx.conf")
                .withFileFromClasspath("entrypoint.sh", "entrypoint.sh")
        )
        .withExposedPorts(opaPort, altPort)
        .withFileSystemBind("./testdata/simple", "/policy", BindMode.READ_ONLY)
        .withCommand("run -s -a 0.0.0.0:8181 --authentication=token --authorization=basic --bundle /policy")
        .withEnv("EOPA_LICENSE_KEY", System.getenv("EOPA_LICENSE_KEY"));
    //CHECKSTYLE:ON

    @BeforeEach
    public void setUp() throws Exception {
        eopaAddress = "http://" + eopac.getHost() + ":" + eopac.getMappedPort(opaPort);
        eopaAltAddress = "http://" + eopac.getHost() + ":" + eopac.getMappedPort(altPort) + "/customprefix";

        this.recordingHandler = new RecordingHandler();
        this.recordingHandler.setLevel(ALL);
        Field httpcLoggerField = OPALatencyMeasuringHTTPClient.class.getDeclaredField("logger");
        httpcLoggerField.setAccessible(true);
        this.logger = (Logger) httpcLoggerField.get(null);
        this.logger.addHandler(this.recordingHandler);
        this.logger.setLevel(ALL);
        this.logger.setUseParentHandlers(false);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("==== container logs from EOPA container ====");
        final String eopalogs = eopac.getLogs();
        System.out.println(eopalogs);

        this.logger.removeHandler(this.recordingHandler);
    }

    @Test
    public void testEOPAHealth() {
        // This test just makes sure that we can reach the OPAClient health endpoint
        // and that it returns the expected {} value.

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder().uri(URI.create(eopaAddress + "/health")).build();
        HttpResponse<String> resp = null;

        try {
            resp = client.send(req, HttpResponse.BodyHandlers.ofString());
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
    public void testEOPAHealthAlternate() {
        // This makes sure that we can also successfully reach the OPA health
        // API on the "alternate", reverse-proxy based OPA that has a URL
        // prefix.

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder().uri(URI.create(eopaAltAddress + "/health")).build();
        HttpResponse<String> resp = null;

        try {
            resp = client.send(req, HttpResponse.BodyHandlers.ofString());
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
    public void testEOPAEvaluateBatch() {
        OPAClient opa = new OPAClient(eopaAddress, headers);
        Map<String, Object> input = Map.ofEntries(
            entry("job1", Map.ofEntries(entry("aaa", "111"))),
            entry("job2", Map.ofEntries(entry("bbb", "222")))
        );
        Map<String, OPAResult> result = Map.ofEntries();
        Map<String, Object> expect = Map.ofEntries(
            entry("job1", Map.ofEntries(entry("aaa", "111"))),
            entry("job2", Map.ofEntries(entry("bbb", "222")))
        );

        try {
            result = opa.evaluateBatch("policy/echo", input);
        } catch (OPAException e) {
            System.out.println("exception: " + e);
            assertNull(e);
        }

        for (Map.Entry<String, OPAResult> entry: result.entrySet()) {
            assertTrue(entry.getValue().success());
            try {
                Map<String, Object> v = entry.getValue().get();
                assertEquals(expect.get(entry.getKey()), v);
            } catch (OPAException e) {
                System.out.println("exception: " + e);
                assertNull(e);
            }
        }
    }

    @Test
    public void testEOPAEvaluateBatchFallback() {
        OPAClient opa = new OPAClient(eopaAddress, headers);
        opa.forceBatchFallback(true);
        Map<String, Object> input = Map.ofEntries(
            entry("job1", Map.ofEntries(entry("aaa", "111"))),
            entry("job2", Map.ofEntries(entry("bbb", "222")))
        );
        Map<String,  OPAResult> result = Map.ofEntries();
        Map<String, Object> expect = Map.ofEntries(
            entry("job1", Map.ofEntries(entry("aaa", "111"))),
            entry("job2", Map.ofEntries(entry("bbb", "222")))
        );

        try {
            result = opa.evaluateBatch("policy/echo", input);
        } catch (OPAException e) {
            System.out.println("exception: " + e);
            assertNull(e);
        }

        for (Map.Entry<String, OPAResult> entry: result.entrySet()) {
            assertTrue(entry.getValue().success());
            try {
                Map<String, Object> v = entry.getValue().get();
                assertEquals(expect.get(entry.getKey()), v);
            } catch (OPAException e) {
                System.out.println("exception: " + e);
                assertNull(e);
            }
        }
    }

    @Test
    public void testEOPAEvaluateBatchMixed() {
        OPAClient opa = new OPAClient(eopaAddress, headers);
        Map<String, Object> input = Map.ofEntries(
            entry("job1", Map.ofEntries(entry("aaa", "111"), entry("bbb", "111"))),
            entry("job2", Map.ofEntries(entry("bbb", "222")))
        );
        Map<String, OPAResult> result = Map.ofEntries();

        Map<String, OPAResult> expect = Map.ofEntries(
            entry("job1", new OPAResult(null, new OPAException("unit test"))),
            entry("job2", new OPAResult(Map.ofEntries(entry("222", "bbb")), null))
        );

        TypeReference<Map<String, Object>> tr = new TypeReference<Map<String, Object>>() {};

        try {
            result = opa.evaluateBatch("condfail/p", input, false);
        } catch (OPAException e) {
            e.printStackTrace(System.out);
            System.out.println("exception: " + e);
            assertNull(e);
        }

        for (Map.Entry<String, OPAResult> entry: result.entrySet()) {
            OPAResult actual = entry.getValue();
            String key = entry.getKey();
            OPAResult expected = expect.get(key);
            assertEquals(actual.success(), expected.success());
            assertEquals(actual.getValue(), expected.getValue());
        }
    }

    @Test
    public void testEOPAEvaluateBatchMixedReject() {
        // Tests a that when rejectMixed=true, the entire batch fails at once.

        OPAClient opa = new OPAClient(eopaAddress, headers);
        Map<String, Object> input = Map.ofEntries(
            entry("job1", Map.ofEntries(entry("aaa", "111"), entry("bbb", "111"))),
            entry("job2", Map.ofEntries(entry("bbb", "222")))
        );
        Map<String, OPAResult> result = Map.ofEntries();

        Map<String, OPAResult> expect = Map.ofEntries(
            entry("job1", new OPAResult(null, new OPAException("unit test"))),
            entry("job2", new OPAResult(Map.ofEntries(entry("222", "bbb")), null))
        );

        TypeReference<Map<String, Object>> tr = new TypeReference<Map<String, Object>>() {};

        OPAException err = null;

        try {
            result = opa.evaluateBatch("condfail/p", input, true);
        } catch (OPAException e) {
            err = e;
        }

        assertTrue(err != null);
    }

}

