package org.corehound.reno.adapter.elastic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.corehound.reno.adapter.search.Index;
import org.corehound.reno.adapter.search.SearchException;
import org.corehound.reno.adapter.search.SearchResults;
import org.corehound.reno.adapter.search.SearchService;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TemplateQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticSearchService implements SearchService {

	private static final Logger LOG = LoggerFactory.getLogger(ElasticIndexService.class);
	
	public SearchResults search(String[] index, String query, String queryTemplate, int from, int size)
			throws SearchException {

		long startTime = System.currentTimeMillis();

		if (index == null) {
			index = new String[0];
		}

		LOG.trace("Search requested. Index: {}, Query: {}", index, query);

		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put(ElasticAdapterConstants.TEMPLATE_QUERY_VAR, query);
		
		TemplateQueryBuilder tqb = QueryBuilders.templateQuery(queryTemplate, vars);

		SearchRequestBuilder searchRequestBuilder = ElasticAdapterFactory.getClient().prepareSearch();
		
		searchRequestBuilder.setIndices(index);
		searchRequestBuilder.setQuery(tqb);
		searchRequestBuilder.setFrom(from);
		searchRequestBuilder.setSize(size);
		
		SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

		SearchResults results = new SearchResults();

		Iterator<SearchHit> iterator = searchResponse.getHits().iterator();
		while (iterator.hasNext()) {
			SearchHit hit = iterator.next();
			ElasticSearchItem item = new ElasticSearchItem(hit);
			results.add(item);
		}

		LOG.debug("Search finished. Index: {}, Query: {}, Text: {}, Time: {}, Hits: {}", index, query,
				System.currentTimeMillis() - startTime, results.size());

		return results;

	}

	public SearchResults search(Index[] indexes, String text, String queryTamplete, int from, int size)
			throws SearchException {
		// TODO Auto-generated method stub
		return null;
	}

}
