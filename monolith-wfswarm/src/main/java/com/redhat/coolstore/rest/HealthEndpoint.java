package com.redhat.coolstore.rest;

import org.wildfly.swarm.health.Health;

public class HealthEndpoint {

    @Health
    public String health() {
        return "ok";
    }
}
