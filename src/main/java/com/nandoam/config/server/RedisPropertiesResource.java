package com.nandoam.config.server;

import com.nandoam.config.server.model.Properties;
import com.nandoam.config.server.redis.RedisPropertiesService;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.List;

@Path("/properties")
@Slf4j
public class RedisPropertiesResource {

    @Inject
    RedisPropertiesService redisPropertiesService;

    @GET
    public List<Properties> getAllProperties(){
        return redisPropertiesService.getAllProperties();
    }

    @GET
    @Path("/{key}")
    public Properties getProperties(final String key){
        return redisPropertiesService.getProperties(key);
    }

    @POST
    public void addProperties(final Properties properties){
        redisPropertiesService.addProperties(properties);
    }

    @GET
    @Path("/{key}/count")
    public Integer countPropertyInProperties(final String key){
        return redisPropertiesService.countPropertyInProperties(key);
    }
}
