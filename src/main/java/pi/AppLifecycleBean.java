package pi;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.jboss.logging.Logger;

@ApplicationScoped
public class AppLifecycleBean {

    private static final Logger LOGGER = Logger.getLogger("ListenerBean");

    void onStart(@Observes StartupEvent ev) {               
        LOGGER.info("The application is starting piru technologies...");
        // App.initStorage();
    }

    void onStop(@Observes ShutdownEvent ev) {               
        LOGGER.info("The application is stopping piru technolgies...");
        // App.shutDownStorage();
    }

}