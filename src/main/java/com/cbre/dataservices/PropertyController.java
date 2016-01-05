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

    @Autowired
    private ElasticsearchRepository elasticsearchRepository;

    @RequestMapping(path = "/{propertyId}")
    public Property getPropertyById(@PathVariable(value="propertyId") String propertyId) {

        return new Property();
    }

    @RequestMapping(value = "")
    public PropertyList queryProperties(@RequestParam(value="queryText", defaultValue="") String queryText,
                                        @RequestParam(value="limit", defaultValue="10") int limit,
                                        @RequestParam(value="offset", defaultValue="0") int offset) throws Exception {

        PropertyList resp;
        resp = elasticsearchRepository.queryProperties(queryText, offset, limit);

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