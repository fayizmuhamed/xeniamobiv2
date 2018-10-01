package com.spidertechnosoft.app.xeniamobi_v2.model.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.IndicatorStatus;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.PaymentType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by DELL on 11/24/2017.
 */
@Getter
@Setter
@ToString
public class Sale extends SugarRecord implements Serializable {

    private String saleUid;

    private String salesNo;

    private String salesDate;

    private String customerName;

    private String contactNo;

    private Double totalAmount=0.0;

    private Double taxAmount=0.0;

    private Double grossAmount=0.0;

    private Double discount=0.0;

    private Double netAmount=0.0;

    private Double roundOff=0.0;

    private Integer paymentType=PaymentType.CASH;

    private Double cashReceived=0.0;

    private Double cardReceived=0.0;

    private Double returnedAmount=0.0;

    private String userId;

    private String userName;

    private Integer deleted= IndicatorStatus.NO;

    List<SaleLineItem> items=new ArrayList<SaleLineItem>();

    public List<SaleLineItem> getSaleLineItems(){

        return SaleLineItem.find(SaleLineItem.class, "sale_Uid = ?", new String[]{this.saleUid});
    }
}
