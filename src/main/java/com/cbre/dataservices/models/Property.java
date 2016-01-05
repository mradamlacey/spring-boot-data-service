package com.cbre.dataservices.models;

import java.util.ArrayList;
import java.util.List;

import com.cbre.dataservices.models.PropertyAddress;

public class Property {

    private String id;
    private String buildingName;
    private String streetNumber;
    private String streetName;
    private String streetType;
    private String city;
    private String state;
    private String county;
    private String postalCode;
    private String country;
    private List<PropertyAddress> alternateAddresses;
    private GeoLocation location;
    private String propertyType;
    private AuditInfo audit;
    private List<GeographicHierarchyLevel> geographicHierarchy;
    private double grossSquareFeet;
    private double netRentableAreaSquareFeet;
    private double availableSquareFeet;
    private double maxContiguousSquareFeet;
    private int yearBuilt;
    private int monthBuilt;
    private String className;


    public Property() {
        audit = new AuditInfo();
        location = new GeoLocation();
        alternateAddresses = new ArrayList<>();
        geographicHierarchy = new ArrayList<>();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getStreetType() {
        return streetType;
    }

    public void setStreetType(String streetType) {
        this.streetType = streetType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<PropertyAddress> getAlternateAddresses() {
        return alternateAddresses;
    }

    public void setAlternateAddresses(List<PropertyAddress> alternateAddresses) {
        this.alternateAddresses = alternateAddresses;
    }

    public GeoLocation getLocation() {
        return location;
    }

    public void setLocation(GeoLocation location) {
        this.location = location;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public AuditInfo getAudit() {
        return audit;
    }

    public void setAudit(AuditInfo audit) {
        this.audit = audit;
    }

    public List<GeographicHierarchyLevel> getGeographicHierarchy() {
        return geographicHierarchy;
    }

    public void setGeographicHierarchy(List<GeographicHierarchyLevel> geographicHierarchy) {
        this.geographicHierarchy = geographicHierarchy;
    }

    public double getGrossSquareFeet() {
        return grossSquareFeet;
    }

    public void setGrossSquareFeet(double grossSquareFeet) {
        this.grossSquareFeet = grossSquareFeet;
    }

    public double getNetRentableAreaSquareFeet() {
        return netRentableAreaSquareFeet;
    }

    public void setNetRentableAreaSquareFeet(double netRentableAreaSquareFeet) {
        this.netRentableAreaSquareFeet = netRentableAreaSquareFeet;
    }

    public double getAvailableSquareFeet() {
        return availableSquareFeet;
    }

    public void setAvailableSquareFeet(double availableSquareFeet) {
        this.availableSquareFeet = availableSquareFeet;
    }

    public double getMaxContiguousSquareFeet() {
        return maxContiguousSquareFeet;
    }

    public void setMaxContiguousSquareFeet(double maxContiguousSquareFeet) {
        this.maxContiguousSquareFeet = maxContiguousSquareFeet;
    }

    public int getYearBuilt() {
        return yearBuilt;
    }

    public void setYearBuilt(int yearBuilt) {
        this.yearBuilt = yearBuilt;
    }

    public int getMonthBuilt() {
        return monthBuilt;
    }

    public void setMonthBuilt(int monthBuilt) {
        this.monthBuilt = monthBuilt;
    }

    public String getAddress(){
        return String.format("%s %s %s %s %s %s", this.streetNumber,
                this.streetName, this.streetType, this.city, this.state, this.postalCode);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
