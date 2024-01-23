package vttp.paf.assessmentpractice.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class Booking {

    private String name;
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    
    private Integer duration;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Booking() {
    }

    public Booking(String name, String email, Date date, Integer duration) {
        this.name = name;
        this.email = email;
        this.date = date;
        this.duration = duration;
    }
}
