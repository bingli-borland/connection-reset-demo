package com.bes.enterprise.actuator;

import io.micrometer.core.instrument.Clock;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.CollectorRegistry;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.export.prometheus.PrometheusMetricsExportAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.export.prometheus.PrometheusProperties;
import org.springframework.boot.actuate.metrics.export.prometheus.PrometheusScrapeEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore({ PrometheusMetricsExportAutoConfiguration.class})
@AutoConfigureAfter(MetricsAutoConfiguration.class)
@ConditionalOnBean(Clock.class)
@ConditionalOnClass(PrometheusMeterRegistry.class)
@ConditionalOnProperty(prefix = "management.metrics.export.prometheus", name = "enabled", havingValue = "true",
        matchIfMissing = true)
@EnableConfigurationProperties(PrometheusProperties.class)
public class BesPrometheusMetricsExportAutoConfiguration {

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnAvailableEndpoint(endpoint = BesPrometheusScrapeEndpoint.class)
    public static class PrometheusScrapeEndpointConfiguration {

        @Bean
        @ConditionalOnMissingBean
        @ConditionalOnProperty(prefix = "management.metrics.export.prometheus.bes", name = "enabled", havingValue = "true",
                matchIfMissing = true)
        public PrometheusScrapeEndpoint prometheusEndpoint(CollectorRegistry collectorRegistry) {
            return new BesPrometheusScrapeEndpoint(collectorRegistry);
        }

    }
}
