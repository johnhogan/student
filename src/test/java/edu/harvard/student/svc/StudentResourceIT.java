package edu.harvard.student.svc;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import edu.harvard.student.domain.Student;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This example demonstrates returning a customized error message from a restful
 * service call.
 *
 */
@RunWith(Arquillian.class)
@RunAsClient
public class StudentResourceIT {

    private static Client client;

    @BeforeClass
    public static void initClient() {
        client = ClientBuilder.newClient();
    }

    @AfterClass
    public static void closeClient() {
        client.close();
    }

    @Test
    public void testStudentResource() throws Exception {
        try {
            Student student = client.target("http://localhost:8080/student/rest/students/1").request().get(Student.class);
        } catch (NotFoundException e) {
            String error = e.getResponse().readEntity(String.class);
        }
    }

}
