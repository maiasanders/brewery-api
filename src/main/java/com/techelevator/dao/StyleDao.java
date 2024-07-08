package com.techelevator.dao;

import com.techelevator.model.Style;
import org.springframework.jdbc.core.JdbcTemplate;

public interface StyleDao {

    Style getStyleById(int id);
    Style getStyleByName(String name);

}
