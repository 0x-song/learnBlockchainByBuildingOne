package com.sz.blockchain.transaction;

import com.sz.blockchain.util.Base58Check;

import java.util.Arrays;

public class TXOutput {
    //转出的资产
    private int value;

    //资产的接收方地址
//    private String receiveAddress;

    private byte[] pubKeyHash;

    /**
     * 指定公钥是否可以解锁交易输出
     * @param pubKeyHash
     * @return
     */
    public boolean canUnlockUTXOs(byte[] pubKeyHash){
        return Arrays.equals(pubKeyHash, this.pubKeyHash);
    }

    public static TXOutput newTXOutput(int value, String receiveAddress){
        byte[] bytes = Base58Check.base58ToBytes(receiveAddress);
        byte[] pubKeyHash = Arrays.copyOfRange(bytes, 1, bytes.length);
        return new TXOutput(value, pubKeyHash);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public byte[] getPubKeyHash() {
        return pubKeyHash;
    }

    public void setPubKeyHash(byte[] pubKeyHash) {
        this.pubKeyHash = pubKeyHash;
    }

    public TXOutput(int value, byte[] pubKeyHash) {
        this.value = value;
        this.pubKeyHash = pubKeyHash;
    }

    public TXOutput() {
    }
}
