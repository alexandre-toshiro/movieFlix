package com.devsuperior.movieflix.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.devsuperior.movieflix.dto.ReviewUserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;



import com.devsuperior.movieflix.tests.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ReviewResourceIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private String visitorUsername;
    private String visitorPassword;
    private String memberUsername;
    private String memberPassword;

    @BeforeEach
    void setUp() throws Exception {

        visitorUsername = "bob@gmail.com";
        visitorPassword = "123456";
        memberUsername = "ana@gmail.com";
        memberPassword = "123456";
    }

    @Test
    public void insertShouldReturnUnauthorizedWhenNotValidToken() throws Exception {

        ReviewUserDTO reviewDTO = new ReviewUserDTO();
        reviewDTO.setText("Gostei do filme!");
        reviewDTO.setMovieId(1L);

        String jsonBody = objectMapper.writeValueAsString(reviewDTO);

        ResultActions result =
                mockMvc.perform(post("/reviews")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void insertShouldReturnForbiddenWhenVisitorAuthenticated() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, visitorUsername, visitorPassword);

        ReviewUserDTO reviewDTO = new ReviewUserDTO();
        reviewDTO.setText("Gostei do filme!");
        reviewDTO.setMovieId(1L);

        String jsonBody = objectMapper.writeValueAsString(reviewDTO);

        ResultActions result =
                mockMvc.perform(post("/reviews")
                        .header("Authorization", "Bearer " + accessToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isForbidden());
    }

    @Test
    public void insertShouldInsertReviewWhenMemberAuthenticatedAndValidData() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, memberUsername, memberPassword);

        String reviewText = "Gostei do filme!";
        long movieId = 1L;

        ReviewUserDTO reviewDTO = new ReviewUserDTO();
        reviewDTO.setText(reviewText);
        reviewDTO.setMovieId(movieId);

        String jsonBody = objectMapper.writeValueAsString(reviewDTO);

        ResultActions result =
                mockMvc.perform(post("/reviews")
                        .header("Authorization", "Bearer " + accessToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());

        result.andExpect(jsonPath("$.id").isNotEmpty());
        result.andExpect(jsonPath("$.text").value(reviewText));
        result.andExpect(jsonPath("$.movieId").value(movieId));

        result.andExpect(jsonPath("$.user").isNotEmpty());
        result.andExpect(jsonPath("$.user.id").isNotEmpty());
        result.andExpect(jsonPath("$.user.name").isNotEmpty());
        result.andExpect(jsonPath("$.user.email").value(memberUsername));
    }

    @Test
    public void insertShouldReturnUnproccessableEntityWhenMemberAuthenticatedAndInvalidData() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, memberUsername, memberPassword);

        ReviewUserDTO reviewDTO = new ReviewUserDTO();
        reviewDTO.setText("        ");
        reviewDTO.setMovieId(1L);

        String jsonBody = objectMapper.writeValueAsString(reviewDTO);

        ResultActions result =
                mockMvc.perform(post("/reviews")
                        .header("Authorization", "Bearer " + accessToken)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isUnprocessableEntity());
    }
}