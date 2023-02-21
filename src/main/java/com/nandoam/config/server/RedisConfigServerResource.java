package com.nandoam.config.server;

import com.nandoam.config.server.redis.Property;
import com.nandoam.config.server.redis.RedisService;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.List;

@Path("/properties")
@Slf4j
public class RedisConfigServerResource {

    @Inject
    RedisService redisService;

    @GET
    @Path("/keys")
    public Uni<List<String>> keys() {
        return redisService.keys();
    }


    @GET
    @Path("/{key}")
    public Property getRedisProperty(String key) {
        return redisService.getProperty(key);
    }

    @POST
    public Property create(Property property) {
        redisService.set(property.key, property.value);
        return property;
    }


}
