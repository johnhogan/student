package edu.harvard.student.svc;

public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException(String s) {
        super(s);
    }
}
