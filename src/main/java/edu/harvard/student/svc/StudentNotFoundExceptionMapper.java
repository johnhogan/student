package edu.harvard.student.svc;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class StudentNotFoundExceptionMapper implements ExceptionMapper<StudentNotFoundException> {

    /**
     * Add customized error message to NOT_FOUND response.
     * 
     * @param exception
     * @return 
     */
    @Override
    public Response toResponse(StudentNotFoundException exception) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(exception.getMessage())
                .type("text/plain").build();
    }
}
