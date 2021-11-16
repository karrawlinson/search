
package com.carina.search.service;

import org.springframework.data.elasticsearch.core.query.MoreLikeThisQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.ArrayList;
import java.util.List;
import org.elasticsearch.index.query.MultiMatchQueryBuilder.Type;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import com.carina.search.domain.Movie;
import com.carina.search.domain.SearchResponse;
import com.carina.search.domain.SearchResult;
import com.carina.search.domain.Suggestion;
import com.carina.search.domain.SuggestionResponse;
/**
 * ElasticsearchService that uses Spring Data Elasticsearch to search elastic
 * 
 * https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#elasticsearch.operations
 * 
 * @author Karen Rawlinson
 *
 */
@Service
public class ElasticsearchService {
	 
	@Autowired
	private ElasticsearchOperations elasticsearchOperations;
	 
	@Value( "${elasticsearch.search.index}" )
	private String searchIndex;
	
	@Value( "${query.morelikethis.fields}" )
	private String[] moreLikeThisFields;
	
	@Value( "${query.searchall.fields}" )
	private String[] searchAllFields;
	
	@Value( "${query.searchall.rating}" )
	private String ratingField;
	
	@Value( "${query.morelikethis.min}" )
	private int minTermFreq;
	
	@Value( "${query.morelikethis.max}" )
	private int maxQueryTerms;
	
	@Value( "${query.title.field}" )
	private String titleField;
	
	public Movie getDocument(String id) {

		return elasticsearchOperations.get(id, Movie.class, IndexCoordinates.of(searchIndex));

	}
	
	public SearchResponse searchByTitle(String title, Integer pageNum, Integer limit) {
		
		Criteria criteria = new Criteria(titleField).matches(title);
		Query query = new CriteriaQuery(criteria);
		PageRequest page = PageRequest.of(pageNum, limit);
		query.setPageable(page);
		SearchHits<Movie> hits = elasticsearchOperations.search(query, Movie.class, IndexCoordinates.of(searchIndex));
		return formatSearchResults(pageNum, limit, hits);
		
	}
	
	public SearchResponse moreLikeThis(String id, Integer pageNum, Integer limit) {
		
		MoreLikeThisQuery moreLikeThisQuery = new MoreLikeThisQuery();
		moreLikeThisQuery.setId(id);
		moreLikeThisQuery.addFields(moreLikeThisFields);
		moreLikeThisQuery.setMinTermFreq(minTermFreq);
		moreLikeThisQuery.setMaxQueryTerms(maxQueryTerms);

		PageRequest page = PageRequest.of(pageNum, limit);
		moreLikeThisQuery.setPageable(page);

		SearchHits<Movie> hits = elasticsearchOperations.search(moreLikeThisQuery, Movie.class, IndexCoordinates.of(searchIndex));
		return formatSearchResults(pageNum, limit, hits);
		
	}

	public SearchResponse searchAll(String query, Double minRating, Integer pageNum, Integer limit) {
		
		QueryBuilder matchQuery = QueryBuilders.multiMatchQuery(query, searchAllFields);
		PageRequest page = PageRequest.of(pageNum, limit);		
		Query searchQuery = new NativeSearchQueryBuilder()
				  .withQuery(matchQuery)
				  .withFilter(QueryBuilders.rangeQuery(ratingField).gte(minRating))
				  .withPageable(page)
				  .build();
		
		SearchHits<Movie> hits = elasticsearchOperations.search(searchQuery, Movie.class, IndexCoordinates.of(searchIndex));
		return formatSearchResults(pageNum, limit, hits);
		
	}
	
	public SuggestionResponse autosuggest(String query, Integer limit) {

		// note: the titleField was mapped as type search_as_you_type on index creation which allows matching to ngram tokens
		// https://www.elastic.co/guide/en/elasticsearch/reference/current/search-as-you-type.html
		QueryBuilder matchQuery = QueryBuilders
				.multiMatchQuery(query, titleField, titleField + "._2gram", titleField + "._3gram")
				.type(Type.BOOL_PREFIX);

		Query searchQuery = new NativeSearchQueryBuilder().withQuery(matchQuery)
				.withPageable(PageRequest.of(0, limit)).build();
		SearchHits<Movie> searchSuggestions = elasticsearchOperations.search(searchQuery, Movie.class,
				IndexCoordinates.of(searchIndex));
		
		return formatSuggestions(searchSuggestions);
		
	}

	// utility method to map search results to suggestion format
	private SuggestionResponse formatSuggestions(SearchHits<Movie> searchSuggestions) {
		List<Suggestion> suggestions = new ArrayList<Suggestion>();
		if (searchSuggestions.getSearchHits() != null) {
			searchSuggestions.getSearchHits().forEach(searchHit -> {
				suggestions.add(new Suggestion(searchHit.getContent().getId(), searchHit.getContent().getOriginalTitle()));
			});
		}
		return new SuggestionResponse(searchSuggestions.getTotalHits(), suggestions);
	}
	
	// utility method to simplify search results
	private SearchResponse formatSearchResults(Integer pageNum, Integer limit, SearchHits<Movie> hits) {
		
		List<SearchResult> searchResults = new ArrayList<SearchResult>();
		if(hits != null && hits.getSearchHits() != null ) {
			hits.getSearchHits().forEach(searchHit -> {
				searchResults.add(new SearchResult(searchHit.getId(), searchHit.getScore(), searchHit.getContent()));
			});
			
		} 
		return new SearchResponse(hits.getTotalHits(), searchResults, pageNum, limit);	
		
	}
	

}
