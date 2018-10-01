package com.spidertechnosoft.app.xeniamobi_v2.model.dictionary;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CategorySummary implements Serializable {

    private String categoryUid;

    private String categoryName;

    private Integer categoryQty;

    private Double categoryAmount;

}
