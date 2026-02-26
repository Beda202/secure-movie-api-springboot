package com.movieflix.service.implentation;

import com.movieflix.Repository.MovieRepo;
import com.movieflix.dto.MovieDto;
import com.movieflix.dto.MoviePageResponse;
import com.movieflix.entities.Movie;
import com.movieflix.exception.FileExistException;
import com.movieflix.exception.FileNotFoundException;
import com.movieflix.service.FileService;
import com.movieflix.service.MovieService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
@Service
public class MovieServiceImpl implements MovieService {
   private final MovieRepo movieRepo;
   private final FileService fileService;

   @Value("${project.poster}")
   private String path ;

   @Value("${base.url}")
   private String baseUrl;
   
   public MovieServiceImpl(MovieRepo movieRepo, FileService fileService) {
       this.movieRepo = movieRepo;
       this.fileService = fileService;
   }

    @Override
    public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException {
        if(Files.exists(Paths.get(path + File.separator + file.getOriginalFilename()))){
            throw new FileExistException("File already exists please enter another fileName !");
        };

        String fileName = fileService.uploadFile(path, file);
        movieDto.setPoster(fileName);

        Movie movie = Movie.builder()
                .title(movieDto.getTitle())
                .director(movieDto.getDirector())
                .studio(movieDto.getStudio())
                .movieCast(movieDto.getMovieCast())
                .releaseYear(movieDto.getReleaseYear())
                .poster(movieDto.getPoster())
                .build();

        Movie savedMovie = movieRepo.save(movie);
        String posterUrl = baseUrl + "/file/" + fileName ;
        MovieDto response = new MovieDto(
                savedMovie.getMovieId(),
                savedMovie.getTitle(),
                savedMovie.getDirector(),
                savedMovie.getStudio(),
                savedMovie.getMovieCast(),
                savedMovie.getReleaseYear(),
                savedMovie.getPoster(),
                 posterUrl);

        return response;
    }

    @Override
    public MovieDto getMovieById(Integer MovieId) {
       Movie movie = movieRepo.findById(MovieId).orElseThrow(() -> new FileNotFoundException("Movie not found with id " + MovieId));
       String posterUrl = baseUrl + "/file/" + movie.getPoster();
       MovieDto response = new MovieDto(
               movie.getMovieId(),
               movie.getTitle(),
               movie.getDirector(),
               movie.getStudio(),
               movie.getMovieCast(),
               movie.getReleaseYear(),
               movie.getPoster(),
               posterUrl
       );
        return response;
    }

    @Override
    public List<MovieDto> getAllMovies() {
        List<Movie> movies = movieRepo.findAll();
        List<MovieDto> response = new ArrayList<>();
        for(Movie movie : movies) {
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            MovieDto movieDto = new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl
            );
            response.add(movieDto);
        }
        return response;
    }

    @Override
    public MovieDto updateMovie(Integer MovieId, MovieDto movieDto, MultipartFile file) throws IOException {
        Movie movie = movieRepo.findById(MovieId).orElseThrow(() -> new FileNotFoundException("Movie not found with id " + MovieId));
        String fileName = movie.getPoster();
        if (fileName !=null){
            Files.delete(Paths.get(path + File.separator + fileName));
            fileName = fileService.uploadFile(path, file);
        }
        movieDto.setPoster(fileName);
        Movie mv = new Movie(
                movie.getMovieId(),
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()

        );
        String posterUrl = baseUrl + "/file/" + fileName;
        Movie saved = movieRepo.save(mv);
        MovieDto dto = new MovieDto(
                saved.getMovieId(),
                saved.getTitle(),
                saved.getDirector(),
                saved.getStudio(),
                saved.getMovieCast(),
                saved.getReleaseYear(),
                saved.getPoster(),
                posterUrl
        );
        return dto;
    }

    @Override
    public String deleteMovie(Integer MovieId) throws IOException{
        Movie movie = movieRepo.findById(MovieId).orElseThrow(() -> new FileNotFoundException("Movie not found with id " + MovieId));
        Integer movieId = movie.getMovieId() ;
        Files.deleteIfExists(Paths.get(path + File.separator + movie.getPoster()));
        movieRepo.delete(movie);
        return "MovieId Deleted Successfully"+ movieId;
    }

    @Override
    public MoviePageResponse getAllMovieWithPagination(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Movie> page = movieRepo.findAll(pageable);
        List<Movie> movies  = page.getContent();

        List<MovieDto> response = new ArrayList<>();
        for(Movie movie : movies) {
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            MovieDto movieDto = new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl
            );
            response.add(movieDto);
        }
        return new MoviePageResponse(
                response ,
                pageNumber,
                pageSize ,
                (int) page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()

        );

   }

    @Override
    public MoviePageResponse getAllMovieWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String dir) {
       Sort sort = dir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Movie> page = movieRepo.findAll(pageable);
        List<Movie> movies  = page.getContent();

        List<MovieDto> response = new ArrayList<>();
        for(Movie movie : movies) {
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            MovieDto movieDto = new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getStudio(),
                    movie.getMovieCast(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    posterUrl
            );
            response.add(movieDto);
        }
        return new MoviePageResponse(
                response ,
                pageNumber,
                pageSize ,
                (int) page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()

        );

    }
}
