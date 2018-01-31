package com.github.hackerwin7.libjava.rest.dropwizard.helloworld;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

/**
 * Created by IntelliJ IDEA.
 * User: hackerwin7
 * Date: 2017/08/15
 * Time: 5:24 PM
 * Desc:
 */
public class Saying {

    private long id;

    @Length(max = 3)
    private String context;

    public Saying() {

    }

    public Saying(long id, String context) {
        this.id = id;
        this.context = context;
    }

    @JsonProperty
    public long getId() {
        return id;
    }

    @JsonProperty
    public String getContext() {
        return context;
    }
}
