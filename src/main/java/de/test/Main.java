package de.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author iternovykh
 *         Date: 22.04.2016.
 */
public class Main
{
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args)
    {
//    	System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
//    	System.setProperty("jdk.http.auth.proxying.disabledSchemes", "");
//    	System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,TLSv1");
        LOG.info("Starting demo");
//        
//        try {
//			ProxiedHttpsConnection n = new ProxiedHttpsConnection(
//			        new URL("https://smartcentraltest.crisil.com:443/api/smartservice/getHealthCheck"), 
//			        "localhost", 3128, "admin", "admin");
//			try (InputStream in = n.getInputStream()) {
//		        byte[] buff = new byte[1024];
//		        int length;
//		        while ((length = in.read(buff)) >= 0) {
//		            System.out.write(buff, 0, length);
//		        }
//		    }
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

        final RestClient client = create();
        
        final String res = client.addPost();
        
        LOG.info(res);

//        for (int i = 1; i < 10; ++i)
//        {
//            final Post p = client.getByID(i);
//            if (p != null)
//            {
//                LOG.info(String.format("Fetched post = [%s]", p));
//            } else
//            {
//                LOG.warn(String.format("No post available for id = %s", i));
//            }
//        }
    }

    private static RestClient create()
    {
        return new RestTemplateClient();
    }
}
