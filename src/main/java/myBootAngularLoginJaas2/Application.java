package myBootAngularLoginJaas2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import myBootAngularLoginJaas2.config.DatabaseConfig;
import myBootAngularLoginJaas2.config.SecurityConfig;




/**
 * Hello world!
 *
 */
@Configuration

@EnableAutoConfiguration
@ComponentScan(basePackages = {"myBootAngularLoginJaas2"})
public class Application extends SpringBootServletInitializer{
	protected static Logger logger = LoggerFactory.getLogger(Application.class);
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {	
		logger.info("***STARTING_APPLICATION_CONFIGURE_FUN***");
		return application
				.sources(new Class[] { Application.class, SecurityConfig.class,DatabaseConfig.class });
	}
}