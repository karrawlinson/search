package com.carina.search.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
/**
 * Movie search result
 * 
 * @author Karen Rawlinson
 *
 */
@Data
@AllArgsConstructor
public class SearchResult {

	private String id;
	
	private Float score;
	
	private Movie content;
}
