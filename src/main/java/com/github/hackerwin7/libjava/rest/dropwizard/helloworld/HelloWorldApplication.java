package com.github.hackerwin7.libjava.rest.dropwizard.helloworld;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Created by IntelliJ IDEA.
 * User: hackerwin7
 * Date: 2017/08/15
 * Time: 5:18 PM
 * Desc:
 */
public class HelloWorldApplication extends Application<HelloWorldConfiguration> {

    public static void main(String[] args) throws Exception {
        String yml = HelloWorldApplication.class.getClassLoader().getResource("hw.yaml").getPath();
        new HelloWorldApplication().run("server", yml);
        System.out.println("started dropwizard...");
        System.out.println("path = " + yml);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<HelloWorldConfiguration> bootstrap) {

    }

    @Override
    public void run(HelloWorldConfiguration helloWorldConfiguration, Environment environment) throws Exception {
        final HelloWorldResource resource = new HelloWorldResource(
                helloWorldConfiguration.getTemplate(),
                helloWorldConfiguration.getDefaultName()
        );
        environment.jersey().register(resource);

        final TemplateHealthCheck healthCheck = new TemplateHealthCheck(
                helloWorldConfiguration.getTemplate()
        );
        environment.healthChecks().register("template", healthCheck);
    }
}
