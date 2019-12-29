package com.kgbg.util;

import com.alibaba.fastjson.JSON;

import com.kgbg.cons.SecuretiyCons;
import com.kgbg.dto.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JwtToken class
 *
 * @author tangbin
 * @date 2018年7月7日12:28:13
 */
public class JwtUtils {

    /**
     * token的有效时间
     */
    private static final int TOKEN_EXPIRATION = 1000*60*20;

    /**
     * token刷新的有效时间
     */
    private static final int REFRESH_TOKEN_EXPIRATION = 1000*60*60;




    private static class KeyIoRead {
        private static String key = "";

        private static String key(InputStream inputStream) throws IOException {
            byte[] buf = new byte[2048];
            int length = 0;
            String rs = "";
            while (true) {
                if (!((length = inputStream.read(buf)) != -1)) break;
                rs = new String(buf, 0, length);
            }
            inputStream.close();
            return rs;
        }


    }

    /**
     * 读取资源文件
     *
     * @param fileName 文件的名称
     * @return
     */
    public static String readResourceKey(String fileName) {
        String key = "";
        try {
            if (KeyIoRead.key == "") {
                InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
                assert inputStream != null;
                key = KeyIoRead.key(inputStream);
            } else {
                key = KeyIoRead.key;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return key;
    }

    /**
     * 构建token
     *
     * @param user      用户对象
     * @param ttlMillis 过期的时间-毫秒
     * @return
     * @throws Exception
     */
    public static String buildJwtRS256(Map user, long ttlMillis) throws Exception {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS256;
        // 读取私钥
        String key = readResourceKey("rsa_private_key_pkcs8.pem");

        // 生成签名密钥
        byte[] keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        // 生成JWT的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        //创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）


        // 生成jwt文件
        JwtBuilder builder = Jwts.builder()
                // 这里其实就是new一个JwtBuilder，设置jwt的body
                // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(user)
                .setHeaderParam("typ", "JWT")
                .setIssuedAt(now)
                .setSubject(JSON.toJSONString(user))
                .signWith(signatureAlgorithm, privateKey);

        // 如果配置了过期时间
        if (ttlMillis >= 0) {
            // 当前时间加上过期的秒数
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            // 设置过期时间
            builder.setExpiration(exp);
        }
        String token = builder.compact();
        return token;
    }



    /**
     * 构建token
     *
     * @param user      用户对象
     * @return
     * @throws Exception
     */
    public static TokenDto buildJwtRS256(Map user) throws Exception {
        String token = buildJwtRS256(user, TOKEN_EXPIRATION);
        Map map = new HashMap();
        map.put(SecuretiyCons.REFRESH_TOKEN_KEY,token);
        String refresh_token = buildJwtRS256(map,REFRESH_TOKEN_EXPIRATION);
        TokenDto tokenDto = new TokenDto();
        tokenDto.setAccess_token(token);
        tokenDto.setBearer("bearer");
        tokenDto.setExpires_in(TOKEN_EXPIRATION);
        tokenDto.setRefresh_token(refresh_token);
        return tokenDto;
    }

    /**
     * 解密Jwt内容
     *
     * @param jwt
     * @return
     */
    public static Claims parseJwtRS256(String jwt) {
        Claims claims = null;
        try {
            // 读取公钥
            String key = readResourceKey("rsa_public_key_2048.pub");
            // 生成签名公钥
            byte[] keyBytes = (new BASE64Decoder()).decodeBuffer(key);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            claims = Jwts.parser()
                    .setSigningKey(publicKey)
                    .parseClaimsJws(jwt).getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return claims ;
    }


    /**
     * 解密Jwt内容
     *
     * @param jwt
     * @return
     */
    public static boolean parseJwt(String jwt) {
        boolean rs = false;

        // 读取公钥
        String key = readResourceKey("rsa_public_key_2048.pub");
        // 生成签名公钥
        byte[] keyBytes = new byte[0];
        try {
            keyBytes = (new BASE64Decoder()).decodeBuffer(key);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            rs = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rs;
    }

    public static TokenDto refreshToken(String jwt) throws Exception {
        Claims claims = parseJwtRS256(jwt);
        return buildJwtRS256(claims);
    }
}