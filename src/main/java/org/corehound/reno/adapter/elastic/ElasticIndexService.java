package org.corehound.reno.adapter.elastic;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.corehound.reno.adapter.elastic.ElasticAdapterConstants;
import org.corehound.reno.adapter.index.IndexException;
import org.corehound.reno.adapter.index.IndexService;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;

public class ElasticIndexService implements IndexService {

	private static final Logger LOG = LoggerFactory.getLogger(ElasticIndexService.class);

	public String index(String index, String url, String title, String content, Map<String, Object> metadata)
			throws IndexException {

		long startTime = System.currentTimeMillis();
		String id;

		LOG.trace("Document indexing requested. Index: {} Url: {}", index, url);

		try {

			id = hash(url);
			
			Map<String, Object> source = new HashMap<String, Object>();
			source.put(ElasticAdapterConstants.URL, url);
			source.put(ElasticAdapterConstants.TITLE, title);
			source.put(ElasticAdapterConstants.CONTENT, content);
			source.putAll(metadata);

			Client client = ElasticAdapterFactory.getClient();

			IndexRequestBuilder requestBuilder = client.prepareIndex(index, ElasticAdapterConstants.DOCUMENT_TYPE, id);
			requestBuilder.setSource(source);
			requestBuilder.get();

		} catch (Exception e) {
			throw new IndexException("Document indexing failed. Index: " + index, e);
		}

		LOG.debug("Document indexing finished. Index: {} Url: {} Time: {}", index, url,
				System.currentTimeMillis() - startTime);

		return id;
	}

	private String hash(String url) throws NoSuchAlgorithmException {
		MessageDigest mdEnc = MessageDigest.getInstance("MD5");
		mdEnc.update(url.getBytes(), 0, url.length());
		return new BigInteger(1, mdEnc.digest()).toString(16);
	}

}
