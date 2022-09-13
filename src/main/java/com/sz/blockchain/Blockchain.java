package com.sz.blockchain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sz.blockchain.data.Block;
import com.sz.blockchain.util.MessageDigestUtils;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Blockchain {

    private List<Block> chain;

    private List pending_transactions;

    public Blockchain() {
        this.chain = new LinkedList();
        this.pending_transactions = new LinkedList();

        //初始化创世区块
        System.out.println("Creating genesis block:初始化创世区块");
        new_block(null);
    }

    public void new_block(String previous_hash){
        Block block = new Block(chain.size(), new Date(), pending_transactions, previous_hash);
        //获取到当前区块的hash
        String hash = hash(block);
        block.setHash(hash);
        this.pending_transactions.clear();
        //将当前区块加入到区块链中
        this.chain.add(block);
        System.out.println("created block " + block.getIndex());
    }

    public String hash(Block block){
        ObjectMapper objectMapper = new ObjectMapper();
        String s = null;
        try {
            s = objectMapper.writeValueAsString(block);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return MessageDigestUtils.getSHA256Str(s);
    }

    public void last_block(){

    }

    public void new_transaction(){

    }

    public List<Block> getChain() {
        return chain;
    }

    public void setChain(List<Block> chain) {
        this.chain = chain;
    }

    public List getPending_transactions() {
        return pending_transactions;
    }

    public void setPending_transactions(List pending_transactions) {
        this.pending_transactions = pending_transactions;
    }
}
