package com.sz.blockchain.transaction;

public class TXOutput {
    //转出的资产
    private int value;

    //资产的接收方地址
    private String receiveAddress;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }

    public TXOutput(int value, String receiveAddress) {
        this.value = value;
        this.receiveAddress = receiveAddress;
    }
}
