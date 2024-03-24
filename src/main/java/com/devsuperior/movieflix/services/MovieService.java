package com.devsuperior.movieflix.services;

import com.devsuperior.movieflix.dto.MovieByGenreDTO;
import com.devsuperior.movieflix.dto.MovieDTO;
import com.devsuperior.movieflix.dto.ReviewUserDTO;
import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.repositories.GenreRepository;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final MovieRepository repository;
    private final GenreRepository genreRepository;

    public MovieService(MovieRepository repository, GenreRepository genreRepository) {
        this.repository = repository;
        this.genreRepository = genreRepository;
    }

    public MovieDTO findById(Long id) {
        Optional<Movie> obj = repository.findById(id);
        Movie entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new MovieDTO(entity, entity.getGenre());
    }

    public Page<MovieByGenreDTO> findMovieByGenre(Long genreId, Pageable pageable) {
        Genre genre = (genreId == 0) ? null : genreRepository.getOne(genreId);
        Page<Movie> page = repository.findMovieByGenre(genre, pageable);
        List<Movie> moviesWithGenre = page.getContent();
        repository.findMoviesWithGenre(moviesWithGenre);// n + 1 - com genero
        repository.findMoviesWithReviews(moviesWithGenre);// n + 1 - com reviews
        return page.map(MovieByGenreDTO::new);
    }

    public List<ReviewUserDTO> findReviews(Long id) {
        Movie movie = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        List<Review> reviews = repository.findReviewsOfMovie(movie.getId());
        return reviews.stream().map(review -> new ReviewUserDTO(review, review.getUser(), review.getMovie())).collect(Collectors.toList());
    }
}
