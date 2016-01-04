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

    public List<Property> getItems(){ return properties; }
    public int getTotal(){ return total; }
    public int getOffset(){ return offset; }
    public int getLimit(){ return limit; }
}
