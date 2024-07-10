package com.techelevator.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.techelevator.model.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Time;
import java.util.Date;
import java.util.List;

public class EventPostRequestDto {
    @JsonProperty("event-name")
    @NotBlank(message = "field event-name can not be blank")
    String eventName;

    @JsonProperty("brewery_id")
    @Min(value = 0, message = "field brewery_id can not be blank")
    int breweryId;
    @JsonProperty("event_date")
    @NotBlank(message = "field event_date can not be blank")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    String eventDate;
    @NotBlank(message = "field begins can not be blank")
//            @DateTimeFormat()
    @Pattern(regexp = "[0-1][0-9]:[0-5][0-9] [a|A|p|P]M")
    String begins;
    @Pattern(regexp = "[0-1][0-9]:[0-5][0-9] [a|A|p|P]M")
    String ends;
    @NotBlank(message = "field desc can not be blank")
    String desc;
    @JsonProperty("over_21")
    boolean is21Up;
    String[] categories;

    public EventPostRequestDto() { }

    public EventPostRequestDto(String eventName, int breweryId, String eventDate, String begins, String ends, String desc, boolean is21Up, String[] categories) {
        this.eventName = eventName;
        this.breweryId = breweryId;
        this.eventDate = eventDate;
        this.begins = begins;
        this.ends = ends;
        this.desc = desc;
        this.is21Up = is21Up;
        this.categories = categories;
    }


    public String getEventName() {
        return eventName;
    }

    public int getBreweryId() {
        return breweryId;
    }

    public void setBreweryId(int breweryId) {
        this.breweryId = breweryId;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getBegins() {
        return begins;
    }

    public void setBegins(String begins) {
        this.begins = begins;
    }

    public String getEnds() {
        return ends;
    }

    public void setEnds(String ends) {
        this.ends = ends;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isIs21Up() {
        return is21Up;
    }

    public void setIs21Up(boolean is21Up) {
        this.is21Up = is21Up;
    }

    public String[] getCategories() {
        return categories;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }
}
