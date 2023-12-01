package com.bes.enterprise.actuator;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.common.TextFormat;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.boot.actuate.metrics.export.prometheus.PrometheusScrapeEndpoint;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Deque;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingDeque;


@WebEndpoint(id = "prometheus")
public class BesPrometheusScrapeEndpoint extends PrometheusScrapeEndpoint {

    private Deque<String> metricCache = new LinkedBlockingDeque<>(5);
    private final CollectorRegistry collectorRegistry;

    public BesPrometheusScrapeEndpoint(CollectorRegistry collectorRegistry, Long period) {
        super(collectorRegistry);
        this.collectorRegistry = collectorRegistry;
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                metricCache.clear();
            }
        }, 100, period);
    }

    @ReadOperation(produces = TextFormat.CONTENT_TYPE_004)
    public String scrape() {
        String metric = metricCache.peek();
        if (metric != null) {
            return metric;
        } else {
            try {
                Writer writer = new StringWriter();
                TextFormat.write004(writer, this.collectorRegistry.metricFamilySamples());
                metric = writer.toString();
                metricCache.offerFirst(metric);
                return metric;
            } catch (IOException ex) {
                // This actually never happens since StringWriter::write() doesn't throw any
                // IOException
                throw new RuntimeException("Writing metrics failed", ex);
            }
        }
    }

}
