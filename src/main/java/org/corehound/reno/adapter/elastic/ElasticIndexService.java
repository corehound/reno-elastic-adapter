package org.corehound.reno.adapter.elastic;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.corehound.reno.adapter.index.IndexException;
import org.corehound.reno.adapter.index.IndexService;

public class ElasticIndexService implements IndexService {

	public String index(String index, String url, String title, String content,
			Map<String, Object> metadata) throws IndexException {
		// TODO Auto-generated method stub
		return null;
	}
	
	private String hash(String url) throws NoSuchAlgorithmException{
		MessageDigest mdEnc = MessageDigest.getInstance("MD5");
		mdEnc.update(url.getBytes(), 0, url.length());
		return new BigInteger(1, mdEnc.digest()).toString(16);
	}

}
