package com.example.bikerental.model;

public class Review {
    private String reviewId;
    private String userEmail;
    private String bikeId;
    private int rating;
    private String comment;

    public Review() {}

    public Review(String reviewId, String userEmail, String bikeId, int rating, String comment) {
        this.reviewId = reviewId;
        this.userEmail = userEmail;
        this.bikeId = bikeId;
        this.rating = rating;
        this.comment = comment;
    }

    public String getReviewId() { return reviewId; }
    public void setReviewId(String reviewId) { this.reviewId = reviewId; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public String getBikeId() { return bikeId; }
    public void setBikeId(String bikeId) { this.bikeId = bikeId; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
