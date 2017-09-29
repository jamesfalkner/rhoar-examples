package com.redhat.coolstore.rest;

import org.wildfly.swarm.health.Health;
import org.wildfly.swarm.health.HealthStatus;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Created by jfalkner on 9/27/17.
 */
@Path("/infra")
public class InfraEndpoint {


    @GET
    @Path("/health")
    @Health
    public HealthStatus health() {
        return HealthStatus.named("main").up();
    }
}
