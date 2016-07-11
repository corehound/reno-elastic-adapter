package org.corehound.reno.adapter.elastic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.corehound.reno.adapter.admin.AdminException;
import org.corehound.reno.adapter.admin.AdminService;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsRequest;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.AdminClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticAdminService implements AdminService {

	private static final Logger LOG = LoggerFactory.getLogger(ElasticAdminService.class);

	public void createIndex(String name, String definition) throws AdminException {

		LOG.debug("Index creating requested. Name: {}", name);

		AdminClient client = ElasticAdapterFactory.getClient().admin();

		try {
			client.indices().prepareCreate(name).setSettings(definition).get();

		} catch (Exception e) {
			LOG.error("Index creating failed - {}. Name: {}", e.getMessage(), name, e);
			throw new AdminException("Index creating failed - " +  e.getMessage() + ". Name: " + name, e);
		}

		LOG.info("Index created. Name: {}", name);

	}

	public void deleteIndex(String name) throws AdminException {

		LOG.debug("Index deleting requested. Name: {}", name);

		AdminClient client = ElasticAdapterFactory.getClient().admin();

		try {
			client.indices().prepareDelete(name).get();

		} catch (Exception e) {
			LOG.error("Index deleting failed - {}. Name: {}", e.getMessage(), name, e);
			throw new AdminException("Index deleting failed - " + e.getMessage() + ". Name: " + name, e);
		}

		LOG.info("Index deleted. Name: {}", name);

	}

	public String getIndexDefinition(String name) {
		
		LOG.debug("Index definition getting requested.");

		AdminClient client = ElasticAdapterFactory.getClient().admin();

		String definition = null;
		
		try {
			
			//client.indices().getSettings(null).get

			LOG.debug("Index definition getting finished. Value: {}", definition);
			
		} catch (Exception e) {
			LOG.error("Index definition getting failed - {}", e.getMessage(), e);
			//TODO throw exception
			//throw new AdminException("Index deleting failed - " + e.getMessage() + ". Name: " + name, e);
		}

		return definition;
	}

	
	public List<String> getIndexNames() {
		
		LOG.debug("Index list getting requested.");

		AdminClient client = ElasticAdapterFactory.getClient().admin();

		String[] indexArray = new String[0];
		
		try {
			indexArray = client.cluster().prepareState().execute().actionGet().getState()
		    .getMetaData().concreteAllIndices();

			LOG.debug("Index list getting finished. Values: {}", Arrays.toString(indexArray));
			
		} catch (Exception e) {
			LOG.error("Index list getting failed - {}", e.getMessage(), e);
			//TODO throw exception
			//throw new AdminException("Index deleting failed - " + e.getMessage() + ". Name: " + name, e);
		}

		return new ArrayList<String>(Arrays.asList(indexArray));
	}

	public void updateSynonyms(String indexName, String synonyms) throws AdminException {
		// TODO Auto-generated method stub

	}

	public String getSynonyms(String indexName) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @param indexName
	 * @return
	 */
	private boolean isIndexExists(String indexName) {

		AdminClient client = ElasticAdapterFactory.getClient().admin();
		boolean exists = client.indices().prepareExists(indexName).execute().actionGet().isExists();
		LOG.debug("Index exists. Name: {} Exists: {}", indexName, exists);

		return exists;
	}
	


}
