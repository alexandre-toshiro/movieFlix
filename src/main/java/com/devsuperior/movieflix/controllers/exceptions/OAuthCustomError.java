package com.devsuperior.movieflix.controllers.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class OAuthCustomError implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("error")
    private String error;
    @JsonProperty("error_description")
    private String errorDescription;

    public OAuthCustomError() {
    }

    public OAuthCustomError(String error, String errorDescription) {
        this.error = error;
        this.errorDescription = errorDescription;
    }


}
