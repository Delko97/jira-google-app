package com.zive.it.response;

import com.atlassian.jwt.Jwt;

public class JwtToken {

    private String jwt;

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
