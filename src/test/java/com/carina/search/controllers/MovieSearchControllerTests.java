package com.carina.search.controllers;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import com.carina.search.domain.Movie;
import com.carina.search.domain.SearchResponse;
import com.carina.search.domain.SearchResult;
import com.carina.search.domain.Suggestion;
import com.carina.search.domain.SuggestionResponse;
import com.carina.search.service.ElasticsearchService;

/**
 * Unit test for MovieSearchController
 * 
 * @author Karen Rawlinson
 *
 */
@WebMvcTest
public class MovieSearchControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ElasticsearchService service;

	private SuggestionResponse mockSuggestions() {
		List<Suggestion> suggestions = new ArrayList<>();
		suggestions.add(new Suggestion("1", "title"));
		suggestions.add(new Suggestion("2", "title"));
		return new SuggestionResponse(Long.valueOf(10), suggestions);

	}

	private SearchResponse mockSearches() {
		List<SearchResult> list = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			Movie movie = new Movie("id" + i, "title" + i, "199" + i, "Genre" + i, (double) i * 10, "USA" + i,
					"English", "Mr. Director" + i, "Ms. Writer" + i, "Pixar" + i, "Actor1, Actor2, Actor3",
					"A test movie about testing", 1.2, 0.0);
			SearchResult result = new SearchResult("id" + i, (float) i % 10, movie);
			list.add(result);
		}

		return new SearchResponse((long) 400, list, 0, 20);
	}

	@Test
	public void testAutosuggestOk() throws Exception {
		when(service.autosuggest("mov", 5)).thenReturn(mockSuggestions());
		this.mockMvc.perform(get("/movie/autosuggest?query=mov")).andDo(print())
				.andExpect(jsonPath("$.suggestions").isNotEmpty())
				.andExpect(jsonPath("$.suggestions[0].id").isNotEmpty());

	}

	@Test
	public void testAutosuggestBadRequest() throws Exception {

		this.mockMvc.perform(get("/movie/autosuggest")).andDo(print()).andExpect(status().isBadRequest());
	}

	@Test
	public void testGetMovieOk() throws Exception {
		Movie movie = mockSearches().getSearchResults().get(0).getContent();
		when(service.getDocument("id1")).thenReturn(movie);
		this.mockMvc.perform(get("/movie/id1")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.original_title").isNotEmpty()).andExpect(jsonPath("$.id").isNotEmpty());
	}

	@Test
	public void testGetMovieNotFound() throws Exception {

		this.mockMvc.perform(get("/movie/badid")).andDo(print()).andExpect(status().isNotFound());

	}

	@Test
	public void testSearchTitleBadRequest() throws Exception {

		this.mockMvc.perform(get("/movie/titleSearch")).andDo(print()).andExpect(status().isBadRequest());
	}
	@Test
	public void testSearchTitleOk() throws Exception {
		when(service.searchByTitle("mov", 0, 20)).thenReturn(mockSearches());
		this.mockMvc.perform(get("/movie/titleSearch?query=mov&page=0&limit=20")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.search_results").isNotEmpty())
				.andExpect(jsonPath("$.search_results[0].id").isNotEmpty());
	}

	@Test
	public void testSearchOk() throws Exception {
		when(service.searchAll("mov", 0.0, 0, 20)).thenReturn(mockSearches());
		this.mockMvc.perform(get("/movie/search?query=mov&page=0&limit=20")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.search_results").isNotEmpty())
				.andExpect(jsonPath("$.search_results[0].id").isNotEmpty());
	}

	@Test
	public void testSearchBadRequest() throws Exception {

		this.mockMvc.perform(get("/movie/search")).andDo(print()).andExpect(status().isBadRequest());
	}

	@Test
	public void testSearchSimilarOk() throws Exception {
		when(service.moreLikeThis("test", 0, 20)).thenReturn(mockSearches());
		this.mockMvc.perform(get("/movie/test/similar?page=0&limit=20")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.search_results").isNotEmpty())
				.andExpect(jsonPath("$.search_results[0].id").isNotEmpty());
	}

	@Test
	public void testSearchSimilarBadRequest() throws Exception {

		this.mockMvc.perform(get("/movie/similar")).andDo(print()).andExpect(status().isNotFound());
	}

}
