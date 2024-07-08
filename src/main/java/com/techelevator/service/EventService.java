package com.techelevator.service;

import com.techelevator.dao.BreweryDao;
import com.techelevator.dao.CategoryDao;
import com.techelevator.dao.EventDao;
import com.techelevator.dao.UserDao;
import com.techelevator.exception.UnauthorizedUserException;
import com.techelevator.model.Brewery;
import com.techelevator.model.Event;
import com.techelevator.model.User;
import com.techelevator.model.dto.EventPostRequestDto;
import com.techelevator.model.dto.EventPostResponseDto;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;

@Component
public class EventService {
    private EventDao eventDao;
    private UserDao userDao;
    private BreweryDao breweryDao;
    private CategoryDao categoryDao;

    public EventService(EventDao eventDao, UserDao userDao, BreweryDao breweryDao, CategoryDao categoryDao) {
        this.eventDao = eventDao;
        this.userDao = userDao;
        this.breweryDao = breweryDao;
        this.categoryDao = categoryDao;
    }

    public EventPostResponseDto add(EventPostRequestDto dto, String name) {
        User user = userDao.getUserByUsername(name);
        Brewery brewery = breweryDao.getBreweryByBrewerId(user.getId());
        if (isAuthBrewer(user, brewery)) {
            Event event = dtoToEvent(dto);
            return convertEventToPostResponseDto(eventDao.createEvent(event));
        }
        throw new UnauthorizedUserException();
    }

    private EventPostResponseDto convertEventToPostResponseDto(Event event) {
        return new EventPostResponseDto(
                event.getId(),
                event.getEventName(),
                event.getEventDate(),
                event.getBegins(),
                event.getEnds(),
                event.getDesc(),
                event.isIs21Up(),
                event.getCategories()
        );
    }

    private boolean isAuthBrewer(User user, Brewery brewery) {
        return user.getId() == brewery.getBrewerId();
    }
    private Event dtoToEvent(EventPostRequestDto dto) {
        Event event = new Event();
        event.setEventDate(LocalDate.parse(dto.getEventDate()));
        event.setBegins(Time.valueOf(dto.getBegins()));
        event.setEnds(Time.valueOf(dto.getEnds()));
        event.setDesc(dto.getDesc());
        event.setIs21Up(dto.isIs21Up());
        event.setCategories(new ArrayList<>());
        for (String category : dto.getCategories()) {
            event.addCategory(categoryDao.getCategoryByName(category, false) );
        }
        return event;
    }
}
