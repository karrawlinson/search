package com.carina.search.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * API response for suggestion api calls
 * 
 * @author Karen Rawlinson
 */
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class SuggestionResponse {

	@JsonProperty("total_hits")
	private Long totalHits;
	
	private List<Suggestion> suggestions;
}
