/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.harvard.student.svc;

import org.apache.log4j.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.jbeans.config.Configurable;

/**
 *  This class demonstrates use of the Configurator and @Configurable
 *  to inject properties from an external properties file.   * 
 */
@Stateless
@DependsOn("Configurator")
public class DemoSessionBean {
    
    private static Logger LOGGER = Logger.getLogger(DemoSessionBean.class);
    @Inject @Configurable("jbeans.demo.string.prop")
    String demoProperty;
    
    @Inject @Configurable("jbeans.config.demo.int")
    int demoInt;
    
    @Inject @Configurable("jbeans.config.demo.float")
    float demoFloat;
    
    @Inject @Configurable("jbeans.config.demo.long")
    float demoLong;
    
    @Inject @Configurable("jbeans.config.demo.boolean")
    boolean demoBool;

    @PostConstruct
    private void init() {
        LOGGER.info("demoProperty="+ demoProperty);
        LOGGER.info("demoInt="+ demoInt);
        LOGGER.info("demoFloat="+ demoFloat);
        LOGGER.info("demoLong="+ demoLong);
        LOGGER.info("demoBool="+ demoBool);
    }
        
    
    public String getProperty(){
        LOGGER.info("Retrieving new injected property: "+ demoProperty);

        if (demoProperty==null){
            init();
        }
        return demoProperty;
    }
    
    public boolean injectedPropertyOk(){
        LOGGER.info("Verifying injected jbeans.demo.string.prop is ok, prop value: "+ demoProperty);

        // properties file jbeans.demo.string.prop=This demo property came from the externally configured properties file\!
        Boolean retval=demoProperty!=null && demoProperty.startsWith("This demo property came from the externally");
        LOGGER.info("injectedPropertyOk(), returning:"+ Boolean.toString(retval));

        return retval;
    }
    
    public boolean injectedBooleanPropertyOk(){
        LOGGER.info("Verifying injected boolean is ok, prop value: "+ demoBool);
        
        // properties file jbeans.config.demo.boolean=true
        boolean retval=demoBool;
        LOGGER.info("injectedBooleanPropertyOk(), returning:"+ retval);

        return retval;
    }
    
    public boolean injectedLongPropertyOk(){
        LOGGER.info("Verifying injected long is ok, prop value: "+ demoLong);
        
        // properties file jbeans.config.demo.long=0L
        boolean retval=demoLong==0L;
        LOGGER.info("injectedLongPropertyOk(), returning:"+ retval);

        return retval;
    }
    
    public boolean injectedFloatPropertyOk(){
        LOGGER.info("Verifying injected float is ok, prop value: "+ demoFloat);
        
        // properties file jbeans.config.demo.float=1.007
        boolean retval=demoFloat==1.007;
        LOGGER.info("injectedFloatPropertyOk(), returning:"+ retval);

        return retval;
    }
    
    public boolean injectedIntPropertyOk(){
        LOGGER.info("Verifying injected int is ok, prop value: "+ demoFloat);
        
        // properties file jbeans.config.demo.int=1
        boolean retval=demoInt==1;
        LOGGER.info("injectedFloatPropertyOk(), returning:"+ retval);

        return retval;
    }
    
}
