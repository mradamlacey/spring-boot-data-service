package com.cbre.dataservices.repositories;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cbre.dataservices.models.PropertyList;

@Component
public class ElasticsearchRepository {

    @Value("${elasticsearch.url}")
    private String baseUrl;


    @Value("${elasticsearch.index}")
    private String indexName;

    public ElasticsearchRepository(){

    }

    public PropertyList queryProperties(){

        System.out.println("Elasticsearch URL: " + baseUrl);
        System.out.println("Elasticsearch index: " + indexName);

        PropertyList properties = new PropertyList();

        return properties;
    }
}
