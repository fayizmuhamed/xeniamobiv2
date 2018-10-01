package com.spidertechnosoft.app.xeniamobi_v2.model.resource;

import com.spidertechnosoft.app.xeniamobi_v2.model.domain.Product;

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
public class CartItem implements Serializable {

    private String itemUid;

    private String itemCode;

    private String itemName;

    private String categoryUid;

    private String categoryName;

    private Integer itemQty;

    private Double itemUnitPrice;

    private Boolean salesInc;

    private Double outPutTax;

    private Double itemAmount;

    private String itemNarration;

    private Product product;

    public Double getItemUnitPrice() {
        return itemUnitPrice==null?0:itemUnitPrice;
    }
}
