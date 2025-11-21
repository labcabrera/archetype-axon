package org.labcabrera.sample.archetype.casefolder.interfaces.http;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.bind.annotation.RestController;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CounterController implements CounterControllerDefinition {

    private Map<String, Integer> counterMap = new ConcurrentHashMap<>();

    private final Counter counterOperations;
    private final Counter counterFoo;
    private final Counter counterError;

    public CounterController(MeterRegistry meterRegistry) {
        this.counterOperations = Counter.builder("counter.operations.total")
            .description("Total number of operations")
            .register(meterRegistry);
        this.counterFoo = Counter.builder("counter.foo.total")
            .description("Total number of foo operations")
            .tag("type", "example")
            .register(meterRegistry);
        this.counterError = Counter.builder("counter.errors.total")
            .description("Total number of error operations")
            .register(meterRegistry);
        Gauge.builder("foo.count", counterMap, e -> e.getOrDefault("foo", 0))
            .description("Current foo count")
            .register(meterRegistry);
    }

    @Override
    public Map<String, Integer> getCounters() {
        log.debug("Getting all counters");
        return counterMap;
    }

    @Override
    public Integer incrementCounter(String key) {
        log.info("Incrementing counter for key {}", key);
        if ("err".equals(key)) {
            counterError.increment();
            throw new RuntimeException("Simulated error for key 'err'");
        }
        counterOperations.increment();
        counterMap.compute(key, (k, v) -> (v == null) ? 1 : v + 1);
        if ("foo.counter.total".equals(key)) {
            counterFoo.increment();
        }
        return counterMap.get(key);
    }

    @Override
    public Integer setCounter(String key, Integer value) {
        log.info("Setting counter for key {} to value {}", key, value);
        return counterMap.put(key, value);
    }

}
