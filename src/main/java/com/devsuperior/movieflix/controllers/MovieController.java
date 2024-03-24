package com.devsuperior.movieflix.controllers;

import com.devsuperior.movieflix.dto.MovieByGenreDTO;
import com.devsuperior.movieflix.dto.MovieDTO;
import com.devsuperior.movieflix.dto.ReviewUserDTO;
import com.devsuperior.movieflix.services.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/movies")
public class MovieController {

    private final MovieService service;

    public MovieController(MovieService service) {
        this.service = service;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<MovieDTO> findById(@PathVariable Long id) {
        MovieDTO dto = service.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping
    public ResponseEntity<Page<MovieByGenreDTO>> findAll(
            @RequestParam(value = "genreId", defaultValue = "0") Long genreId,
            Pageable pageable) {
        Page<MovieByGenreDTO> list = service.findMovieByGenre(genreId, pageable);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}/reviews")
    public ResponseEntity<List<ReviewUserDTO>> findReviews(@PathVariable Long id) {
        List<ReviewUserDTO> list = service.findReviews(id);
        return ResponseEntity.ok().body(list);
    }


}
