package com.spidertechnosoft.app.xeniamobi_v2.model.domain;

import com.orm.SugarRecord;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Setting extends SugarRecord implements Serializable {

    private String key;

    private String value;

    private Long updatedAt;

    private String updatedBy;

    public Setting() {
    }

    public Setting(String key, String value) {
        this.key = key;
        this.value = value;
    }

}
