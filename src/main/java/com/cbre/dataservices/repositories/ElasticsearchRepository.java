package com.cbre.dataservices.repositories;

import com.cbre.dataservices.models.*;
import com.cbre.dataservices.repositories.util.MappingInfo;
import com.cbre.dataservices.util.SslTrustAllManager;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.RawValue;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.omg.CORBA.portable.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Component
public class ElasticsearchRepository {

    @Value("${elasticsearch.url}")
    private String baseUrl;

    @Value("${elasticsearch.index}")
    private String indexName;

    @Autowired
    private SslTrustAllManager sslTrustAllManager;

    private List<MappingInfo> propertyMappingInfo;
    private List<MappingInfo> spaceMappingInfo;

    public ElasticsearchRepository(){
        propertyMappingInfo = new ArrayList<>();
        spaceMappingInfo = new ArrayList<>();
    }

    public PropertyList queryProperties(String queryText, int offset, int limit) throws Exception{

        ObjectMapper mapper = new ObjectMapper();

        System.out.println("Elasticsearch URL: " + baseUrl);
        System.out.println("Elasticsearch index: " + indexName);

        List<Property> properties = new ArrayList<>();

        JsonNodeFactory factory = JsonNodeFactory.instance;

        ObjectNode request = factory.objectNode();
        String url = String.format("%s/%s/property/_search", this.baseUrl, this.indexName);

        StringBuffer response;

        if(queryText != null && !queryText.isEmpty()){

            ObjectNode query = request.putObject("query");
            ObjectNode multiMatch = query.putObject("multi_match");
            multiMatch.put("query", queryText);
            ArrayNode fields = multiMatch.putArray("fields");
            fields.add("PROPERTY_NAME^5");
            fields.add("STREET_NAME^5");
            fields.add("CITY");
            fields.add("COUNTY");
            fields.add("REGION_NAME");
            fields.add("MARKET_NAME");
            fields.add("SUB_MARKET_NAME");
            fields.add("DISTRICT_NAME");
            fields.add("NEIGHBORHOOD_NAME");
        }
        else{
            ObjectNode query = request.putObject("query");
            query.putObject("match_all");
        }

        request.putRawValue("size", new RawValue(String.format("%d", limit)));
        request.putRawValue("from", new RawValue(String.format("%d", offset)));

        String requestJson = mapper.writeValueAsString(request);

        response = sendHttpPost(url, requestJson);

        JsonNode node = mapper.readTree(response.toString());
        JsonNode hits = node.get("hits");

        if(hits == null){
            System.out.println("[ERROR] - Elasticsearch response");
            System.out.println(response.toString());

            throw new Exception("Error from Elasticsearch");
        }
        int total = hits.get("total").asInt();

        JsonNode resultHits = hits.get("hits");
        if(!resultHits.isArray()){
            throw new Exception("Invalid format from Elasticsearch response, no 'hits' field");
        }

        Iterator<JsonNode> iter = resultHits.elements();
        while(iter.hasNext()){
            JsonNode hit = iter.next();

            JsonNode source = hit.get("_source");
            String id = hit.get("_id").asText();

            properties.add(parsePropertySource(id, source));
        }

        PropertyList propertyList = new PropertyList(properties, total, offset, limit);

        return propertyList;
    }

    private StringBuffer sendHttpGet(String url) throws Exception{
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);

        request.addHeader("accept", "application/json");

