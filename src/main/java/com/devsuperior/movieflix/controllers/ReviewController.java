package com.devsuperior.movieflix.controllers;

import com.devsuperior.movieflix.dto.ReviewCreateDTO;
import com.devsuperior.movieflix.dto.ReviewUserDTO;
import com.devsuperior.movieflix.services.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "/reviews")
public class ReviewController {

    private final ReviewService service;


    public ReviewController(ReviewService service) {
        this.service = service;
    }


    @PostMapping
    public ResponseEntity<ReviewUserDTO> insert(@RequestBody @Valid ReviewCreateDTO dto) {
        ReviewUserDTO reviewUserDTO =  service.createReview(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(reviewUserDTO);
    }


}
