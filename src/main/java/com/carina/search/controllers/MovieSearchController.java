package com.carina.search.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.carina.search.domain.Movie;
import com.carina.search.domain.SearchResponse;
import com.carina.search.domain.SuggestionResponse;
import com.carina.search.service.ElasticsearchService;

import io.swagger.v3.oas.annotations.tags.Tag;
/**
 * Rest API to search movies 
 * 
 * @author Karen Rawlinson
 *
 */
@RestController
@Tag(name = "Search")
@RequestMapping("movie")
public class MovieSearchController {

	@Autowired
	private ElasticsearchService searchService;

	/**
	 * Typeahead functionality for movie searches
	 * @param query
	 * @param limit
	 * @return
	 */
	@RequestMapping(value = { "/autosuggest" }, produces = {MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public SuggestionResponse autosuggest(@RequestParam String query, @RequestParam(defaultValue = "5") Integer limit) {
		return searchService.autosuggest(query, limit);
	}

	/**
	 * Find movie by document id
	 * @param id
	 * @param page
	 * @param limit
	 * @return
	 */
	@RequestMapping(value = { "/{id}" }, produces = {MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public Movie findById(@PathVariable String id) {
		
		Movie movie = searchService.getDocument(id);
		if(movie == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found");
		}
		return movie;
	}
	
	/**
	 * Search similar movies for more like this functionaity
	 * @param id
	 * @param page
	 * @param limit
	 * @return
	 */
	@RequestMapping(value = { "/{id}/similar" }, produces = {MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public SearchResponse searchSimilar(@PathVariable String id, @RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "100") Integer limit ) {
		return searchService.moreLikeThis(id, page, limit);
	}

	/**
	 * Search documents and allow filtering based on facets
	 * @param query
	 * @param page
	 * @param limit
	 * @return
	 */
	@RequestMapping(value = { "/search" }, produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public SearchResponse search(@RequestParam String query, @RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "100") Integer limit) {
		return searchService.searchAll(query, page, limit);
	}

	/**
	 * Simple title search for movies
	 * @param query
	 * @param page
	 * @param limit
	 * @return
	 */
	@RequestMapping(value = { "/titleSearch" }, produces = {MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
	public SearchResponse searchByTitle(@RequestParam String query,@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "100") Integer limit) {
		return searchService.searchByTitle(query, page, limit);
	}
}
