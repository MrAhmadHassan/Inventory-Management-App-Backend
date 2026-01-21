package com.ecommerce.inventory.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor

public class TokenResponse{
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private long expiresIn;


}



//        {
//    public static TokenResponse bearer(
//            String accessToken,
//            String refreshToken,
//            long expiresIn) {
//
//        return new TokenResponse(
//                accessToken,
//                refreshToken,
//                "Bearer",
//                expiresIn
//        );
//    }
//}
