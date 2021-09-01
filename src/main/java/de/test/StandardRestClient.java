package de.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author iternovykh
 *         Date: 22.04.2016.
 */
public class StandardRestClient implements RestClient
{
    private static final Logger LOG = LoggerFactory.getLogger(StandardRestClient.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public Post getByID(int id)
    {
        CloseableHttpResponse response = null;
        InputStream is = null;
        CloseableHttpClient client = null;

        try
        {
            client = HttpClients.createDefault();

            HttpGet get = new HttpGet(String.format("http://jsonplaceholder.typicode.com/posts/%s", id));
            response = client.execute(get);
            HttpEntity getEntity = response.getEntity();

            is = getEntity.getContent();

            return MAPPER.readValue(is, Post.class);
        } catch (Exception ex)
        {
            LOG.error(String.format("Error fetching post by id = %s", id), ex);
        } finally
        {
            if (client != null)
            {
                try
                {
                    client.close();
                } catch (IOException e)
                {
                    LOG.trace("Error closing client", e);
                }
            }

            if (response != null)
            {
                try
                {
                    response.close();
                } catch (IOException e)
                {
                    LOG.trace("Error closing respose", e);
                }
            }

            if (is != null)
            {
                try
                {
                    is.close();
                } catch (IOException e)
                {
                    LOG.trace("Error closing input stream", e);
                }
            }
        }

        return null;
    }

	@Override
	public String getHealthCheck() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String addPost() {
		// TODO Auto-generated method stub
		return null;
	}
}
