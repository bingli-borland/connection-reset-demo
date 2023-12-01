package com.bes.enterprise.actuator;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.common.TextFormat;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.boot.actuate.metrics.export.prometheus.PrometheusScrapeEndpoint;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


@WebEndpoint(id = "prometheus")
public class BesPrometheusScrapeEndpoint extends PrometheusScrapeEndpoint {
    private static Lock lock = new ReentrantLock();
    private final CollectorRegistry collectorRegistry;

    public BesPrometheusScrapeEndpoint(CollectorRegistry collectorRegistry) {
        super(collectorRegistry);
        this.collectorRegistry = collectorRegistry;
    }

    @ReadOperation(produces = TextFormat.CONTENT_TYPE_004)
    public String scrape() {
        lock.lock();
        try {
            Writer writer = new StringWriter();
            TextFormat.write004(writer, this.collectorRegistry.metricFamilySamples());
            return writer.toString();
        } catch (IOException ex) {
            // This actually never happens since StringWriter::write() doesn't throw any
            // IOException
            throw new RuntimeException("Writing metrics failed", ex);
        } finally {
            lock.unlock();
        }
    }

}
