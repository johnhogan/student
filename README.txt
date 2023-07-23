
Here's what you need to do to build, deploy and run student

1)  CONFIGURE JBoss Wildfly or EAP for you application.
  a) Add JNDI prop for your app's externalized properties file standalone.xml's naming subsystem: 
     <subsystem xmlns="urn:jboss:domain:naming:2.0">
         ...
         <simple name="java:global/jbeans/student/ENVIRONMENT_PROPERTIES_PATH" value="C:/extconf/jbeans/app/student/config"/>

  b) Configure your app's logging-profile.
    There's a sample logging-profile in the student-configs-for-eap-standalone.txt file.
    Copy/paste this segment into the logging-profiles section of your standalone.xml file.

BUILD THE APPLICATION (and run JUnit tests)
    mvn clean install -DskipITs


RUN THE INTEGRATION TESTS USING ARQUILLIAN REMOTE PROFILE (Wildfly or EAP server must be running locally)
    mvn clean verify -Parq-remote

ENCRYPTED PROPERTIES
If you application's external properties file includes JASYPT encrypted properties, you need to configure your
app's JASYPT key.  This is to define the environment property your app will expect to exist during application
startup.  Your application will read this key, and use the value during JASYPT encrypt/decrypt operations.
The name of the JASYPT key in your app's src\main\resources\jbeans-app.properties file.  This file gets included in
your project war file.
Jasypt encryption key name must be defined even if you propeties file doesn't contain any encrypted properties.
    jbeans.app.props.encKey.name=dKey 

3) The expected properties file name is also defined in jbeans-app.properties. This is the name of the properties file you want
the configurator component to load your app's properties.  It can be called any name desired.  For the demo app, the expected props file name is:
    student.properties

NOTE
When the Configurator is initializing, if there is no JNDI/naming value defined.  It will attempt to read a properties
file of the same name (configured in jbeans-app.properties, i.e.:   jee-config-demo) packaged within your war file.
This demo does that.

For detailed information on the Configurator, see:
    https://www.amazon.com/Real-World-Java-Patterns-Rethinking-Practices-ebook/dp/B009ZQ9I62/ref=sr_1_1?crid=1266XYTXZ57WZ&keywords=adam+bien+design+patterns&qid=1689958059&sprefix=adam+bien+design+pattern%2Caps%2C114&sr=8-1

	
=================================================
Using jbeans-config restful services ...

Deploy your student project in JBoss7.2.
    mvn clean package wildfly:deploy

Services are at:   http://localhost:8080/student/rest/students.  See javadoc for usage.


You can view an individual property settings in a browser, or by using curl with:
    curl http://localhost:8080/student/rest/configuration/jbeans.demo.string.prop

You can view all properties in a browser, or by using curl with:
    curl localhost:8080/student/rest/configuration

ADD OR UPDATE a property.  This example adds new.config.demo.param property.
    curl -X PUT -H "Content-Type: text/plain" -d 'this is the value of my new prop' 'http://localhost:8080/student/rest/configuration/new.config.demo.param'
    curl -X PUT -H "Content-Type: text/plain" -d 'changed value of updated prop' 'http://localhost:8080/student/rest/configuration/new.config.demo.param'

GET an individual property value
    http://localhost:8080/student/rest/configuration/new.config.demo.param


DELETE a property
    curl -i -X DELETE http://localhost:8080/student/rest/configuration/new.config.demo.param

All properties in a browser:
    http://localhost:8080/student/rest/configuration/


Undeploy your application from EAP 7
    mvn clean package wildfly:undeploy

==============
There are some sample CLI scripts in included in the "scripts" folder

For info on using Elytron CLIs, see:
https://access.redhat.com/documentation/en-us/red_hat_jboss_enterprise_application_platform/7.1/html/how_to_configure_server_security/securely_storing_credentials


