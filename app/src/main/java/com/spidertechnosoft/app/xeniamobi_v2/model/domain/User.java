package com.spidertechnosoft.app.xeniamobi_v2.model.domain;

import com.orm.SugarRecord;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.UserType;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User extends SugarRecord implements Serializable {

    private String uid;

    private String name;

    private String email;

    private String contact;

    private String username;

    private String password;

    private UserType type;

    private Boolean active=true;
}
