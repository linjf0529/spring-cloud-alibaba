package com.linjf.cloud.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author linjf
 * @create 2022/3/28 16:13
 */
@Data
public class Payment implements Serializable {

    private Long id;
    private String serial;
    private String port;
}
