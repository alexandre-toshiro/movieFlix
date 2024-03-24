package com.devsuperior.movieflix.dto;

import com.devsuperior.movieflix.entities.Review;

import javax.validation.constraints.NotBlank;

public class ReviewCreateDTO {

    private Long id;

    @NotBlank
    private String text;
    private Long movieId;

    public ReviewCreateDTO() {
    }

    public ReviewCreateDTO(Long id, String text, Long movieId) {
        this.id = id;
        this.text = text;
        this.movieId = movieId;
    }

    public ReviewCreateDTO(Review entity) {
        id = entity.getId();
        text = entity.getText();
        movieId = entity.getMovie().getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }
}
