package com.cbre.dataservices.models;

import java.util.ArrayList;
import java.util.List;

public class SearchFilter {
    private String fieldName;
    private List<SearchFilterComparison> predicates;

    public SearchFilter(){
        predicates = new ArrayList<>();
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public List<SearchFilterComparison> getPredicates() {
        return predicates;
    }

    public void setPredicates(List<SearchFilterComparison> predicates) {
        this.predicates = predicates;
    }
}
