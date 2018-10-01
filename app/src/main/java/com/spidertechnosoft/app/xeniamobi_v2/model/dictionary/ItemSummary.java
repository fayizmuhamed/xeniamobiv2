package com.spidertechnosoft.app.xeniamobi_v2.model.dictionary;

import com.orm.SugarRecord;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemSummary implements Serializable {

    private String itemUid;

    private String itemName;

    private Double itemPrice;

    private Integer itemQty;

    private Double itemAmount;

}
