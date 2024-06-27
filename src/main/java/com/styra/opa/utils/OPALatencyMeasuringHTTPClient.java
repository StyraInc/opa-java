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
 * This HTTPClient implementation wraps OPAHTTPClient. In addition to
 * (optionally) injecting extra headers into the requests, it can also send log
 * messages to a java.util.logging.Logger instances with latency measurements.
 */
public class OPALatencyMeasuringHTTPClient extends OPAHTTPClient {

    private Logger logger;

    private String latencyMeasurementFormatString = "path=''{1}'' latency={0,number,#}ms";

    private MessageFormat fmt;

    private Level latencyMeasurementLogLevel = FINE;

    public OPALatencyMeasuringHTTPClient(Logger logger) {
        super();
        this.logger = logger;
    }

    /**
     * This constructor allows providing additional headers that should be
     * passed to OPAHTTPClient.
     *
     * @param logger
     * @param headers
     * @param headers
     */
    public OPALatencyMeasuringHTTPClient(Logger logger, Map<String, String> headers) {
        super(headers);
        this.logger = logger;
        this.fmt = new MessageFormat(this.latencyMeasurementFormatString);
    }

    /**
     * Modify the format in which the latency measurements are logged.
     *
     * The format string should be compatible with java.text.MessageFormat.
     * The {0} argument will contain the measured request latency in ms, and
     * the {1} argument will contain the URL path for the HTTP request. The
     * default format is  "path=''{1}'' latency={0,number,#}ms"
     *
     * @param newFormat
     */
    public void setLatencyMeasurementFormat(String newFormat) {
        this.latencyMeasurementFormatString = newFormat;
        this.fmt = new MessageFormat(this.latencyMeasurementFormatString);
    }

    /**
     * Modify the log level at which latency measurements are recorded.
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

        long sendLatency = endTime - startTime;

        Object[] logArgs = {sendLatency, path};

        logger.log(latencyMeasurementLogLevel, fmt.format(logArgs));

        return response;
    }
}

