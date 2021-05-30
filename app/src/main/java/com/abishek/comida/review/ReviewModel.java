package com.abishek.comida.review;

public class ReviewModel {
    private String userId;
    private String partnerId;
    private String orderId;
    private String comment;
    private String rating;
    private String createAt;
    private String customerName;
    private String customerImage;

    public ReviewModel(String userId, String partnerId, String orderId, String comment,
                       String rating, String createAt, String customerName, String customerImage) {
        this.userId = userId;
        this.partnerId = partnerId;
        this.orderId = orderId;
        this.comment = comment;
        this.rating = rating;
        this.createAt = createAt;
        this.customerName = customerName;
        this.customerImage = customerImage;
    }

    public String getUserId() {
        return userId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getComment() {
        return comment;
    }

    public String getRating() {
        return rating;
    }

    public String getCreateAt() {
        return createAt;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerImage() {
        return customerImage;
    }
}
