package com.techelevator.dao;

import com.techelevator.model.Category;

import java.util.List;

public interface CategoryDao {
    Category getCategoryByName(String categoryName, boolean wild);

    List<Category> getCategoriesByEventId(int id);
}
