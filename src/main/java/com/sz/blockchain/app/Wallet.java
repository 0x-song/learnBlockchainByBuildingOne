package com.sz.blockchain.app;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;

import java.security.*;

/**
 * 公钥密码学
 * 数字签名:
 * 发送方将HelloWorld使用接收方公钥进行加密得到密文1，同时对HelloWorld进行hash，得到一份摘要1，再使用发送方私钥进行签名，将这两部分传递给接收方
 * 接收方接收到密文1，使用接收方自己的私钥进行解密，得出HelloWorld，再次对其进行hash，得到摘要。摘要1经过发送方公钥解密，可以暴露出原始摘要
 * 通过比对接收方得到两个摘要是否一致
 * btc使用椭圆曲线数字签名（ECDSA）来签署交易信息
 */
public class Wallet {

    /**
     * 账号校验码长度
     */
    public static final int ADDRESS_CHECKSUM_LENGTH = 4;



    private void initWallet(){
        createKeyPair();
    }

    /**
     * 创建一个新的私钥公钥对
     * @return
     */
    private KeyPair createKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        Security.addProvider(new BouncyCastleProvider());
        KeyPairGenerator generator = KeyPairGenerator.getInstance("ECDSA", BouncyCastleProvider.PROVIDER_NAME);
        //设定椭圆曲线参数
        ECNamedCurveParameterSpec secp256k1 = ECNamedCurveTable.getParameterSpec("secp256k1");
        generator.initialize(secp256k1, new SecureRandom());
        return generator.generateKeyPair();
    }

}
