package cn.smallmartial.auth;

import cn.smallmartial.auth.entity.UserInfo;
import cn.smallmartial.auth.utils.JwtUtils;
import cn.smallmartial.auth.utils.RsaUtils;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @Author smallmartial
 * @Date 2019/4/23
 * @Email smallmarital@qq.com
 */
public class JwtTest {

    private static final String pubKeyPath = "E:\\nginx\\rsa\\rsa.pub";

    private static final String priKeyPath = "E:\\nginx\\rsa\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "234");
    }

    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(20L, "jack"), privateKey, 5);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiamFjayIsImV4cCI6MTU1NjAxNDgwNn0.VdmTbQSTTiRPdBMTG3Lb0XFJnLwg4j8AmrMlQEWqXwQNQjrQr9SQ7B_r6Skfmk-pUpfwN1ZAPN25Og76edIv7-vOZHuyozPkZXoj4KC6fsaSunoGxkKpjrnWZ1bM2Jka8Mv7i4iOubvwsD6pwcbngRtRPXO5_IzJcGskqp0DeP0";

        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}
