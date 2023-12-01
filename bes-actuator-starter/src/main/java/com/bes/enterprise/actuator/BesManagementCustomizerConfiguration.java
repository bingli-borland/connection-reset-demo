package com.bes.enterprise.actuator;

import com.bes.enterprise.springboot.autoconfigure.BesWebServerFactoryCustomizer;
import com.bes.enterprise.springboot.embedded.BesServletWebServerFactory;
import com.bes.enterprise.web.crane.AbstractProtocol;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.boot.actuate.autoconfigure.web.ManagementContextConfiguration;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementServerProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementWebServerFactoryCustomizer;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;


@ManagementContextConfiguration
@EnableConfigurationProperties({ManagementServerBesProperties.class})
public class BesManagementCustomizerConfiguration {

    @Bean
    ManagementWebServerFactoryCustomizer<ConfigurableServletWebServerFactory> besManagementWebServerFactoryCustomizer(ManagementServerBesProperties besProperties,
            ListableBeanFactory beanFactory) {
        return new BesManagementCustomizer(beanFactory, besProperties);
    }
    static class BesManagementCustomizer extends ManagementWebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {
        private ManagementServerBesProperties besProperties;

        protected BesManagementCustomizer(ListableBeanFactory beanFactory, ManagementServerBesProperties besProperties) {
            super(beanFactory, BesWebServerFactoryCustomizer.class);
            this.besProperties = besProperties;
        }

        @Override
        protected void customize(ConfigurableServletWebServerFactory factory, ManagementServerProperties managementServerProperties, ServerProperties serverProperties) {
            super.customize(factory, managementServerProperties, serverProperties);
            PropertyMapper propertyMapper = PropertyMapper.get();
            propertyMapper.from(besProperties::getMaxThreads).when(this::isPositive).to((maxThreads) -> {
                ((BesServletWebServerFactory) factory).addExecutorCustomizers((workThreadExecutor) -> workThreadExecutor.setMaxThreads(maxThreads));
                ((BesServletWebServerFactory) factory).addConnectorCustomizers((connector) -> ((AbstractProtocol) connector.getProtocolHandler()).setMaxThreads(maxThreads));
            });
            propertyMapper.from(besProperties::getMinSpareThreads).when(this::isPositive).to((minThreads) -> {
                ((BesServletWebServerFactory) factory).addExecutorCustomizers((workThreadExecutor) -> workThreadExecutor.setMinSpareThreads(minThreads));
                ((BesServletWebServerFactory) factory).addConnectorCustomizers((connector) -> ((AbstractProtocol) connector.getProtocolHandler()).setMinSpareThreads(minThreads));
            });
            propertyMapper.from(besProperties::getMaxQueueSize).when(this::isPositive).to((maxQueueSize) ->
                ((BesServletWebServerFactory) factory).addExecutorCustomizers((workThreadExecutor) -> workThreadExecutor.setMaxQueueSize(maxQueueSize))
            );
            propertyMapper.from(besProperties::getMaxIdleTime).when(this::isPositive).to((maxIdleTime) ->
                ((BesServletWebServerFactory) factory).addExecutorCustomizers((workThreadExecutor) -> workThreadExecutor.setMaxIdleTime(maxIdleTime))
            );
            propertyMapper.from(besProperties::getIoMode).whenNonNull().to((ioMode) -> {
                String protocol = null;
                switch (ioMode) {
                    case "NIO":
                        protocol = "com.bes.enterprise.web.crane.http11.Http11NioProtocol";
                        break;
                    case "AIO":
                    default:
                        protocol = "com.bes.enterprise.web.crane.http11.Http11Nio2Protocol";
                }
                ((BesServletWebServerFactory)factory).setProtocol(protocol);
            });

        }

        private boolean isPositive(int value) {
            return value > 0;
        }

    }

}

