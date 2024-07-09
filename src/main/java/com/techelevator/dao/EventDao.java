package com.techelevator.dao;

import com.techelevator.model.Category;
import com.techelevator.model.Event;
import com.techelevator.model.dto.EventGetResponseDto;

import java.util.List;

public interface EventDao {
    Event getEventById(int id);
    Event createEvent(Event event);
    Category addCategoryToEvent(Event event, Category category);

    List<Event> getEventsByBreweryId(int id);

    List<Event> getEventsByBreweryMinDate(int id, String minDate);

    List<Event> getEventsByBreweryMaxDate(int id, String maxDate);

    List<Event> getEventsByBreweryOver21(int id, Boolean over21);

    List<Event> getEventsByBreweryQuery(int id, String query);

    List<Event> getEventsByBreweryDateRange(int id, String minDate, String maxDate);

    List<Event> getEventsByBreweryMinDateOver21(int id, String minDate, Boolean over21);

    List<Event> getEventsByBreweryMinDateQuery(int id, String minDate, String query);

    List<Event> getEventsByBreweryMaxDateOver21(int id, String maxDate, Boolean over21);

    List<Event> getEventsByBreweryMaxDateQuery(int id, String maxDate, String query);

    List<Event> getEventsByBreweryOver21Query(int id, Boolean over21, String query);

    List<Event> getEventsByBreweryDateRangeOver21(int id, String minDate, String maxDate, Boolean over21);

    List<Event> getEventsByBreweryDatesQuery(int id, String minDate, String maxDate, String query);

    List<Event> getEventsByBreweryMinDateOver21Query(int id, String minDate, Boolean over21, String query);

    List<Event> getEventsByBreweryMaxDateOver21Query(int id, String maxDate, Boolean over21, String query);

    List<Event> getEventsByBreweryDatesOver21Query(int id, String minDate, String maxDate, Boolean over21, String query);
}
