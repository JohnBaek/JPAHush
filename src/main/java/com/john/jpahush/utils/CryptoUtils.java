package com.john.jpahush.utils;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * 암호화 관련 클래스
 */
@SuppressWarnings("unused")
public class CryptoUtils {

    /**
     * SHA-256으로 인코딩 한다.
     * @param string 인코딩할 문자열
     * @return SHA-256 인코딩 문자열
     */
    public static String sha256(final String string) {


        try {
            StringBuilder hexString = new StringBuilder();

            // SHA-256에 대한 객체를 가져온다.
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // 해당 문자열에 대한 SHA-256 데이터를 가져온다.
            byte[] hash = digest.digest(string.getBytes(StandardCharsets.UTF_8));

            // 모든 데이터에 대해서 처리
            for (byte b : hash) {
                // 16진수 문자열로 변환
                String hex = Integer.toHexString(0xff & b);

                // 16진수 문자열이 1자리인 경우, 앞에 0 추가
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }

            // 16진수로 변환된 문자열 반환
            return hexString.toString();
        }catch (Exception e) {
            MoreExceptionHandler.Log(e);
            return null;
        }
    }
}
