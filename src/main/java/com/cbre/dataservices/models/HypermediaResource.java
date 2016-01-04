package com.cbre.dataservices.models;

import java.util.List;

public abstract class HypermediaResource {

    private List<HypermediaLink> links;

    public void addLink(HypermediaLink link){
        if(link == null){
            throw new NullPointerException("link");
        }

        links.add(link);
    }

    public void addLinks(List<HypermediaLink> links){
        for(HypermediaLink link : links){
            addLink(link);
        }
    }
}
