package com.dev.vjeko_api.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "hero_data")

public class HeroData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String img;
    private String title;
    private String subtitle;
    private String description;

    public HeroData(Long id, String img, String title, String subtitle, String description) {
        this.id = id;
        this.img = img;
        this.title = title;
        this.subtitle = subtitle;
        this.description = description;
    }
    public HeroData() {}

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}