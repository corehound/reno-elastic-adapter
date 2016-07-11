package org.corehound.reno.adapter.elastic;

import java.net.InetAddress;

import org.corehound.reno.adapter.AdapterFactory;
import org.corehound.reno.adapter.admin.AdminService;
import org.corehound.reno.adapter.index.IndexService;
import org.corehound.reno.adapter.search.SearchService;
import org.corehound.reno.plugin.Config;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticAdapterFactory extends AdapterFactory {
	
	private static Logger logger = LoggerFactory.getLogger(ElasticAdapterFactory.class);
	
	private static Client client;
	private static int connectionAttempts = 0; 

	@Override
	protected SearchService doGetSearchService() {
		return new ElasticSearchService();
	}

	@Override
	protected IndexService doGetIndexService() {
		return new ElasticIndexService();
	}

	@Override
	protected AdminService doGetAdminService() {
		return new ElasticAdminService();
	}
	
	static Client getClient(){
		if(client == null){
			if(connectionAttempts < 5){
				try {
					String clusterName = Config.get("elastic.cluster.name");
					String ip = Config.get("elastic.ip");
					int elasticPort = Integer.parseInt(Config.get("elastic.port"));
					logger.info("Initializing elastic client(clusterName={}, ip={}, port={}) attempt {}",clusterName, ip, elasticPort, ++connectionAttempts);
					Settings settings = Settings.settingsBuilder().put("cluster.name", clusterName).build();
					client = TransportClient.builder().settings(settings).build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), elasticPort));
				} catch (Exception e){
					logger.error(e.getMessage(), e);
				}
			}
		}
		return client;
	}
}
