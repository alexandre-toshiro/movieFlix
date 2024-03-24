package com.devsuperior.movieflix.repositories;

import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("SELECT obj FROM Movie obj INNER JOIN obj.genre genres" +
            " WHERE (:genre IS NULL OR genres = :genre) ORDER BY  obj.title")
    Page<Movie> findMovieByGenre(Genre genre, Pageable pageable);

    @Query("SELECT obj FROM Movie obj JOIN FETCH obj.genre WHERE obj IN :movie")
    List<Movie> findMoviesWithGenre(List<Movie> movie);

    @Query("SELECT obj FROM Movie obj LEFT JOIN FETCH obj.reviews WHERE obj IN :movies")
    List<Movie> findMoviesWithReviews(@Param("movies") List<Movie> movies);

    @Query("SELECT r FROM Review r JOIN FETCH r.user WHERE r.movie.id = :id")
    List<Review> findReviewsOfMovie(@Param("id") Long id);


}
