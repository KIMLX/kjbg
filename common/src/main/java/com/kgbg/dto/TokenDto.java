package com.kgbg.dto;

import lombok.Data;

@Data
public class TokenDto {
    private String access_token;
    private String bearer;
    private String refresh_token;
    private int expires_in;

    public TokenDto() {
    }
}
