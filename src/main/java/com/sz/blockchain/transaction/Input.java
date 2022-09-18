package com.sz.blockchain.transaction;

public class Input {

    //上一次交易编号
    private String txId;

    //上一次交易的第几个output
    private int outIdx;

    private String sendAddress;

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public int getOutIdx() {
        return outIdx;
    }

    public void setOutIdx(int outIdx) {
        this.outIdx = outIdx;
    }

    public String getSendAddress() {
        return sendAddress;
    }

    public void setSendAddress(String sendAddress) {
        this.sendAddress = sendAddress;
    }

    public Input(String txId, int outIdx, String sendAddress) {
        this.txId = txId;
        this.outIdx = outIdx;
        this.sendAddress = sendAddress;
    }
}
