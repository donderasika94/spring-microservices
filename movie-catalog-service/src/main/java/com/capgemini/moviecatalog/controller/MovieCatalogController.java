package com.capgemini.moviecatalog.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.capgemini.moviecatalog.entity.Movie;
import com.capgemini.moviecatalog.entity.MovieCatalog;
import com.capgemini.moviecatalog.entity.Rating;
import com.capgemini.moviecatalog.entity.UserRating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogController {

	@Autowired
	private RestTemplate restTemplate;
	
	@GetMapping("/{userId}")
	public ResponseEntity<List<MovieCatalog>> getMovieCatalog(@PathVariable String userId){
		//Get the movie ratings for the user.
		
		UserRating userRatings = restTemplate.getForEntity("http://localhost:8082/ratings/" +userId,UserRating.class ).getBody();
		
		List<Rating> movieRatings = userRatings.getUserRatings();
		List<MovieCatalog> movieCatalog = new ArrayList<MovieCatalog>();
		
		for(Rating rating:movieRatings) {
			
			Movie movie = restTemplate.getForEntity("http://localhost:8083/movies/"+rating.getMovieId(), Movie.class).getBody();
			
			movieCatalog.add(new MovieCatalog(movie.getMovieTitle(),movie.getMovieDescription(),rating.getRating()));
		}
		
		return new ResponseEntity<List<MovieCatalog>>(movieCatalog,HttpStatus.OK);
		//get movie information
		
		//combine and return MovieCatalog
		
	}
	
}
