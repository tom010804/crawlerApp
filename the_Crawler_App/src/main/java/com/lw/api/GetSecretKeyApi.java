package com.lw.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName GetSecretKeyApi
 * @Description TODO
 * @Author Li Wang
 * @Date 2021/6/29/029 15:16
 **/
@RestController
public class GetSecretKeyApi {

    public static final String KEY_ALGORITHM = "RSA";
    //public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * @Description //TODO 获取公钥私钥
     **/
    @PostMapping
    private Map<String,String> SecretKey(){
        Map<String, Object> keyMap;
        HashMap<String, String> map = new HashMap<>();
        try {
            keyMap = initKey();
            String publicKey = getPublicKey(keyMap);
            map.put(PUBLIC_KEY,publicKey);
            System.out.println(publicKey);
            String privateKey = getPrivateKey(keyMap);
            map.put(PRIVATE_KEY,privateKey);
            System.out.println(privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public class Keys {

    }

    //获得公钥
    public  String getPublicKey(Map<String, Object> keyMap) throws Exception {
        //获得map中的公钥对象 转为key对象
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        //byte[] publicKey = key.getEncoded();
        //编码返回字符串
        return encryptBASE64(key.getEncoded());
    }

    //获得私钥
    public  String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        //获得map中的私钥对象 转为key对象
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        //byte[] privateKey = key.getEncoded();
        //编码返回字符串
        return encryptBASE64(key.getEncoded());
    }

    //解码返回byte
    public  byte[] decryptBASE64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    //编码返回字符串
    public  String encryptBASE64(byte[] key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key);
    }

    //map对象中存放公私钥
    public  Map<String, Object> initKey() throws Exception {
        //获得对象 KeyPairGenerator 参数 RSA 1024个字节
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(512);
        //通过对象 KeyPairGenerator 获取对象KeyPair
        KeyPair keyPair = keyPairGen.generateKeyPair();

        //通过对象 KeyPair 获取RSA公私钥对象RSAPublicKey RSAPrivateKey
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        //公私钥对象存入map中
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    public static void main(String[] args) {
        BASE64Decoder base64decoder = new BASE64Decoder();
        try {
            //读取pem证书
            BufferedReader br = new BufferedReader(new FileReader("xxx.pem"));
            String s = br.readLine();
            StringBuffer publickey = new StringBuffer();
            while (s.charAt(0) != '-') {
                publickey.append(s + "\r");
                s = br.readLine();
            }
            byte[] keybyte = base64decoder.decodeBuffer(publickey.toString());
            KeyFactory kf = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keybyte);
            PublicKey publicKey = kf.generatePublic(keySpec);
            BASE64Encoder bse=new BASE64Encoder();
            System.out.println("pk:"+bse.encode(publicKey.getEncoded()));

            //被签的原文
            String toSign="MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEAtnj5ZE7R020rFkVERfNnQw6rGcyO\n" +
                    "dUKvIAWO1q3gzJU4ZJVivep7bo768DDLSWlO4g/birIAs7spW8jS/MhQ2wIDAQABAkBk7resh3jD\n" +
                    "BWDO+dORCdk1m3iDVcX7EL7D7K8dCqMNJg8tcYZInq8NfFGvflVk+D935Hif+G88xed00aXEVr5x\n" +
                    "AiEA/QkMaNijGJJoAaEAX6crJoDxKf26HQLIwe2fOZ0Gy6kCIQC4nEfKOEqaBOHr8UmKUpDys3nU\n" +
                    "mHqPlTMlSm+Mxpgq4wIgQ3tUSenspTL2dejANsJYaa5dors+FVqWu9Fpc24DT/ECIAXmCG0vq0KM\n" +
                    "kWNmjED9LmBy15uxW4km7UFtxW6sEcSdAiAjD9VQBCti6alzP9TegoGnsCc4zE0XTGgRO+BIFZao\n" +
                    "dw==";
            //生成的签名
            String sign="MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALZ4+WRO0dNtKxZFREXzZ0MOqxnMjnVCryAFjtat4MyV\n" +
                    "OGSVYr3qe26O+vAwy0lpTuIP24qyALO7KVvI0vzIUNsCAwEAAQ==";

            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initVerify(publicKey);
            signature.update(toSign.getBytes());
            boolean verify = signature.verify(base64decoder
                    .decodeBuffer(
                            sign));
            System.out.println(verify);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
