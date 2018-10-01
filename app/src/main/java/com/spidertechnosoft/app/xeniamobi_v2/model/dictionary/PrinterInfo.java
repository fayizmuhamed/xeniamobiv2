package com.spidertechnosoft.app.xeniamobi_v2.model.dictionary;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PrinterInfo implements Serializable {

    private String deviceName;

    private int vendorId;

    private int productId;

    private int deviceClass;

    private int deviceSubClass;

    private int protocol;

}
