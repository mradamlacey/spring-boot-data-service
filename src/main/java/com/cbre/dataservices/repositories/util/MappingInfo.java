package com.cbre.dataservices.repositories.util;

import static humanize.Humanize.camelize;

public class MappingInfo {

    private String dataType;
    private String elasticsearchFieldName;
    private String modelName;

    public static MappingInfo Build(String elasticsearchFieldName, String dataType){
        MappingInfo mappingInfo = new MappingInfo();
        mappingInfo.elasticsearchFieldName = elasticsearchFieldName;
        mappingInfo.dataType = dataType;
        mappingInfo.modelName = camelize(elasticsearchFieldName.toLowerCase());

        return mappingInfo;
    }

    public static MappingInfo Build(String modelName, String elasticsearchFieldName, String dataType){
        MappingInfo mappingInfo = new MappingInfo();
        mappingInfo.elasticsearchFieldName = elasticsearchFieldName;
        mappingInfo.dataType = dataType;
        mappingInfo.modelName = modelName;

        return mappingInfo;
    }

    public MappingInfo(){

    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getElasticsearchFieldName() {
        return elasticsearchFieldName;
    }

    public void setElasticsearchFieldName(String elasticsearchFieldName) {
        this.elasticsearchFieldName = elasticsearchFieldName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}
