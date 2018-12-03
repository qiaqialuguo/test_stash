package rock;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.util.Properties;

public class LogTest {
    public static void main(String[] args) throws Exception{
        ForFig forFig = new ForFig();
        Properties properties = forFig.readProperties();
        final String testfig = properties.getProperty("testfig");
        System.out.println(testfig);


    }
        }
