package com.cbre.dataservices;

import java.util.concurrent.atomic.AtomicLong;

import com.cbre.dataservices.repositories.ElasticsearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.cbre.dataservices.models.Property;
import com.cbre.dataservices.models.PropertyList;
import com.cbre.dataservices.models.SubmissionStatus;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    private ElasticsearchRepository elasticsearchRepository;

    @RequestMapping(path = "/{propertyId}")
    public Property getPropertyById(@PathVariable(value="propertyId") String propertyId) {
        return new Property(counter.incrementAndGet(),
                String.format(template, propertyId));
    }

    @RequestMapping(value = "")
    public PropertyList queryProperties(@RequestParam(value="name", defaultValue="World") String name) {

        PropertyList resp = new PropertyList();

        elasticsearchRepository.queryProperties();

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