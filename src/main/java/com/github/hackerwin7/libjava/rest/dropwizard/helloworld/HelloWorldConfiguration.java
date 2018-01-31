package com.github.hackerwin7.libjava.rest.dropwizard.helloworld;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by IntelliJ IDEA.
 * User: hackerwin7
 * Date: 2017/08/15
 * Time: 5:10 PM
 * Desc:
 */
public class HelloWorldConfiguration extends Configuration {

    @NotEmpty
    private String template;

    @NotEmpty
    private String defaultName = "Stranger";

    private String co;

    @JsonProperty("co.kk.y")
    public String getCo() {
        return co;
    }

    @JsonProperty("co.kk.y")
    public void setCo(String co) {
        this.co = co;
    }

    @JsonProperty
    public String getTemplate() {
        return template;
    }

    @JsonProperty
    public void setTemplate(String template) {
        this.template = template;
    }

    @JsonProperty
    public String getDefaultName() {
        return defaultName;
    }

    @JsonProperty
    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }
}
