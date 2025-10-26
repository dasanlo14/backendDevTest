package com.backendDemo.similarProducts.client;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
@ConfigurationProperties(prefix = "upstream.limiter")
public class UpstreamLimiter {
    private int maxConcurrent = 16;
    private Duration acquireTimeout = Duration.ofMillis(250);

    private Semaphore sem;

    @PostConstruct
    void init() {
        sem = new Semaphore(maxConcurrent);
    }

    public <T> T runWithPermit(Supplier<T> call) {
        try {
            if (!sem.tryAcquire(acquireTimeout.toMillis(), TimeUnit.MILLISECONDS)) {
                throw new ResponseStatusException(
                        HttpStatus.SERVICE_UNAVAILABLE, "Upstream saturado"
                );
            }
            try {
                return call.get();
            } finally {
                sem.release();
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Interrumpido");
        }
    }
}

