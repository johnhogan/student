
/**
 * This class demonstrates use of the Java EE / JAX-RS ClientRequestFilter class.
 */
package edu.harvard.student.svc;

import java.io.IOException;
import edu.harvard.student.domain.Student;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

//This filter adds a username to SavedBy field
public class SavedByRequestFilter implements ClientRequestFilter {

    public static final String USERNAME = "bchClinicianUser";

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        String method = requestContext.getMethod();
        if ("POST".equals(method) && requestContext.hasEntity()) {
            Student patient = (Student) requestContext.getEntity();
            patient.setSavedBy(USERNAME);
            requestContext.setEntity(patient);
        }

    }

}
