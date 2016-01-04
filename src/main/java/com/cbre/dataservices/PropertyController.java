package com.cbre.dataservices;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.*;

import com.cbre.dataservices.models.Property;
import com.cbre.dataservices.models.PropertyList;
import com.cbre.dataservices.models.SubmissionStatus;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(path = "/{propertyId}")
    public Property getPropertyById(@PathVariable(value="propertyId") String propertyId) {
        return new Property(counter.incrementAndGet(),
                String.format(template, propertyId));
    }

    @RequestMapping(value = "")
    public PropertyList queryProperties(@RequestParam(value="name", defaultValue="World") String name) {

        PropertyList resp = new PropertyList();

        return resp;
    }

    @RequestMapping(value = "/submitNew", method = RequestMethod.POST)
    public SubmissionStatus submitNew() {
        throw new NotImplementedException();
    }

    @RequestMapping(value = "/submitUpdate", method = RequestMethod.POST)
    public SubmissionStatus submitUpdate() {
        throw new NotImplementedException();
    }
}