        HttpResponse response = client.execute(request);

        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " +
                response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        return result;
    }

    private StringBuffer sendHttpPost(String url, String requestData) throws Exception{
        HttpClient client = new DefaultHttpClient();
        SSLSocketFactory sf = new SSLSocketFactory(sslTrustAllManager.getSSLContext());
        Scheme scheme = new Scheme("https", sf, 443);

        client.getConnectionManager().getSchemeRegistry().register(scheme);

        HttpPost post = new HttpPost(url);

        post.addHeader("accept", "application/json");

        post.setEntity(new StringEntity(requestData));

        HttpResponse response = client.execute(post);
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + post.getEntity());
        System.out.println("Response Code : " +
                response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        return result;
    }

    private String getElasticsearchFieldNameForComparison(String entityName, String fieldName, SearchFilterComparison comparison){

        List<MappingInfo> mappingInfos = getMappingInfoByModelName(entityName, fieldName);
        if(mappingInfos.isEmpty()){
            throw new IllegalArgumentException(fieldName);
        }
        MappingInfo mappingInfo = mappingInfos.get(0);
        if(mappingInfo == null){
            throw new IllegalArgumentException(fieldName);
        }

        if(mappingInfo.getDataType().equalsIgnoreCase("match")){
            if(comparison.getComparison().equalsIgnoreCase("eq")){
                return mappingInfo.getElasticsearchFieldName();
            }
        }
        if(mappingInfo.getDataType().equalsIgnoreCase("fulltext")){
            if(comparison.getComparison().equalsIgnoreCase("eq")){
                return mappingInfo.getElasticsearchFieldName() + ".raw";
            }
            else if(comparison.getComparison().equalsIgnoreCase("eq")){
                return mappingInfo.getElasticsearchFieldName();
            }
            else{
                throw new NotImplementedException();
            }
        }

        String msg = String.format("Unable to find fieldName for %s, data type %s, comparison %s",
                fieldName, mappingInfo.getDataType(), comparison.getComparison());

        System.out.println(msg);

        throw new NotImplementedException();
    }

    private List<MappingInfo> getMappingInfoByModelName(String entityName, String modelName){
        List<MappingInfo> mappingInfo = null;
        if(entityName.equalsIgnoreCase("property")){
            mappingInfo = propertyMappingInfo;
        }
        if(entityName.equalsIgnoreCase("space")){
            mappingInfo = spaceMappingInfo;
        }

        List<MappingInfo> matching = new ArrayList<>();
        for(MappingInfo info : mappingInfo){
            if(info.getModelName().equals(modelName)){
                matching.add(info);
            }
        }

        return matching;
    }

    private List<MappingInfo> getMappingInfoByElasticsearchFieldName(String fieldName){
        throw new NotImplementedException();
    }

    private Property parsePropertySource(String id, JsonNode source){

        Property property = new Property();

        property.setId(id);

        property.setBuildingName(source.get("PROPERTY_NAME").asText());
        if(!source.get("STREET_NUMBER2").isNull()){
            property.setStreetNumber(source.get("STREET_NUMBER1").asText() + "-" + source.get("STREET_NUMBER2").asText());
        }
        else{
            property.setStreetNumber(source.get("STREET_NUMBER1").asText());
        }
        property.setStreetName(source.get("STREET_NAME").asText());
        property.setStreetName(source.get("STREET_TYPE").asText());
        property.setStreetName(source.get("CITY").asText());
        property.setStreetName(source.get("STATE").asText());
        property.setStreetName(source.get("COUNTY").asText());
        property.setStreetName(source.get("POSTAL_CODE").asText());
        property.setStreetName(source.get("COUNTRY").asText());

        String[] geoParts = source.get("location").asText("").split(",");
        if(geoParts.length != 2){
            throw new IllegalArgumentException("Invalid geo location from property id: " +
                    property.getId() + ": " + source.get("location").asText());
        }
        property.getLocation().setLatitude(Double.parseDouble(geoParts[0]));
        property.getLocation().setLongitude(Double.parseDouble(geoParts[1]));

        JsonNode altAddys = source.get("alternate_address");
        Iterator<JsonNode> addyIter = altAddys.elements();
        while(addyIter.hasNext()){
            JsonNode addy = addyIter.next();

            String name = addy.get("name").asText(null);
            String address = addy.get("address").asText(null);

            if(name != null){
                PropertyAddress propAddress = new PropertyAddress();
                propAddress.setName(name);
                propAddress.setAddress(address);
                property.getAlternateAddresses().add(propAddress);
            }
        }

        // Audit info
        Date tmp;

        // Geo hierarchy
        GeographicHierarchyLevel region = parseGeoHierarchy("REGION", "Region", 0, source);
        GeographicHierarchyLevel market = parseGeoHierarchy("MARKET", "Market", 1, source);
        GeographicHierarchyLevel subMarket = parseGeoHierarchy("SUB_MARKET", "Sub Market", 2, source);
        GeographicHierarchyLevel district = parseGeoHierarchy("DISTRICT", "District", 3, source);
        GeographicHierarchyLevel neighborhood = parseGeoHierarchy("NEIGHBORHOOD", "Neighborhood", 4, source);

        if (region != null)
        {
            property.getGeographicHierarchy().add(region);
        }
        if (market != null)
        {
            property.getGeographicHierarchy().add(market);
        }
        if (subMarket != null)
        {
            property.getGeographicHierarchy().add(subMarket);
        }
        if (district != null)
        {
            property.getGeographicHierarchy().add(district);
        }
        if (neighborhood != null)
        {
            property.getGeographicHierarchy().add(neighborhood);
        }

        property.setPropertyType(source.get("PROPERTY_TYPE_NAME").asText());
        property.setClassName(source.get("CLASS_NAME").asText());

        property.setGrossSquareFeet(source.get("GROSS_SF").asDouble(-1));
        property.setNetRentableAreaSquareFeet(source.get("NET_RENTABLE_AREA").asDouble(-1));
        property.setAvailableSquareFeet(source.get("AVAIL_SF_TOTAL").asDouble(-1));
        property.setMaxContiguousSquareFeet(source.get("MAX_CONTIGUOUS_SF").asDouble(-1));

        property.setYearBuilt(source.get("YEAR_BUILT").asInt(-1));
        property.setMonthBuilt(source.get("MONTH_BUILT").asInt(-1));

        return property;
    }

    private Space parseSpaceSource(String id, JsonNode source){
        Space space = new Space();

        space.setId(id);

        return space;
    }

    private GeographicHierarchyLevel parseGeoHierarchy(String elasticsearchFieldPrefix, String levelLabel, int levelOrder, JsonNode source){
        if(source.get(elasticsearchFieldPrefix + "_ID").isNull()){
            return null;
        }

        GeographicHierarchyLevel level = new GeographicHierarchyLevel();
        level.setLabel(levelLabel);
        level.setLevel(levelOrder);
        level.setId(source.get(elasticsearchFieldPrefix + "_ID").asText());
        level.setName(source.get(elasticsearchFieldPrefix + "_NAME").asText());

        return level;
    }

    private void buildPropertyMapping(){

        propertyMappingInfo.add(MappingInfo.Build("buildingName", "PROPERTY_NAME", "fulltext"));
        propertyMappingInfo.add(MappingInfo.Build("streetNumber", "STREET_NUMBER1", "fulltext"));
        propertyMappingInfo.add(MappingInfo.Build("streetNumber", "STREET_NUMBER2", "fulltext"));
        propertyMappingInfo.add( MappingInfo.Build("STREET_NAME", "fulltext"));
        propertyMappingInfo.add(MappingInfo.Build("STREET_TYPE", "fulltext"));
        propertyMappingInfo.add( MappingInfo.Build("CITY", "fulltext"));
        propertyMappingInfo.add(MappingInfo.Build("STATE", "fulltext"));
        propertyMappingInfo.add(MappingInfo.Build("COUNTY", "fulltext"));
        propertyMappingInfo.add(MappingInfo.Build("POSTAL_CODE", "fulltext"));
        propertyMappingInfo.add(MappingInfo.Build("COUNTRY", "fulltext"));
        propertyMappingInfo.add(MappingInfo.Build("alternateAddress.name", "alternate_address.name", "fulltext"));
        propertyMappingInfo.add(MappingInfo.Build("alternateAddress.address", "alternate_address.address", "fulltext"));
        propertyMappingInfo.add(MappingInfo.Build("CREATED_ON", "datetime"));
        propertyMappingInfo.add(MappingInfo.Build("CREATED_BY", "fulltext"));
        propertyMappingInfo.add(MappingInfo.Build("MODIFIED_ON", "datetime"));
        propertyMappingInfo.add(MappingInfo.Build("MODIFIED_BY", "fulltext"));
        propertyMappingInfo.add(MappingInfo.Build("IS_VERIFIED", "bool"));

        propertyMappingInfo.add( MappingInfo.Build("propertyType", "PROPERTY_TYPE_NAME", "match"));
        propertyMappingInfo.add(MappingInfo.Build("class", "CLASS_NAME", "match"));

        propertyMappingInfo.add(MappingInfo.Build("grossSquareFeet", "GROSS_SF", "numeric"));
        propertyMappingInfo.add(MappingInfo.Build("netRentableAreaSquareFeet", "NET_RENTABLE_AREA", "numeric"));
        propertyMappingInfo.add(MappingInfo.Build("availableSquareFeet", "AVAIL_SF_TOTAL", "numeric"));
        propertyMappingInfo.add(MappingInfo.Build("maxContiguousSquareFeet", "MAX_CONTIGUOUS_SF", "numeric"));

        propertyMappingInfo.add(MappingInfo.Build("yearBuilt", "YEAR_BUILT", "numeric"));
        propertyMappingInfo.add(MappingInfo.Build("monthBuilt", "MONTH_BUILT", "numeric"));

        propertyMappingInfo.add(MappingInfo.Build("geographicHierarchy.region.id", "REGION_ID", "match"));
        propertyMappingInfo.add(MappingInfo.Build("geographicHierarchy.region.name", "REGION_NAME", "fulltext"));
        propertyMappingInfo.add( MappingInfo.Build("geographicHierarchy.market.id", "MARKET_ID", "match"));
        propertyMappingInfo.add(MappingInfo.Build("geographicHierarchy.market.name", "MARKET_NAME", "fulltext"));
        propertyMappingInfo.add(MappingInfo.Build("geographicHierarchy.submarket.id", "SUB_MARKET_ID", "match"));
        propertyMappingInfo.add(MappingInfo.Build("geographicHierarchy.submarket.name", "SUB_MARKET_NAME", "fulltext"));
        propertyMappingInfo.add( MappingInfo.Build("geographicHierarchy.neighborhood.id", "NEIGHBORHOOD_ID", "match"));
        propertyMappingInfo.add( MappingInfo.Build("geographicHierarchy.neighborhood.name", "NEIGHBORHOOD_NAME", "fulltext"));
        propertyMappingInfo.add(MappingInfo.Build("geographicHierarchy.district.id", "DISTICT_ID", "match"));
        propertyMappingInfo.add(MappingInfo.Build("geographicHierarchy.district.name", "DISTRICT_NAME", "fulltext"));
    }
}
