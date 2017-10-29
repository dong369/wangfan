package com.wangfanpinche.utils.http;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeanUtils;
/**
 * Http工具类
 * 
 * @author Thanatos
 *
 */
public class HttpUtils {

	/**
	 * 通过GET方法发送参数
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String get(String url, Map<String, String> params) {
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			URIBuilder builder = new URIBuilder(url);
			params.forEach((key, value) -> {
				builder.addParameter(key, value);
			});
			HttpGet httpGet = new HttpGet(builder.build());
			try(CloseableHttpResponse response = httpclient.execute(httpGet)){
				HttpEntity entity = response.getEntity();
				return EntityUtils.toString(entity, "UTF-8");
			}
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 通过POST方法发送参数
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String post(String url, Map<String, String> params) {
		// 创建默认的httpClient实例.
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			URIBuilder builder = new URIBuilder(url);
 			
			HttpPost httpPost = new HttpPost(builder.build());
			List<BasicNameValuePair> pairs = params.entrySet()
												   .stream()
												   .map( e-> new BasicNameValuePair(e.getKey(), e.getValue()))
												   .collect(Collectors.toList());
			httpPost.setEntity(new UrlEncodedFormEntity(pairs));
			try(CloseableHttpResponse response = httpclient.execute(httpPost)){
				HttpEntity entity = response.getEntity();
				return EntityUtils.toString(entity, "UTF-8");
			}
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String postEntity(String url, String entity){
		if (url == null || url.length() == 0) {
			System.out.println( "httpPost, url is null");
            return null;
        }

        HttpClient httpClient = getNewHttpClient();

        HttpPost httpPost = new HttpPost(url);

        try {
            httpPost.setEntity(new StringEntity(entity));
            httpPost.setHeader("Accept", "application/json;charset=UTF-8");
            httpPost.setHeader("Content-type", "application/json;charset=UTF-8");

            HttpResponse resp = httpClient.execute(httpPost);
            if (resp.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                System.out.println("httpGet fail, status code = " + resp.getStatusLine().getStatusCode());
                return null;
            }

            return new String(EntityUtils.toByteArray(resp.getEntity()), "UTF-8");
        } catch (Exception e) {
            System.out.println("httpPost exception, e = " + e.getMessage());
            e.printStackTrace();
            return null;
        }
	}
	public static byte[] postEntityGetByteArray(String url, String entity){
		if (url == null || url.length() == 0) {
			System.out.println( "httpPost, url is null");
			return null;
		}
		
		HttpClient httpClient = getNewHttpClient();
		
		HttpPost httpPost = new HttpPost(url);
		
		try {
			httpPost.setEntity(new StringEntity(entity));
			httpPost.setHeader("Accept", "application/json;charset=UTF-8");
			httpPost.setHeader("Content-type", "application/json;charset=UTF-8");
			
			HttpResponse resp = httpClient.execute(httpPost);
			if (resp.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				System.out.println("httpGet fail, status code = " + resp.getStatusLine().getStatusCode());
				return null;
			}
			
			return EntityUtils.toByteArray(resp.getEntity());
		} catch (Exception e) {
			System.out.println("httpPost exception, e = " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	 private static HttpClient getNewHttpClient() {
	        try {
	            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
	            trustStore.load(null, null);

	            SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
	            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

	            HttpParams params = new BasicHttpParams();
	            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

	            SchemeRegistry registry = new SchemeRegistry();
	            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	            registry.register(new Scheme("https", sf, 443));

	            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

	            return new DefaultHttpClient(ccm, params);
	        } catch (Exception e) {
	            return new DefaultHttpClient();
	        }
	    }
	
	/**
	 * 把一个对象序列化成Http的参数形式
	 * 	   例如   User{ username:"abc", age:16}
	 *    序列化后   username=abc&age=16
	 * @param target
	 * @return
	 */
	public static <T> String serializeObject(T target) {
		return serializeObject(target, true, true);
	}
	
	/**
	 * 把一个对象序列化成Http的参数形式
	 * 	   例如   User{ username:"abc", age:16}
	 *    序列化后   username=abc&age=16
	 * @param target 目标对象
	 * @param sortable 是否排序
	 * @param isEncode 是否进行UTF8编码
	 * @return
	 * @author thanatos
	 */
	public static <T> String serializeObject(T target, Boolean sortable, boolean isEncode){
		
		StringBuilder sb = new StringBuilder();

		Class<?> actualEditable = target.getClass();
		PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
		if(sortable){
			Arrays.sort(targetPds, (a, b) -> a.getName().compareTo(b.getName()));
		}
		
		for (PropertyDescriptor targetPd : targetPds) {
			Method readMethod = targetPd.getReadMethod();
			if(readMethod != null && targetPd.getWriteMethod()!= null){
				try {
					Object invoke = readMethod.invoke(target);
					if(invoke == null){
						continue;
					} else if(Collection.class.isAssignableFrom(readMethod.getReturnType())){
						// XXX 数组的比较麻烦。需要拼接，先不写(暂时用不到)
						//System.out.println(invoke);
					} else {
						if(isEncode){
							sb.append( "&" + targetPd.getName() + "=" + URLEncoder.encode(invoke.toString(), "UTF-8"));
						} else {
							sb.append( "&" + targetPd.getName() + "=" + invoke.toString());
						}
					}
					
				} catch (Exception ex) {
					throw new RuntimeException("转换出错:" + targetPd.getName(), ex);
				}
			}
		}
		return sb.deleteCharAt(0).toString();
	}
	
	 private static class SSLSocketFactoryEx extends SSLSocketFactory {

	        SSLContext sslContext = SSLContext.getInstance("TLS");

	        public SSLSocketFactoryEx(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
	            super(truststore);

	            TrustManager tm = new X509TrustManager() {

	                public X509Certificate[] getAcceptedIssuers() {
	                    return null;
	                }

	                @Override
	                public void checkClientTrusted(X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
	                }

	                @Override
	                public void checkServerTrusted(X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
	                }
	            };

	            sslContext.init(null, new TrustManager[]{tm}, null);
	        }

	        @Override
	        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
	            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
	        }

	        @Override
	        public Socket createSocket() throws IOException {
	            return sslContext.getSocketFactory().createSocket();
	        }
	    }

}
