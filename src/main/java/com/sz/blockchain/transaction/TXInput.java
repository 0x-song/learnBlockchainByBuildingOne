package com.sz.blockchain.transaction;

import com.sz.blockchain.util.CryptoUtils;

import java.util.Arrays;

public class TXInput {

    //上一次交易编号
    private String txId;

    //上一次交易的第几个output
    private int txOutputIndex;

    private byte[] signature;

    private byte[] pubKey;

    /**
     * 查找已花费的交易输出时需要用到
     * @param pubKeyHash
     * @return
     */
    public boolean verifyPubKey(byte[] pubKeyHash){
        byte[] ripeMD160Hash = CryptoUtils.ripeMD160Hash(pubKey);
        return Arrays.equals(ripeMD160Hash, pubKeyHash);
    }


    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public int getTxOutputIndex() {
        return txOutputIndex;
    }

    public void setTxOutputIndex(int txOutputIndex) {
        this.txOutputIndex = txOutputIndex;
    }

    public TXInput(String txId, int txOutputIndex, byte[] signature, byte[] pubKey) {
        this.txId = txId;
        this.txOutputIndex = txOutputIndex;
        this.signature = signature;
        this.pubKey = pubKey;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public byte[] getPubKey() {
        return pubKey;
    }

    public void setPubKey(byte[] pubKey) {
        this.pubKey = pubKey;
    }

    public TXInput() {
    }
}
