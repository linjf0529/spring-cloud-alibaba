package com.linjf.cloud.entity;

import lombok.Data;

import javax.xml.ws.Service;
import java.io.Serializable;

@Data
public class User implements Serializable {
    private Long id;
    private String username;
    private String address;
    private String servicePort;
}
