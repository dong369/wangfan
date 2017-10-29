package com.wangfanpinche.utils.http;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpsUtil {

	/**
	 * 
	 * @param url
	 *            "https://api.mch.weixin.qq.com/secapi/pay/refund"
	 * @param entity
	 * @param instance
	 *            "PKCS12"
	 * @param filename
	 *            "G:/apiclient_cert.p12"
	 * @param password
	 *            "1411235902"
	 * @return
	 */
	public static String postStringEntity(String url, String entity, String instance, String filename, String password) {
		KeyStore keyStore = null;
		try (FileInputStream instream = new FileInputStream(new File(filename))) {
			keyStore = KeyStore.getInstance(instance);
			keyStore.load(instream, password.toCharArray());
			SSLContext sslcontext = org.apache.http.ssl.SSLContexts.custom().loadKeyMaterial(keyStore, password.toCharArray()).build();
			// Allow TLSv1 protocol only
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
			try (CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build()) {

				HttpPost httppost = new HttpPost(url);
				httppost.setEntity(new StringEntity(entity));
				try(CloseableHttpResponse response = httpclient.execute(httppost);){
					HttpEntity respEntity = response.getEntity();
					return EntityUtils.toString(respEntity, "UTF-8");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

}
