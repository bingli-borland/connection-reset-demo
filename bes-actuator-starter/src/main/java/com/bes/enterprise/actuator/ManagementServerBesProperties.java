package com.bes.enterprise.actuator;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "management.server.bes", ignoreUnknownFields = true)
public class ManagementServerBesProperties {

    private int maxThreads = 5;
    private int minSpareThreads = 1;
    private int maxQueueSize = 4096;
    private int maxIdleTime = 60000;

    private String ioMode = "NIO";

    public int getMaxThreads() {
        return maxThreads;
    }

    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    public int getMinSpareThreads() {
        return minSpareThreads;
    }

    public void setMinSpareThreads(int minSpareThreads) {
        this.minSpareThreads = minSpareThreads;
    }

    public int getMaxQueueSize() {
        return maxQueueSize;
    }

    public void setMaxQueueSize(int maxQueueSize) {
        this.maxQueueSize = maxQueueSize;
    }

    public int getMaxIdleTime() {
        return maxIdleTime;
    }

    public void setMaxIdleTime(int maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
    }

    public String getIoMode() {
        return ioMode;
    }

    public void setIoMode(String ioMode) {
        this.ioMode = ioMode;
    }
}
