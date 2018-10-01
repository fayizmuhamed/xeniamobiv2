package com.spidertechnosoft.app.xeniamobi_v2.model.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by DELL on 11/24/2017.
 */
@Getter
@Setter
@ToString
public class SaleLineItem extends SugarRecord implements Serializable {

    private String saleUid;
    private String itemUid;
    private String itemCode;
    private String itemName;
    private String categoryUid;
    private String categoryName;
    private Integer qty;

    private Double itemRate;

    private Double grossAmount=0.0;

    private Double discount=0.0;

    private Double amount=0.0;

    private Double taxableAmount=0.0;

    private Boolean salesInc;

    private Double taxPer=0.0;

    private Double taxAmount=0.0;

    private Double netAmount=0.0;

    private String narration;






}
