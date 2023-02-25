package com.nandoam.config.server.model;

import lombok.Data;
import java.util.List;

@Data
public class Properties {

    private String id;
    private List<Property> propertyList;


}
