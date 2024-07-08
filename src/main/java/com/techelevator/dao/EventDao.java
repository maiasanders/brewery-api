package com.techelevator.dao;

import com.techelevator.model.Category;
import com.techelevator.model.Event;

public interface EventDao {
    Event getEventById(int id);
    Event createEvent(Event event);
    Category addCategoryToEvent(Event event, Category category);
}
