package com.movieflix.service;

import com.movieflix.dto.MovieDto;
import com.movieflix.dto.MoviePageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MovieService {

    MovieDto addMovie(MovieDto movieDto , MultipartFile file) throws IOException;

    MovieDto getMovieById(Integer MovieId);

    List<MovieDto> getAllMovies();

    MovieDto updateMovie(Integer MovieId,MovieDto movieDto , MultipartFile file) throws IOException;
    String deleteMovie(Integer MovieId) throws IOException;

    MoviePageResponse getAllMovieWithPagination(Integer pageNumber , Integer pageSize) ;
    MoviePageResponse getAllMovieWithPaginationAndSorting(Integer pageNumber , Integer pageSize, String sortBy , String dir) ;
}
