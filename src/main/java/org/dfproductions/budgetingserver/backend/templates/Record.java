package org.dfproductions.budgetingserver.backend.templates;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "records")
public class Record {

    @Id
    private int id;

    private String category;
    private String date;
    private double price;
    private String place;
    private String note;
    private int userId;

    public Record(String category, String date, double price, String place, String note, int userId){
        this.category = category;
        this.date = date;
        this.price = price;
        this.place = place;
        this.note = note;
        this.userId = userId;
    }

    public Record(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
