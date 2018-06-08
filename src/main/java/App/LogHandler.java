package App;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.net.URL;


public class LogHandler {
    private static final Logger _logger;

    static{
        URL url = LogHandler.class.getResource("/log4j.properties");
        PropertyConfigurator.configure(url.getPath());
        _logger  = Logger.getLogger("Assignment03");
        System.out.println(_logger);
    }
    public static void info(String msg){
        _logger.info(msg);
    }

    public static void warning(String msg){
        _logger.warn(msg);
    }

    public static void error(String msg){
        _logger.error(msg);
    }

    public static void error(String msg, Throwable t){
        _logger.error(msg, t);
    }
}
