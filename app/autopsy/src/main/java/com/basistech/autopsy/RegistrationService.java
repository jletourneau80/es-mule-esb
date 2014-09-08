package com.basistech.autopsy;

/**
 * Created by IntelliJ IDEA.
 * User: jletourneau
 * Date: Jan 26, 2010
 * Time: 9:06:52 AM
 * To change this template use File | Settings | File Templates.
 */


import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;

import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;

import java.net.URI;
import java.util.Iterator;

import java.util.Date;

@Path("/rss")
public class RegistrationService {

    @GET
    @Produces("application/atom+xml")
    @Path("/feed")
    public String produceRSS() throws java.net.MalformedURLException {
        AutopsyWarehouse warehouse = AutopsyWarehouse.getInstance();
        
        Feed feed = warehouse.getAbdera().newFeed();

        feed.setId("tag:com.basistech.autopsy,2014:/autopsy");
        feed.setTitle("Registered Autopsy Instances");
        feed.setSubtitle("Autopsy Analysis");
        feed.setUpdated(new Date());
        feed.addAuthor("Mule ESB");

         for (Iterator it = warehouse.getCache().keySet().iterator(); it.hasNext(); ) {
            URI autopsy = (URI)it.next();
            Entry entry = feed.addEntry();
            entry.setId("tag:com.basistech.autopsy,2014:/" + autopsy.getHost());
            entry.setTitle(autopsy.toURL().toString());
            entry.setUpdated(new Date(warehouse.getCache().get(autopsy)));
            entry.setPublished(new Date());
            entry.addLink(autopsy.toURL().toString());

        }

        return feed.toString();
    }

    @POST
    @Produces("text/plain")
    @Path("/registration/{name}")
    public String sayHelloWithUri(@PathParam("name") String name) {
        
        AutopsyWarehouse warehouse = AutopsyWarehouse.getInstance();
        try{
        URI surfaceHost = new URI("http://" + name);
        warehouse.addAutopsyInstance(surfaceHost);
        
        }catch(java.net.URISyntaxException e){
          return "bad uri syntax";  
        }
        return "Hello " + name;
    }


}