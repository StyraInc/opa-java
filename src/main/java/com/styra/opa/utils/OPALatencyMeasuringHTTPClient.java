package com.styra.opa.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.MessageFormat;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Level.FINE;

/**
 * This HTTPClient implementation wraps OPAHTTPClient and has the same
 * functionality, but also creates log messages indicating the latency for each
 * request processed.
 */
public class OPALatencyMeasuringHTTPClient extends OPAHTTPClient {

    // Checkstyle wants this to not be a hard-coded magic number. You never
    // know, maybe the length of a second will change one day?
    private static final double MS_PER_NS = 0.000001;

    private static Logger logger = Logger.getLogger(OPALatencyMeasuringHTTPClient.class.getName());

    private String latencyMeasurementFormatString = "path=''{1}'' latency={0,number,#}ms";

    private MessageFormat fmt;

    private Level latencyMeasurementLogLevel = FINE;

    public OPALatencyMeasuringHTTPClient() {
        super();
        this.fmt = new MessageFormat(this.latencyMeasurementFormatString);
    }

    /**
     * This constructor allows providing additional headers that should be
     * passed to OPAHTTPClient.
     *
     * @param logger
     * @param headers
     * @param headers
     */
    public OPALatencyMeasuringHTTPClient(Map<String, String> headers) {
        super(headers);
        this.fmt = new MessageFormat(this.latencyMeasurementFormatString);
    }

    /**
     * Modify the format in which the latency measurements are logged, the
     * default is "path=''{1}'' latency={0,number,#}ms".
     *
     * The format string should be compatible with java.text.MessageFormat.
     * The {0} argument will contain the measured request latency in ms, and
     * the {1} argument will contain the URL path for the HTTP request.
     *
     * @param newFormat
     */
    public void setLatencyMeasurementFormat(String newFormat) {
        this.latencyMeasurementFormatString = newFormat;
        this.fmt = new MessageFormat(this.latencyMeasurementFormatString);
    }

    /**
     * Modify the log level at which latency measurements are recorded, the
     * default is FINE.
     *
     * @param newLevel
     */
    public void setLatencyMeasurementLogLevel(Level newLevel) {
        this.latencyMeasurementLogLevel = newLevel;
    }

    @Override
    public HttpResponse<InputStream> send(HttpRequest request)
            throws IOException, InterruptedException, URISyntaxException {

        String path = request.uri().getPath();

        long startTime = System.nanoTime();
        HttpResponse<InputStream> response = super.send(request);
        long endTime = System.nanoTime();

        // convert ns -> ms
        double sendLatency = (((double) endTime) - ((double) startTime)) * MS_PER_NS;

        Object[] logArgs = {(long) sendLatency, path};

        logger.log(latencyMeasurementLogLevel, fmt.format(logArgs));

        return response;
    }
}

