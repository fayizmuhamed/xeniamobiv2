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
public class Category extends SugarRecord implements Serializable {

    private String uid;

    private String name;

    private Integer active= IndicatorStatus.YES;
}
