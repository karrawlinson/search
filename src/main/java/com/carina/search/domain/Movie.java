package com.carina.search.domain;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

/**
 * Represents movie document in Elasticsearch cluster
 * 
 * @author Karen Rawlinson
 */
import lombok.AllArgsConstructor;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@AllArgsConstructor
@Document(indexName = "movies")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie {

	private String id;

	@JsonProperty("original_title") @Field("original_title")
	private String originalTitle;

	private String year;
	private String genre;
	private Double duration;
	private String country;
	private String language;
	private String director;
	private String writer;

	@JsonProperty("production_company") @Field("production_company")
	private String productionCompany;

	private String actors;
	private String description;

	@JsonProperty("avg_vote") @Field("avg_vote")
	private Double avgVote;

	private Double votes;

}
