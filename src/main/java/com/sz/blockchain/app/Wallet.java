package com.sz.blockchain.app;

import com.sz.blockchain.data.Blockchain;
import com.sz.blockchain.transaction.Transaction;
import com.sz.blockchain.util.Base58Check;
import com.sz.blockchain.util.CryptoUtils;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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


    private PrivateKey privateKey;

    private byte[] publicKey;


    public Wallet(){
        try {
            initWallet();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成钱包地址
     * @return
     */
    public String getAddress() throws IOException {
        byte[] ripeMD160Hash = CryptoUtils.ripeMD160Hash(publicKey);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write((byte) 0);
        byteArrayOutputStream.write(ripeMD160Hash);
        byte[] versionedPayload = byteArrayOutputStream.toByteArray();

        // 3. 计算校验码
        byte[] checksum = CryptoUtils.checkSum(versionedPayload);

        // 4. 得到 version + payload + checksum 的组合
        byteArrayOutputStream.write(checksum);
        byte[] binaryAddress = byteArrayOutputStream.toByteArray();
        // 5. 执行Base58转换处理
        return Base58Check.rawBytesToBase58(binaryAddress);

    }


    private void initWallet() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        KeyPair keyPair = createKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        this.privateKey = privateKey;
        this.publicKey = publicKey.getEncoded();
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

    public static void trade(String sendAddress, String receiverAddress, int amount, PrivateKey privateKey) throws Exception {
        Blockchain blockChain = Blockchain.createBlockChain();
        Transaction transaction = Transaction.newTransaction(sendAddress, receiverAddress, amount, blockChain);
        blockChain.mineBlock(new Transaction[]{transaction});
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }
}
