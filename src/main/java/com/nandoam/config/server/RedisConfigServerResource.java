package com.nandoam.config.server;

import com.nandoam.config.server.model.Property;
import com.nandoam.config.server.redis.RedisReactiveService;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.List;

@Path("/properties-config")
@Slf4j
public class RedisConfigServerResource {

    @Inject
    RedisReactiveService redisService;

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


    /*@GET
    @Path("/lists/{key}")
    public Uni<List<Property>> listKeys(String key) {
        return redisService.getPropertiesList(key);
    }*/

    @GET
    @Path("/lists/{user}")
    public Uni<List<String>> getUserProperties(String user) {
        return redisService.keysByUser(user);
    }


    @POST
    public Property create(Property property) {
        redisService.set(property.key, property.value);
        //redisService.addProperty(property.key,property);
        return property;
    }


}
