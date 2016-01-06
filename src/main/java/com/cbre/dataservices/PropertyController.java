package com.cbre.dataservices;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.cbre.dataservices.repositories.ElasticsearchRepository;
import com.cbre.dataservices.models.Property;
import com.cbre.dataservices.models.PropertyList;
import com.cbre.dataservices.models.SubmissionStatus;

@RestController
@RequestMapping("/api/properties")
@Api(basePath="/api/properties", value="Property", description="APIs for property information")
public class PropertyController {

    @Autowired
    private ElasticsearchRepository elasticsearchRepository;

    @ApiOperation(value="Retrieve a property by id")
    @RequestMapping(path = "/{propertyId}")
    public Property getPropertyById(@ApiParam(name="propertyId", value="The Id of the property to retrieve")
                                        @PathVariable(value="propertyId") String propertyId) {

        return new Property();
    }

    @ApiOperation(value="Retrieve a list of properties, with optional filtering and search criteria, with paging support")
    @RequestMapping(value = "")
    public PropertyList queryProperties(@ApiParam(name="queryText", value="")
                                            @RequestParam(value="queryText", defaultValue="") String queryText,
                                        @ApiParam(name="limit", value="Number of records to retrieve in the result set")
                                            @RequestParam(value="limit", defaultValue="10") int limit,
                                        @ApiParam(name="offset", value="Index into the result set to start retrieving results from")
                                            @RequestParam(value="offset", defaultValue="0") int offset) throws Exception {

        PropertyList resp;
        resp = elasticsearchRepository.queryProperties(queryText, offset, limit);

        return resp;
    }

    @ApiOperation(value="Submit a new provisional property to be created in the platform")
    @RequestMapping(value = "/submitNew", method = RequestMethod.POST)
    public SubmissionStatus submitNew() {
        throw new NotImplementedException();
    }

    @ApiOperation(value="Submit a provisional update to an existing property in the platform")
    @RequestMapping(value = "/submitUpdate", method = RequestMethod.POST)
    public SubmissionStatus submitUpdate() {
        throw new NotImplementedException();
    }
}