/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.harvard.student.svc;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test 
 */
public class DemoServletTest {
    
    public DemoServletTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of processRequest method, of class DemoServlet.
     */
    @Test
    public void testProcessRequest() throws Exception {
        System.out.println("processRequest not currently implemented");
    }

    /**
     * Test of doPost method, of class DemoServlet.
     */
    @Test
    public void testDoPost() throws Exception {
        System.out.println("doPost is not currently implemented");
    }

    /**
     * Test of getServletInfo method, of class DemoServlet.
     */
    @Test
    public void testGetServletInfo() {
        System.out.println("getServletInfo");
        DemoServlet instance = new DemoServlet();
        String expResult = "The DemoServlet demonstrates using CDI and the jbeans-config component.";
        String result = instance.getServletInfo();
        assertEquals(expResult, result);
    }
    
}
