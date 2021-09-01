package de.test;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.util.JSONPObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;

/**
 * @author iternovykh
 *         Date: 22.04.2016.
 */
public class RestTemplateClient extends RestClientBase implements RestClient
{
	private HttpHeaders createHttpHeaders(String user, String password)
	{
	    String notEncoded = user + ":" + password;
	    String encodedAuth = Base64.getEncoder().encodeToString(notEncoded.getBytes()); 
	    HttpHeaders headers = new HttpHeaders();
//	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.add("Proxy-Authorization", "Basic " + encodedAuth);
	    headers.add("Authorization", "Basic " + encodedAuth);
	    return headers;
	}
	
	private String urlWithPort(String url) {
		URI uri = URI.create(url);
		if (uri.getPort() == -1) {
			try {
				url = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), 443,
                    uri.getPath(), uri.getQuery(), uri.getFragment()).toString();
			} catch (URISyntaxException e) { }
		}
		return url;
	}
	
    @Override
    public Post getByID(int id)
    {
//        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://jsonplaceholder.typicode.com");
//        final URI uri = builder.path("posts/{id}").build().expand(id).encode().toUri();
    	String theUrl = "http://jsonplaceholder.typicode.com/post/" + id;
//    	System.out.print(theUrl);
        HttpHeaders headers = createHttpHeaders("admin","admin");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        
//        JSONObject obj = new JSONObject();
        
        ResponseEntity<Post> response = getTemplate().exchange(theUrl, HttpMethod.GET, entity, Post.class);

        return response.getBody();
    }
    
    @Override
    public String addPost()
    {
//    	String theUrl = urlWithPort("https://jsonplaceholder.typicode.com/posts");
    	String theUrl = "https://jsonplaceholder.typicode.com/posts";
    	String input = "{\"title\":\"foo\",\"body\":\"bar\",\"userId\":2}";
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	HttpEntity<String> entity = new HttpEntity<String>(input, headers);
    	ResponseEntity<String> response = getTemplate().exchange(theUrl, HttpMethod.POST, entity, String.class);
    	return response.getBody();
    }
    
    @Override
    public String getHealthCheck() {
    	String theUrl = "https://smartcentraltest.crisil.com/api/smartservice/getHealthCheck";
    	RequestEntity<Void> request = RequestEntity.get(URI.create(theUrl)).build();
//    	String theUrl = "http://jsonplaceholder.typicode.com/posts/1";
//    	HttpHeaders headers = createHttpHeaders("adminx","adminy");
//        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        ResponseEntity<String> response = getTemplate().exchange(request, String.class);
//        System.out.print(response);
        return response.getBody();
    }
}
