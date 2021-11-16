package com.carina.search.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
/**
 * Suggestion used for autosuggest typeahead functionality
 * 
 * @author Karen Rawlinson
 *
 */
@Data
@AllArgsConstructor
public class Suggestion {

	private String id;	
	private String title;

}
