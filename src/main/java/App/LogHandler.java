package App;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class LogHandler {
    private static final Logger _logger;

    static{
        PropertyConfigurator.configure("log4j.properties");
        _logger  = Logger.getLogger("Assignment03");
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
