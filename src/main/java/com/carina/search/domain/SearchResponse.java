package com.carina.search.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * API response for search api calls
 * 
 * @author Karen Rawlinson
 */
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class SearchResponse {

	@JsonProperty("total_hits")
	private Long totalHits;
	
	@JsonProperty("search_results")
	private List<SearchResult> searchResults;
	
	private Integer page;
	private Integer limit;
}
