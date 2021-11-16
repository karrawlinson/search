package com.carina.search.service;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.SearchHits;

import com.carina.search.domain.Movie;
import com.carina.search.domain.SearchResponse;
import com.carina.search.domain.SearchResult;
import com.carina.search.domain.Suggestion;
import com.carina.search.domain.SuggestionResponse;
/**
 * Integration test that requires elasticsearch to be running on localhost:9200
 * 
 * This should have a dedicated index for integration test
 * which is created/updated/deleted during testing with mock data
 * however for simplicity, just using the default movie index for now
 * 
 * @author Karen Rawlinson
 *
 */
@SpringBootTest
public class ElasticsearchServiceTests {

	@Autowired
	ElasticsearchService elasticsearchService;

	@Test
	void testAutosuggestOk() {

		SuggestionResponse suggestions = elasticsearchService.autosuggest("mup", 5);
		assert(suggestions != null);
		assert(suggestions.getSuggestions().size() == 5);
		assert(suggestions != null);
		for(Suggestion sugg: suggestions.getSuggestions()) {
			assert(sugg.getId().length() > 0);
			assert(sugg.getTitle().length() > 0);
		}
	}
	
	@Test
	void testAutosuggestLimit() {

		SuggestionResponse suggestions = elasticsearchService.autosuggest("mup", 3);
		assert(suggestions != null);
		for(Suggestion sugg: suggestions.getSuggestions()) {
			assert(sugg.getId().length() > 0);
			assert(sugg.getTitle().length() > 0);
		}
		assert(suggestions.getSuggestions().size() == 3);
		assert(suggestions.getTotalHits() > 0);
	}
	
	@Test
	void testSearchTitleOk() {

		SearchResponse response = elasticsearchService.searchByTitle("star", 0, 10);
		assert(response != null);
		assert(response.getSearchResults().size() == 10);
		for(SearchResult result: response.getSearchResults()) {
			assert(result.getId().length() > 0);
			assert(result.getContent().getId().length() > 0);
			assert(result.getContent().getOriginalTitle().length() > 0);
		}
	}
	
	@Test
	void testSearchTitleLimit() {

		SearchResponse response = elasticsearchService.searchByTitle("star", 0, 3);
		assert(response != null);
		assert(response.getSearchResults().size() == 3);
		for(SearchResult result: response.getSearchResults()) {
			assert(result.getId().length() > 0);
			assert(result.getContent().getId().length() > 0);
			assert(result.getContent().getOriginalTitle().length() > 0);
		}
	}
	
	@Test
	void testSearchAllOk() {

		SearchResponse response = elasticsearchService.searchAll("star", 0, 10);
		assert(response != null);
		assert(response.getSearchResults().size() == 10);
		for(SearchResult result: response.getSearchResults()) {
			assert(result.getId().length() > 0);
			assert(result.getContent().getId().length() > 0);
			assert(result.getContent().getOriginalTitle().length() > 0);
		}
	}
	
	@Test
	void testSearchAllLimit() {

		SearchResponse response = elasticsearchService.searchAll("star", 0, 3);
		assert(response != null);
		assert(response.getSearchResults().size() == 3);
		for(SearchResult result: response.getSearchResults()) {
			assert(result.getId().length() > 0);
			assert(result.getContent().getId().length() > 0);
			assert(result.getContent().getOriginalTitle().length() > 0);
		}
	}
	
	@Test
	void testMoreLikeThisFail() {

		SearchResponse response = elasticsearchService.moreLikeThis("star", 0, 10);
		assert(response != null);
		assert(response.getSearchResults().size() == 0);		
	}
}
