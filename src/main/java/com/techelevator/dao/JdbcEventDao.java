package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Category;
import com.techelevator.model.Event;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class JdbcEventDao implements EventDao{

    private JdbcTemplate template;
    private CategoryDao categoryDao;
    private final String CANNOT_CONNECT_MSG = "Unable to connect to database";
    private final String SELECT_STATEMENT = "SELECT event_id, event_name, e.brewery_id, b.brewery_name, event_date, begins, ends, e.description, over_21 " +
            "FROM event AS e " +
            "JOIN brewery AS b ON e.brewery_id = b.brewery_id ";

    public JdbcEventDao(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public Event getEventById(int id) {
        try {
            Event event = template.queryForObject(SELECT_STATEMENT + "WHERE event_id = ?;", this::mapRowToEvent, id);
            event.setCategories(categoryDao.getCategoriesByEventId(id));
            return event;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException(CANNOT_CONNECT_MSG, e);
        }
    }

    @Override
    public Event createEvent(Event event) {
        String sql = "INSERT INTO event " +
                "(event_name, brewery_id, event_date, begins, ends, description, over_21) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                "RETURNING event_id;";

        try {
            int eventId = template.queryForObject(sql, Integer.class, event.getEventName(), event.getBreweryId(), event.getEventDate(), event.getBegins(), event.getEnds(), event.getDesc(), event.isIs21Up());
            for (Category category : event.getCategories()) {
                addCategoryToEvent(event, category);
            }
            return getEventById(eventId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException(CANNOT_CONNECT_MSG, e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Unable to add Event due to data integrity violation", e);
        }
    }

    @Override
    public Category addCategoryToEvent(Event event, Category category) {
        String sql = "INSERT INTO event_category " +
                "(event_id, category_id) VALUES (?, ?);";
        try {
            template.update(sql, event.getId(), category.getId());
            return category;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException(CANNOT_CONNECT_MSG + ", failed to add category to event", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Unable to add category to event due to data integrity violation", e);
        }
    }

    private Event mapRowToEvent(ResultSet set, int rowNum) throws SQLException {
        Event event = new Event();
        event.setId(set.getInt("event_id"));
        event.setEventName(set.getString("event_name"));
        event.setBreweryId(set.getInt("brewery_id"));
        event.setBreweryName(set.getString("brewery_name"));
        event.setEventDate(LocalDate.parse(set.getString("event_date")));
        event.setBegins(set.getTime("begins"));
        if (set.getTime("ends") != null) {
            event.setEnds(set.getTime("ends"));
        }
        event.setDesc(set.getString("description"));
        event.setIs21Up(set.getBoolean("over_21"));
        return event;
    }
}
