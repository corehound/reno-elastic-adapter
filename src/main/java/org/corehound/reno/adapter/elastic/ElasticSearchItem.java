package org.corehound.reno.adapter.elastic;

import java.util.Map;

import org.corehound.reno.adapter.search.SearchItem;
import org.corehound.reno.adapter.elastic.ElasticAdapterConstants;
import org.elasticsearch.search.SearchHit;

public class ElasticSearchItem implements SearchItem {

	private String url;
	private String title;
	private String content;
	private Map<String, Object> map;
	private Double score;

	public ElasticSearchItem() {
		super();
	}

	public ElasticSearchItem(SearchHit searchHit) {
		
		map = searchHit.getSource();
		
		url = (String) map.remove(ElasticAdapterConstants.URL);
		title = (String) map.remove(ElasticAdapterConstants.TITLE);
		content = (String) map.remove(ElasticAdapterConstants.CONTENT);
		
		score = Double.valueOf((double) searchHit.getScore());
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public Double getScore() {
		return score;
	}

}
