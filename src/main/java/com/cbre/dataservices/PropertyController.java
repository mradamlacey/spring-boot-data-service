package com.cbre.dataservices;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cbre.dataservices.models.Property;

@RestController
public class PropertyController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Property greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Property(counter.incrementAndGet(),
                String.format(template, name));
    }
}