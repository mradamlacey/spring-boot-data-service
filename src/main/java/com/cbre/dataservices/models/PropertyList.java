package com.cbre.dataservices.models;


import java.util.ArrayList;
import java.util.List;

public class PropertyList extends HypermediaResource {

    private List<Property> properties;
    private int total;
    private int offset;
    private int limit;

    public PropertyList(){
        properties = new ArrayList<Property>();
    }

    public PropertyList(List<Property> properties, int total, int offset, int limit){
        this.properties = properties;
        this.total = total;
        this.offset = offset;
        this.limit = limit;
    }

    public List<Property> getItems(){ return properties; }
    public int getTotal(){ return total; }
    public int getOffset(){ return offset; }
    public int getLimit(){ return limit; }
}
