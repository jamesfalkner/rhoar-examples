package com.redhat.coolstore.rest;

import org.wildfly.swarm.health.Health;
import org.wildfly.swarm.health.HealthStatus;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/infra")
public class InfraEndpoint {

    @GET
    @Path("/health")
    @Health
    public HealthStatus check() {

        return HealthStatus.named("main").up();
    }

}
