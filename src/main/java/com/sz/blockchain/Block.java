package com.sz.blockchain;

import com.sz.blockchain.util.CryptoUtils;

import java.util.Date;

public class Block {

    private int index;

    private String hash;

    private String previousHash;

    private Date currentTime;

    private String data;


    public void setHash(){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer = stringBuffer.append(index).append(hash).append(previousHash).append(currentTime).append(data);
        this.hash = CryptoUtils.getTwiceSHA256(stringBuffer.toString());
    }

    public String getHash(){
        return hash;
    }


    public Block(int index, String previousHash, Date currentTime, String data) {
        this.index = index;
        this.previousHash = previousHash;
        this.currentTime = currentTime;
        this.data = data;
    }
}
