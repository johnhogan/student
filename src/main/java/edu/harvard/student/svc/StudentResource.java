package edu.harvard.student.svc;

import edu.harvard.student.domain.Student;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

@Path("/students")
public class StudentResource {

    private static Logger LOGGER = Logger.getLogger(StudentResource.class);
    private static final Map<Integer, Student> studentDB = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger();
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPatient(Student student) {
        Response.ResponseBuilder builder = null;
        try {
            student.setId(idCounter.incrementAndGet());
            studentDB.put(student.getId(), student);
            LOGGER.info("Created patient " + student);
            builder=Response.ok(student);
        } catch (Exception e) {
            builder = Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage());
        }

        return builder.build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Student getStudent(@PathParam("id") int id) {
        Student student = studentDB.get(id);
        LOGGER.info("getStudent, student =" + student+", hashcode="+this.hashCode());
        if (student == null) {
            throw new StudentNotFoundException("Could not find student id: " + id);
        }
        return student;
    }

    @GET
    @Path("httpheaders")
    public void test(@Context HttpHeaders headers) {
        LOGGER.info("ALL headers -- " + headers.getRequestHeaders().toString());
        LOGGER.info("'Accept' header -- " + headers.getHeaderString("Accept"));
        LOGGER.info("'TestCookie' value -- " + headers.getCookies().get("TestCookie").getValue());
        List<String> referer = headers.getRequestHeader("referer");
        LOGGER.info("'referer' value -- " + referer);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
    	LOGGER.info("getAll, count = "+studentDB.size());
        Collection<Student> allPatients = studentDB.values();
        return Response.ok(allPatients).build();
    }
    
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updatePatient(@PathParam("id") int id, Student update) {
        Student current = studentDB.get(id);
        if (current == null) {
            throw new StudentNotFoundException("Could not find patient " + id);
        }

        current.setFirstName(update.getFirstName());
        current.setLastName(update.getLastName());
        current.setStreet(update.getStreet());
        current.setState(update.getState());
        current.setZip(update.getZip());
        current.setCountry(update.getCountry());
    }

    @DELETE
    @Path("{id}")
    public Response removeContact(final @PathParam("id") Long id) {
    	LOGGER.info("'deleting patient id: "+id);
        studentDB.remove(id);
        return Response.ok().build();
    }

    @DELETE
    public Response removeAllPatients() {
    	LOGGER.info("removeAllPatients");
        studentDB.clear();
        
        LOGGER.info("removeAllPatients, count = "+studentDB.size());

        return Response.ok().build();
    }

    
}
