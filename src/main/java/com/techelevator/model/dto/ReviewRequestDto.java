package com.techelevator.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;

public class ReviewRequestDto {
//    @Positive(message = "Beer Id must be positive and not blank")
//    private int beerId;
    private String reviewText;
    @Positive
    @Max(value = 5, message = "Rating must be between 1 and 5")
    private int rating;
    private boolean isRecommended;

    public ReviewRequestDto() {}

    public ReviewRequestDto(/*int beerId,*/ String reviewText, int rating, boolean isRecommended) {
//        this.beerId = beerId;
        this.reviewText = reviewText;
        this.rating = rating;
        this.isRecommended = isRecommended;
    }

//    public int getBeerId() {
//        return beerId;
//    }
//
//    public void setBeerId(int beerId) {
//        this.beerId = beerId;
//    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public boolean isRecommended() {
        return isRecommended;
    }

    public void setRecommended(boolean recommended) {
        isRecommended = recommended;
    }
}
