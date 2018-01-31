package com.github.hackerwin7.libjava.rest.dropwizard.helloworld;

import com.codahale.metrics.health.HealthCheck;

/**
 * Created by IntelliJ IDEA.
 * User: hackerwin7
 * Date: 2017/08/16
 * Time: 11:04 AM
 * Desc:
 */
public class TemplateHealthCheck extends HealthCheck {

    private final String template;

    public TemplateHealthCheck(String template) {
        this.template = template;
    }

    @Override
    protected Result check() throws Exception {
        final String saying = String.format(template, "TEST");
        if(!saying.contains("TEST"))
            return Result.unhealthy("template doesn't include a name");
        return Result.healthy();
    }
}
