package com.movieflix.dto;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieDto {

    private Integer movieId ;


    @NotBlank(message = "Title is required")
    private String title ;


    @NotBlank(message = "Director is required")
    private String director ;


    @NotBlank(message = "Studio is required")
    private String studio ;


    private Set<String> movieCast ;


    private Integer releaseYear ;


    @NotBlank(message = "poster is required")
    private String poster ;

    @NotBlank(message = "poster's url is required")
    private String posterUrl ;
}
