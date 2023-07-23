package edu.harvard.student.svc;

import edu.harvard.student.svc.DemoSessionBean;
import java.io.File;
import javax.ejb.EJB;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * For info on running this test, see wiki page:
 * http://websvc4:8090/pages/viewpage.action?pageId=32178326
 *
 * @author CH191532
 */
@RunWith(Arquillian.class)
public class DemoSessionBeanIT {

    private static final String WEBAPP_SRC = "src/test/resources/META-INF";

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(ZipImporter.class, "student.war").importFrom(new File("target/student.war"))
                .as(WebArchive.class);
    }

    @EJB
    private DemoSessionBean demoSessionBean;

    public DemoSessionBeanIT() {
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
     * Test of injectedPropertyOk method, of class DemoSessionBean.
     */
    @Test
    public void testInjectedStringPropertyOk() throws Exception {
        boolean expResult = true;
        boolean result = demoSessionBean.injectedPropertyOk();
        assertEquals(expResult, result);
    }

    /**
     * Test of injectedBooleanPropertyOk method, of class DemoSessionBean.
     */
    @Test
    public void testInjectedBooleanProperty() throws Exception {
        boolean expResult = true;
        boolean result = demoSessionBean.injectedBooleanPropertyOk();
        assertEquals(expResult, result);
    }

    /**
     * Test of injectedBooleanPropertyOk method, of class DemoSessionBean.
     */
    @Test
    public void testInjectedIntProperty() throws Exception {
        boolean expResult = true;
        boolean result = demoSessionBean.injectedIntPropertyOk();
        assertEquals(expResult, result);
    }

}
