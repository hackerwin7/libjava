package com.github.hackerwin7.libjava.rest.dropwizard.helloworld;

import com.codahale.metrics.annotation.Timed;
import org.apache.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by IntelliJ IDEA.
 * User: hackerwin7
 * Date: 2017/08/15
 * Time: 5:29 PM
 * Desc:
 */
@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldResource {

    private static final Logger LOG = Logger.getLogger(HelloWorldResource.class);

    private final String template;
    private final String defaultName;
    private final AtomicLong counter;

    public HelloWorldResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = new AtomicLong();
    }

    @Path("/hi")
    @GET
    @Timed
    public Saying sayHello(@QueryParam("name") Optional<String> name) {
        final String value = String.format(template, name.orElse(defaultName));
        return new Saying(counter.incrementAndGet(), value);
    }

    @Path("/ht")
    @GET
    public Saying ss() {
        LOG.info("hthththth");
        return new Saying(1, "ss ht");
    }
}
