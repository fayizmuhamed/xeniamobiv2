package com.spidertechnosoft.app.xeniamobi_v2.model.dictionary;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DailySummary implements Serializable {

    private Integer noOfSales=0;

    private Double cashTransaction=0.0;

    private Double cardTransaction=0.0;

    private Double creditTransaction=0.0;

    private Double bothTransaction=0.0;

    private Double totalAmount=0.0;

    private Double taxAmount=0.0;

    private Double grossAmount=0.0;

    private Double billDiscount=0.0;

    private Double roundOff=0.0;

    private Double netAmount=0.0;

    private Double cashReceived=0.0;

    private Double cardReceived=0.0;

}
