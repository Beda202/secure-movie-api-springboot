package com.movieflix.controller;

import com.movieflix.dto.MovieDto;
import com.movieflix.dto.MoviePageResponse;
import com.movieflix.entities.Movie;
import com.movieflix.exception.EmptyFileException;
import com.movieflix.service.MovieService;
import com.movieflix.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/movie")
public class MovieController {
    private MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add-movie")
    public ResponseEntity<MovieDto> addMovie(@RequestPart String movieDto, @RequestPart MultipartFile file) throws IOException {
       if(file.isEmpty()){
           throw new EmptyFileException("File is Empty! please send another file ") ;
       }
        MovieDto movieDto1 = convertToMovieDto(movieDto);


        return new ResponseEntity<>(movieService.addMovie(movieDto1, file), HttpStatus.CREATED);
    }

    private MovieDto convertToMovieDto(String movieDtoObject) throws JsonProcessingException{

        ObjectMapper objectMapper  =new ObjectMapper();
        return   objectMapper.readValue(movieDtoObject,MovieDto.class);

    }

    @GetMapping("/{MovieId}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable Integer MovieId) {
        return new ResponseEntity<>(movieService.getMovieById(MovieId), HttpStatus.OK);
    }
    @GetMapping("/get-all-movies")
    public ResponseEntity<List<MovieDto>> getAllMovies() {
        return new ResponseEntity<>(movieService.getAllMovies(), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<MovieDto> updateMovie (@PathVariable Integer id , @RequestPart String movieDto , @RequestPart MultipartFile file) throws IOException{
        if(file.isEmpty()){
            file =null ;
        }
        MovieDto dto = convertToMovieDto(movieDto);
        return new ResponseEntity<>(movieService.updateMovie(id , dto , file), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMovie (@PathVariable Integer id) throws IOException {
        return ResponseEntity.ok(movieService.deleteMovie(id)) ;
    }

    @GetMapping("/all-moviePage")
    public ResponseEntity<MoviePageResponse> getAllMoviePage(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER , required = false) Integer pageNumber ,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE , required = false) Integer pageSize
    ) {
        return ResponseEntity.ok(movieService.getAllMovieWithPagination(pageNumber,pageSize));
    }

    @GetMapping("/all-moviePageSorting")
    public ResponseEntity<MoviePageResponse> getAllMoviePageAndSort(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER , required = false) Integer pageNumber ,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE , required = false) Integer pageSize ,
            @RequestParam(defaultValue = AppConstants.SORT_BY , required = false) String sortBy ,
            @RequestParam(defaultValue = AppConstants.SORT_dir , required = false) String dir
    ) {
        return ResponseEntity.ok(movieService.getAllMovieWithPaginationAndSorting(pageNumber,pageSize,sortBy,dir));
    }
}
