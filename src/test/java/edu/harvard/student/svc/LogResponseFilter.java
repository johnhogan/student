package edu.harvard.student.svc;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;

/**
 * This class demonstrates use of the Java EE / JAX-RS ClientResponseFilter class.
 */
public class LogResponseFilter implements ClientResponseFilter {

    private static final Logger log = Logger.getLogger(LogResponseFilter.class.getName());

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        log.log(Level.INFO, "Date: {0}- Status: {1}", new Object[]{responseContext.getDate(), responseContext.getStatus()});
    }
}
