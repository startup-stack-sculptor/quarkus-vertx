
import io.quarkus.vertx.ConsumeEvent;
import jakarta.enterprise.context.ApplicationScoped;

/*
 * 
 * 
 */

/**
 *
 * @author pm
 */
@ApplicationScoped
public class GreetingService {
    @ConsumeEvent("greetings")
    public String hello(String name) {
        return "Hello " + name;
    }
}
