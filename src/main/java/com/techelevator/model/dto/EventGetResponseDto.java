package com.techelevator.model.dto;

import com.techelevator.model.Category;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

public class EventGetResponseDto {
    int id;
    String eventName;
//    int breweryId;
    String breweryName;
    LocalDate eventDate;
    Time begins;
    Time ends;
    String desc;
    boolean is21Up;
    List<Category> categories;
}
