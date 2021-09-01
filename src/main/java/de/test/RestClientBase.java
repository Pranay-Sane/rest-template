package de.test;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.expression.common.TemplateAwareExpressionParser;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.client.AbstractClientHttpRequest;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;

import javax.net.ssl.SSLContext;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;
/**
 * @author iternovykh
 *         Date: 22.04.2016.
 */
public class RestClientBase
{
    private static final String PROXY_HOST = "localhost";
    private static final int PROXY_PORT = 3128;
    private static final String PROXY_USER = "admin";
    private static final String PROXY_PASSWORD = "admin";

    private RestTemplate template;

    protected RestTemplate getTemplate()
    {
        if (template == null)
        {
//            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//            credentialsProvider.setCredentials(new AuthScope(PROXY_HOST, PROXY_PORT),
//                    new UsernamePasswordCredentials(PROXY_USER, PROXY_PASSWORD));

//            HttpClientBuilder clientBuilder = HttpClientBuilder.create();
//
//            clientBuilder.useSystemProperties();
//            clientBuilder.setProxy(new HttpHost(PROXY_HOST, PROXY_PORT));
//            clientBuilder.setDefaultCredentialsProvider(credentialsProvider);
//            clientBuilder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());
//            
//            
//            CloseableHttpClient client = clientBuilder.build();
//            
//            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
//            factory.setHttpClient(client);

//            template = new RestTemplate();
//            template.setRequestFactory(factory);
//        	template = new RestTemplate();
//        	template.setRequestFactory(disableSSl());
//        	final SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory() {
//        		@Override
//        		protected void prepareConnection(HttpURLConnection conn, String method) throws IOException {
//        			ProxiedHttpsConnection connection = new ProxiedHttpsConnection(
//        					new URL("https://smartcentraltest.crisil.com:443/api/smartservice/getHealthCheck"), 
//        					"ec2-65-0-222-51.ap-south-1.compute.amazonaws.com", 3128, "admin", "admin");
//        			super.prepareConnection(connection, method);
//        		}
//        	};
//        	final SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
//        	final InetSocketAddress address = new InetSocketAddress(PROXY_HOST, PROXY_PORT);
//        	final Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
//        	factory.setProxy(proxy);
//        	
//        	System.setProperty("https.proxyHost", PROXY_HOST);
//            System.setProperty("https.proxyPort", "3128");
//            System.setProperty("http.proxyHost", PROXY_HOST);
//            System.setProperty("http.proxyPort", "3128");
        	
        	final SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory() {
        		@Override
        		protected HttpURLConnection openConnection(URL url, Proxy proxy) throws IOException {
        			ProxiedHttpsConnection conn = new ProxiedHttpsConnection(url, PROXY_HOST, PROXY_PORT, PROXY_USER, PROXY_PASSWORD);
        			return conn;
        		}
        	};
//        	SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
//        	final InetSocketAddress address = new InetSocketAddress(PROXY_HOST, PROXY_PORT);
//        	final Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
//        	factory.setProxy(proxy);
        	
        	template = new RestTemplate(factory);
//        	template.setErrorHandler(new ResponseErrorHandler() {
//	    	    public boolean hasError(ClientHttpResponse response) throws IOException {
//	    	    	System.out.print(response);
//	    	        return false;
//	    	    }
//	    	    public void handleError(ClientHttpResponse response) throws IOException {
//	    	    	System.out.print(response);
//	    	    }
//    	    });
//        	template.getInterceptors().add(new BasicAuthenticationInterceptor(PROXY_USER, PROXY_PASSWORD));
        }

        return template;
    }
    
    private HttpComponentsClientHttpRequestFactory disableSSl() {
        TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                return true;
            }
        };
        SSLContext sslContext = null;
        try {
            sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(new AuthScope(PROXY_HOST, PROXY_PORT),
              new UsernamePasswordCredentials(PROXY_USER, PROXY_PASSWORD));
        
        HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        
        clientBuilder.useSystemProperties();
        clientBuilder.setProxy(new HttpHost(PROXY_HOST, PROXY_PORT, "http"));
        clientBuilder.setDefaultCredentialsProvider(credentialsProvider);
        clientBuilder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());
        clientBuilder.setConnectionManager(poolingConnectionManager());
        clientBuilder.setSSLHostnameVerifier(new NoopHostnameVerifier());
        
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
//        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
        CloseableHttpClient httpClient = clientBuilder.setSSLSocketFactory(csf).build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        
        return requestFactory;
    }
    
    private PoolingHttpClientConnectionManager poolingConnectionManager() {
        SSLContextBuilder builder = new SSLContextBuilder();
        try {
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
        } catch (NoSuchAlgorithmException | KeyStoreException e) {
        	e.printStackTrace();
        }

        SSLConnectionSocketFactory sslsf = null;
        try {
            sslsf = new SSLConnectionSocketFactory(builder.build());
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
        	e.printStackTrace();
        }

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                .<ConnectionSocketFactory>create().register("https", sslsf)
                .register("http", new PlainConnectionSocketFactory())
                .build();

        PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        poolingConnectionManager.setMaxTotal(50);
        return poolingConnectionManager;
    }
}
