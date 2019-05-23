package jltechtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Application providing a RESTful interface accessing discounted products
 * 
 * @author jason
 *
 */
@SpringBootApplication
public class Application {	
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
