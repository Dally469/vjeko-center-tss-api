package com.dev.vjeko_api.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "events")

public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String img;
    private String badge;
    private String date;
    private String view;
    private String title;
    private String description;
    private String postedBy;

    public Event(Long id, String img, String badge, String date, String view, String title, String description, String postedBy) {
        this.id = id;
        this.img = img;
        this.badge = badge;
        this.date = date;
        this.view = view;
        this.title = title;
        this.description = description;
        this.postedBy = postedBy;
    }

    public Event() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }
}