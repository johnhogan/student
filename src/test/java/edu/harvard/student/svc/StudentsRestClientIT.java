package edu.harvard.student.svc;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import edu.harvard.student.config.ApplicationConfig;
import edu.harvard.student.domain.Student;
import org.jbeans.config.Configurator;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
@RunAsClient
public class StudentsRestClientIT {

    private static final String PATIENT_FIRST_NAME="Benjamin";
    private static final String PATIENT_LAST_NAME="Franklin";
    private static final String PATIENT_CITY="Boston";
    private static final String PATIENT_STATE="MA";

    private final Logger log = Logger.getLogger(StudentsRestClientIT.class.getName());

    @ArquillianResource
    private URL deploymentUrl;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class, "student.war")
                .addPackage(ApplicationConfig.class.getPackage())
                .addPackage(Configurator.class.getPackage())
                .addPackage(Student.class.getPackage())
                .addPackage(StudentResource.class.getPackage())
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsResource(new File("src/main/resources/jbeans-app.properties"), "jbeans-app.properties");
        return war;
    }

    private String getRequestUrl() {
        return new StringBuilder(deploymentUrl.toString())
                .append("rest/students")
                .toString();
    }

    @Test
    public void crudTest() {
        log.info("### CRUD tests ###");
        // 1 - drop all students
        log.info("dropping all students");
        Response response = ClientBuilder.newClient().target(getRequestUrl()).request().delete();
        Assert.assertEquals("All students should be dropped", Response.ok().build().getStatus(), response.getStatus());

        // 2 - Create a new student
        log.info("creating a new student");
        Student student = new Student();
        student.getFirstName();
        student.setFirstName(PATIENT_FIRST_NAME);
        student.setLastName(PATIENT_LAST_NAME);
        Student persistedStudent = ClientBuilder.newClient().target(getRequestUrl()).request().post(Entity.entity(student, MediaType.APPLICATION_JSON), Student.class);
        log.log(Level.INFO, "persistedStudent = {0}", persistedStudent.getId());
        Assert.assertEquals("A student should be persisted with Id=1!", (Integer) 1, (Integer) persistedStudent.getId());

        // 3 - Fetch Student by Id
        log.info("fetching a student by id");
        Student fetchedStudentById
                = ClientBuilder.newClient().target(getRequestUrl()).path("/{id}").resolveTemplate("id", persistedStudent.getId()).request().get(Student.class);
        log.info("fetched student = "+fetchedStudentById);
        Assert.assertEquals("Fetched student with Id=1!", (Integer) 1, (Integer) fetchedStudentById.getId());
        Assert.assertEquals("Fetched student first name equal to ", PATIENT_FIRST_NAME, fetchedStudentById.getFirstName());
        Assert.assertEquals("Fetched student first name equal to ", PATIENT_LAST_NAME, fetchedStudentById.getLastName());

        // 4 - Fetch all Students
        log.info("fetching all students");
        GenericType<List<Student>> studentsListType = new GenericType<List<Student>>() {
        };
        List<Student> allStudents = ClientBuilder.newClient().target(getRequestUrl()).request().get(studentsListType);
        Assert.assertEquals("Should have a single student", 1, allStudents.size());

        // 5 - Delete a Student
        log.info("delete a student by id");
        response = ClientBuilder.newClient().target(getRequestUrl()).path("/{id}").resolveTemplate("id", persistedStudent.getId()).request().delete();
        Assert.assertEquals("Student 1 should be dropped", Response.ok().build().getStatus(), response.getStatus());
    }

    // This test shows some basic operations using ASYNC invocations and java.util.concurrent.Future
    @Test
    public void asyncCrudTest() throws Exception {
        log.info("### CRUD tests  ASYNC ###");

        // 1 - drop all students ASYNC
        log.info("dropping all students ASYNC");
        Response response = ClientBuilder.newClient().target(getRequestUrl()).request().async().delete().get();
        Assert.assertEquals("All students should be dropped", Response.ok().build().getStatus(), response.getStatus());

        // 2 - Create a new Student ASYNC
        log.info("creating a new student ASYNC");
        Student student = new Student();
        student.setFirstName(PATIENT_FIRST_NAME);
        student.setLastName(PATIENT_LAST_NAME);
        student.setCity(PATIENT_CITY);
        student.setState(PATIENT_STATE);

        Future<Student> futureStudent = ClientBuilder.newClient().target(getRequestUrl()).request().async().post(Entity.entity(student, MediaType.APPLICATION_JSON), Student.class);

        Student persistedStudent = futureStudent.get();
        Assert.assertEquals("A student should be persisted with Id=1!", (Integer) 1, (Integer) persistedStudent.getId());

        // 3 - Delete a student ASYNC
        log.info("delete a student by id ASYNC");
        ClientBuilder.newClient().target(getRequestUrl()).path("{id}").resolveTemplate("id", persistedStudent.getId()).request().async().delete().get();

        // 4 - Fetch All Students ASYNC
        log.info("fetching all students ASYNC");
        Future<List<Student>> futureStudents = ClientBuilder.newClient().target(getRequestUrl()).request().async().get(new GenericType<List<Student>>() {
        });
        List<Student> allStudents = futureStudents.get();
        if (allStudents!=null && !allStudents.isEmpty()){
            log.info("Have students = "+allStudents);
        }
        else {
            log.info("From the future, no students found");
        }
        //Assert.assertEquals("Should have no students", 0, allStudents.size());
    }

    // This test shows how to use javax.ws.rs.client.InvocationCallback
    @Test
    public void invocationCallBackTest() throws Exception {
        log.info("### Testing invocation callback ###");

        // 1 - drop all students
        log.info("dropping all students");
        Response response = ClientBuilder.newClient().target(getRequestUrl()).request().delete();
        Assert.assertEquals("All students should be dropped", Response.ok().build().getStatus(), response.getStatus());

        // 2 - Create a InvocationCallback
        log.info("Creating a InvocationCallback");
        InvocationCallback<List<Student>> invocationCallback = new InvocationCallback<List<Student>>() {

            @Override
            public void completed(List<Student> allStudents) {
                // Completed the invocation with no student
                Assert.assertEquals("Should have no students", 0, allStudents.size());
            }

            @Override
            public void failed(Throwable throwable) {
                // It should fail
                Assert.fail(throwable.getMessage());

            }
        };
        // 3 - Invoke the service
        log.info("Invoking a service using the InvocationCallback");
        ClientBuilder.newClient().target(getRequestUrl()).request().async().get(invocationCallback).get();
    }

    // Shows how to use a delayed REST invocation
    @Test
    public void delayedInvocationTest() throws Exception {
        log.info("### Testing Delayed invocaton ###");

        // 1 - Drop all students
        log.info("dropping all students");
        Response response = ClientBuilder.newClient().target(getRequestUrl()).request().delete();
        Assert.assertEquals("All students should be dropped", Response.ok().build().getStatus(), response.getStatus());

        // 2 - Create a new Student Invocation
        log.info("Creating a new student invocation");
        Student student = new Student();
        student.setFirstName(PATIENT_FIRST_NAME);
        student.setLastName(PATIENT_LAST_NAME);
        Invocation saveStudentInvocation = ClientBuilder.newClient().target(getRequestUrl()).request().buildPost(Entity.entity(student, MediaType.APPLICATION_JSON));

        // 3 - Create a new list Students Invocation
        log.info("Creating list all students invocation");
        Invocation listStudentsInvocation = ClientBuilder.newClient().target(getRequestUrl()).request().buildGet();

        // 4 - Synch Save student
        log.info("invoking the new student");
        Student persistedStudent = saveStudentInvocation.invoke(Student.class);
        Assert.assertEquals("A students should be persisted with Id=1!", (Integer) 1, (Integer) persistedStudent.getId());

        // 5 - Async List students
        log.info("invoking list all students ASYNC");
        GenericType<List<Student>> studentsListType = new GenericType<List<Student>>() {
        };
        Future<List<Student>> futureAllStudents = listStudentsInvocation.submit(studentsListType);
        List<Student> allStudents = futureAllStudents.get();
        Assert.assertEquals("Should have a single student", 1, allStudents.size());
    }

    // Shows how to use Request and Response filters
    @Test
    public void requestResponseFiltersTest() {
        log.info("### Testing Request and Response Filters ###");

        // 1 - Drop all students
        log.info("dropping all students");
        Response response = ClientBuilder.newClient().target(getRequestUrl()).request().delete();
        Assert.assertEquals("All students should be dropped", Response.ok().build().getStatus(), response.getStatus());

        // 2 - Create a new Student Invocation
        log.info("Invoking create new student using a ClientRequestFilter");
        Student student = new Student();
        student.setFirstName(PATIENT_FIRST_NAME);
        student.setLastName(PATIENT_LAST_NAME);
        Student persistedStudent
                = ClientBuilder.newClient().register(SavedByRequestFilter.class).target(getRequestUrl()).request()
                        .post(Entity.entity(student, MediaType.APPLICATION_JSON), Student.class);
        Assert.assertEquals("A student should be persisted with savedBy", SavedByRequestFilter.USERNAME, persistedStudent.getSavedBy());

        // 3 - Fetch all Students
        log.info("Invoking list all students using a ClientResponseFilter");
        GenericType<List<Student>> studentsListType = new GenericType<List<Student>>() {
        };
        List<Student> allStudents = ClientBuilder.newClient().register(LogResponseFilter.class).target(getRequestUrl()).request().get(studentsListType);
        Assert.assertEquals("Should have a single student", 1, allStudents.size());
    }
}
