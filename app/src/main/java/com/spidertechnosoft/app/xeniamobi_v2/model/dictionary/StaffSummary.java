package com.spidertechnosoft.app.xeniamobi_v2.model.dictionary;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StaffSummary implements Serializable {

    private String userUid;

    private String userName;

    private Double amount;

}
