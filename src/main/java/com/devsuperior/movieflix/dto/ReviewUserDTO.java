package com.devsuperior.movieflix.dto;

import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.entities.User;

public class ReviewUserDTO {

    private Long id;
    private String text;

    private Long movieId;

    private UserDTO user;


    public ReviewUserDTO() {
    }

    public ReviewUserDTO(Long id, String text, Long movieId, UserDTO user) {
        this.id = id;
        this.text = text;
        this.movieId = movieId;
        this.user = user;
    }

    public ReviewUserDTO(Review entity, UserDTO userDTO, Movie movieEntity) {
        id = entity.getId();
        text = entity.getText();
        movieId = movieEntity.getId();
        user = userDTO;

    }

    public ReviewUserDTO(Review entity, User userEntity, Movie movieEntity) {
        id = entity.getId();
        text = entity.getText();
        movieId = movieEntity.getId();
        user = new UserDTO(userEntity);

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

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
