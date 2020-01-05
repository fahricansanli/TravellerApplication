package com.example.istanbultravelapplication.models;

public class District {

    private String districtName;
    private String mostPopularActivity;

    public District(String districtName, String mostPopularActivity){
        this.districtName = districtName;
        this.mostPopularActivity = mostPopularActivity;
    }

    public String getDistrictName() {
        return districtName;
    }

    public String getMostPopularActivity() {
        return mostPopularActivity;
    }
}

