package com.spidertechnosoft.app.xeniamobi_v2.model.domain;

import com.orm.SugarRecord;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.IndicatorStatus;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Product extends SugarRecord implements Serializable {

    private String uid;

    private String code;

    private String name;

    private String categoryUid;

    private String categoryName;

    private Double rate;

    private Double outPutTax;

    private Boolean salesInc;

    private String description;

    private String unit;

    private String image;

    private Integer active= IndicatorStatus.YES;
}
