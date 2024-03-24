package com.devsuperior.movieflix.services;

import com.devsuperior.movieflix.dto.ReviewCreateDTO;
import com.devsuperior.movieflix.dto.ReviewUserDTO;
import com.devsuperior.movieflix.dto.UserDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import com.devsuperior.movieflix.services.exceptions.AuthService;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    private final ReviewRepository repository;
    private final MovieRepository movieRepository;

    private final AuthService authService;

    public ReviewService(ReviewRepository repository, MovieRepository movieRepository, AuthService authService) {
        this.repository = repository;
        this.movieRepository = movieRepository;
        this.authService = authService;
    }


    public ReviewUserDTO createReview(ReviewCreateDTO dto) {
        User loggedUser = authService.findLoggedUser();
        authService.validateMemberOrVisitor(loggedUser.getId());

        Movie movie = movieRepository.findById(dto.getMovieId()).orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        Review review = new Review();
        review.setText(dto.getText());
        review.setMovie(movie);
        review.setUser(loggedUser);

        review  = repository.save(review);

        return new ReviewUserDTO(review, loggedUser, movie);
    }


}
