package com.sz.blockchain.transaction;

public class TXInput {

    //上一次交易编号
    private String txId;

    //上一次交易的第几个output
    private int txOutputIndex;

    private String sendAddress;

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

    public String getSendAddress() {
        return sendAddress;
    }

    public void setSendAddress(String sendAddress) {
        this.sendAddress = sendAddress;
    }

    public TXInput(String txId, int txOutputIndex, String sendAddress) {
        this.txId = txId;
        this.txOutputIndex = txOutputIndex;
        this.sendAddress = sendAddress;
    }
}
