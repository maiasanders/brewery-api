package com.techelevator.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class BeerRequestDto {

    private int beerId;
    @JsonProperty("name")
    @NotBlank(message = "Beer Name can not be blank")
    private String beerName;
    @JsonProperty("brewery_id")
    @Positive
    private int breweryId;
    @NotBlank(message = "Style can not be blank")
    private String style;
    @NotBlank(message = "Description can not be blank")
    private String desc;
    @PositiveOrZero(message = "ABV must be at least 0.0")
    private double abv;
    @JsonProperty("seasonal")
    private boolean isSeasonal;

    public int getBreweryId() {
        return breweryId;
    }

    public void setBreweryId(int breweryId) {
        this.breweryId = breweryId;
    }

    @JsonProperty("season_name")
    private String seasonName;

    public BeerRequestDto() {}

    public BeerRequestDto(int beerId, String name, int breweryId, String style, String desc, double abv, boolean isSeasonal, String seasonName) {
        this.beerId = beerId;
        this.beerName = name;
        this.breweryId = breweryId;
        this.style = style;
        this.desc = desc;
        this.abv = abv;
        this.isSeasonal = isSeasonal;
        this.seasonName = seasonName;
    }

    public BeerRequestDto(int beerId, String name, int breweryId, String style, String desc, double abv, boolean isSeasonal) {
        this.beerId = beerId;
        this.beerName = name;
        this.breweryId = breweryId;
        this.style = style;
        this.desc = desc;
        this.abv = abv;
        this.isSeasonal = isSeasonal;
    }

    public int getBeerId() {
        return beerId;
    }

    public void setBeerId(int beerId) {
        this.beerId = beerId;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
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

    public String getBeerName() {
        return beerName;
    }

    public void setBeerName(String beerName) {
        this.beerName = beerName;
    }
}
