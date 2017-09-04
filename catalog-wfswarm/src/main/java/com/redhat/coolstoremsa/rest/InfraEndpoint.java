package com.redhat.coolstoremsa.rest;

import org.wildfly.swarm.health.Health;
import org.wildfly.swarm.health.HealthStatus;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Created by jfalkner on 9/4/17.
 */
@Path("/infra")
public class InfraEndpoint {

    @Health
    @GET
    @Path("/health")
    public HealthStatus status() {
        return HealthStatus.named("health").up();
    }
}
