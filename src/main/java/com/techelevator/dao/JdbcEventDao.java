package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Category;
import com.techelevator.model.Event;
import com.techelevator.model.dto.EventGetResponseDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcEventDao implements EventDao{

    private JdbcTemplate template;
    private CategoryDao categoryDao;
    private final String CANNOT_CONNECT_MSG = "Unable to connect to database";
    private final String SELECT_STATEMENT = "SELECT e.event_id, e.event_name, e.brewery_id, b.brewery_name, e.event_date, e.begins, e.ends, e.description, e.over_21, STRING_AGG(c.category_name, ',') AS categories, STRING_AGG(CAST(c.category_id AS varchar), ',') AS category_ids " +
            "FROM event AS e " +
            "JOIN brewery AS b ON e.brewery_id = b.brewery_id " +
            "JOIN event_category AS ec ON e.event_id = ec.event_id " +
            "JOIN category AS c ON ec.category_id = c.category_id ";
    private final String GROUP_BY = " GROUP BY e.event_id, b.brewery_id;";
    private final String WHERE_WITH_QUERY = "WHERE e.event_id = ? AND (e.event_name ILIKE ? OR e.description ILIKE ? OR categories ILIKE ? ";

    public JdbcEventDao(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public Event getEventById(int id) {
        try {
            Event event = template.queryForObject(SELECT_STATEMENT + "WHERE e.event_id = ?" + GROUP_BY, this::mapRowToEvent, id);
//            event.setCategories(categoryDao.getCategoriesByEventId(id));
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

    @Override
    public List<Event> getEventsByBreweryId(int id) {
        try {
            return template.query(SELECT_STATEMENT + "WHERE e.brewery_id = ?" + GROUP_BY, this::mapRowToEvent, id);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException(CANNOT_CONNECT_MSG, e);
        }
    }

    @Override
    public List<Event> getEventsByBreweryMinDate(int id, String minDate) {
        try {
            return template.query(SELECT_STATEMENT +
                    "WHERE e.brewery_id = ? AND event_date >= ?" + GROUP_BY, this::mapRowToEvent, id, minDate);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException(CANNOT_CONNECT_MSG, e);
        }
    }

    @Override
    public List<Event> getEventsByBreweryMaxDate(int id, String maxDate) {
        try {
            return template.query(SELECT_STATEMENT +
                    "WHERE e.brewery_id = ? AND event_date <= ?" + GROUP_BY,
                    this::mapRowToEvent, id, maxDate);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException(CANNOT_CONNECT_MSG, e);
        }
    }

    @Override
    public List<Event> getEventsByBreweryOver21(int id, Boolean over21) {
        try {
            return template.query(SELECT_STATEMENT +
                    "WHERE e.brewery_id = ? AND over_21 = ?" + GROUP_BY, this::mapRowToEvent, id, over21);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException(CANNOT_CONNECT_MSG, e);
        }
    }

    @Override
    public List<Event> getEventsByBreweryQuery(int id, String query) {
        try {
            query = makeWildCard(query);
            return template.query(SELECT_STATEMENT  + WHERE_WITH_QUERY + GROUP_BY,
                    this::mapRowToEvent, id, query, query, query);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException(CANNOT_CONNECT_MSG, e);
        }
    }

    @Override
    public List<Event> getEventsByBreweryDateRange(int id, String minDate, String maxDate) {
        try {
            return template.query(SELECT_STATEMENT +
                    "WHERE e.brewery_id = ? AND event_date BETWEEN ? AND ?" + GROUP_BY, this::mapRowToEvent, id, minDate, maxDate);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException(CANNOT_CONNECT_MSG, e);
        }
    }

    @Override
    public List<Event> getEventsByBreweryMinDateOver21(int id, String minDate, Boolean over21) {
        try {
            return template.query(SELECT_STATEMENT +
                    "WHERE e.brewery_id = ? AND event_date >= ? AND over_21 = ?" + GROUP_BY, this::mapRowToEvent, id, minDate, over21);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException(CANNOT_CONNECT_MSG, e);
        }
    }

    @Override
    public List<Event> getEventsByBreweryMinDateQuery(int id, String minDate, String query) {
        try {
            query = makeWildCard(query);
            return template.query(SELECT_STATEMENT +
                    WHERE_WITH_QUERY +
                    " AND event_date >= ?" + GROUP_BY, this::mapRowToEvent, id, query, query, query, minDate);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException(CANNOT_CONNECT_MSG, e);
        }
    }

    @Override
    public List<Event> getEventsByBreweryMaxDateOver21(int id, String maxDate, Boolean over21) {
        try {
            return template.query(SELECT_STATEMENT +
                    "WHERE e.brewery_id = ? AND event_date <= ? AND over_21 = ?" + GROUP_BY, this::mapRowToEvent, id, maxDate, over21);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException(CANNOT_CONNECT_MSG, e);
        }
    }

    @Override
    public List<Event> getEventsByBreweryMaxDateQuery(int id, String maxDate, String query) {
        try {
            query = makeWildCard(query);
            return template.query(SELECT_STATEMENT +
                    WHERE_WITH_QUERY +
                    " AND event_date <= ?" + GROUP_BY, this::mapRowToEvent, id, query, query, query, maxDate);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException(CANNOT_CONNECT_MSG, e);
        }
    }

    @Override
    public List<Event> getEventsByBreweryOver21Query(int id, Boolean over21, String query) {
        try {
            query = makeWildCard(query);
            return template.query(SELECT_STATEMENT +
                    WHERE_WITH_QUERY +
                    " AND over_21 = ?" + GROUP_BY, this::mapRowToEvent, id, query, query, query, over21);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException(CANNOT_CONNECT_MSG, e);
        }
    }

    @Override
    public List<Event> getEventsByBreweryDateRangeOver21(int id, String minDate, String maxDate, Boolean over21) {
        try {
            return template.query(SELECT_STATEMENT +
                    " AND event_date BETWEEN ? AND ? AND over_21 = ?" + GROUP_BY, this::mapRowToEvent, id, minDate, maxDate, over21);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException(CANNOT_CONNECT_MSG, e);
        }
    }

    @Override
    public List<Event> getEventsByBreweryDatesQuery(int id, String minDate, String maxDate, String query) {
        try {
            query = makeWildCard(query);
            return template.query(SELECT_STATEMENT +
                    WHERE_WITH_QUERY +
                    " AND event_date BETWEEN ? AND ?" + GROUP_BY, this::mapRowToEvent, id, query, query, query, minDate, maxDate);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException(CANNOT_CONNECT_MSG, e);
        }
    }

    @Override
    public List<Event> getEventsByBreweryMinDateOver21Query(int id, String minDate, Boolean over21, String query) {
        try {
            query = makeWildCard(query);
            return template.query(SELECT_STATEMENT +
                    WHERE_WITH_QUERY +
                    " AND event_date >= ? AND over_21 = ?" + GROUP_BY, this::mapRowToEvent, id, query, query, query, minDate, over21);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException(CANNOT_CONNECT_MSG, e);
        }
    }

    @Override
    public List<Event> getEventsByBreweryMaxDateOver21Query(int id, String maxDate, Boolean over21, String query) {
        try {
            query = makeWildCard(query);
            return template.query(SELECT_STATEMENT +
                    WHERE_WITH_QUERY +
                    " AND event_date <= ? AND over_21 = ?" + GROUP_BY, this::mapRowToEvent, id, query, query, query, maxDate, over21);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException(CANNOT_CONNECT_MSG, e);
        }
    }

    @Override
    public List<Event> getEventsByBreweryDatesOver21Query(int id, String minDate, String maxDate, Boolean over21, String query) {
        try {
            query = makeWildCard(query);
            return template.query(SELECT_STATEMENT +
                    WHERE_WITH_QUERY +
                    " AND event_date BETWEEN ? AND ? AND over_21 = ?" + GROUP_BY, this::mapRowToEvent, id, query, query, query, minDate, maxDate, over21);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException(CANNOT_CONNECT_MSG, e);
        }
    }

    private Event mapRowToEvent(ResultSet set, int rowNum) throws SQLException {
        Event event = new Event();
        event.setId(set.getInt("event_id"));
        event.setEventName(set.getString("event_name"));
        event.setBreweryId(set.getInt("brewery_id"));
        event.setBreweryName(set.getString("brewery_name"));
        event.setEventDate(LocalDate.parse(set.getString("event_date")));
        event.setBegins(set.getString("begins"));
        if (set.getString("ends") != null) {
            event.setEnds(set.getString("ends"));

        }
        event.setDesc(set.getString("description"));
        event.setIs21Up(set.getBoolean("over_21"));

        event.setCategories(new ArrayList<>());
        String[] categoryNames = set.getString("categories").split(",");
        String[] categoryIds = set.getString("category_ids").split(",");
        for (int i = 0; i < categoryIds.length; i++) {
            event.addCategory(new Category(
                    Integer.parseInt(categoryIds[i]),
                    categoryNames[i]
            ));
        }

        return event;
    }

    private String makeWildCard(String str) {
        return "%" + str + "%";
    }
}
