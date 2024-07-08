package com.techelevator.model.dto;

public class BeerResponseDto {
    private int beerId;
    private String beerName;
    private String brewery_name;
    private String styleName;
    private String desc;
    private double abv;
    private boolean isSeasonal;
    private String seasonName;
    private String imgUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public BeerResponseDto() { }

    public BeerResponseDto(int beerId, String beerName, String brewery_name, String styleName, String desc, double abv, boolean isSeasonal, String seasonName, String imgUrl) {
        this.beerId = beerId;
        this.beerName = beerName;
        this.brewery_name = brewery_name;
        this.styleName = styleName;
        this.desc = desc;
        this.abv = abv;
        this.isSeasonal = isSeasonal;
        this.seasonName = seasonName;
        this.imgUrl = imgUrl;
    }

    public String getBrewery_name() {
        return brewery_name;
    }

    public void setBrewery_name(String brewery_name) {
        this.brewery_name = brewery_name;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public int getBeerId() {
        return beerId;
    }

    public void setBeerId(int beerId) {
        this.beerId = beerId;
    }

    public String getBeerName() {
        return beerName;
    }

    public void setBeerName(String beerName) {
        this.beerName = beerName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getAbv() {
        return abv;
    }

    public void setAbv(double abv) {
        this.abv = abv;
    }

    public boolean isSeasonal() {
        return isSeasonal;
    }

    public void setSeasonal(boolean seasonal) {
        isSeasonal = seasonal;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }
}
