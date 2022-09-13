package com.sz.blockchain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sz.blockchain.data.Block;
import com.sz.blockchain.util.MessageDigestUtils;
import com.sz.blockchain.util.RandomUtils;

import java.util.*;

public class Blockchain {

    private List<Block> chain;

    private List pending_transactions;

    public Blockchain() {
        this.chain = new LinkedList();
        this.pending_transactions = new LinkedList();

        //初始化创世区块
        System.out.println("Creating genesis block:初始化创世区块");
        Block block = new_block();
        this.chain.add(block);
    }

    public Block new_block(){
        Block last_block = last_block();
        String previous_hash = null;
        if(last_block != null){
            previous_hash = last_block.getHash();
        }
        Block block = new Block(chain.size(), new Date(), pending_transactions, previous_hash, RandomUtils.randomHexString());
        //获取到当前区块的hash
        String hash = hash(block);
        block.setHash(hash);
        this.pending_transactions.clear();
        //将当前区块加入到区块链中
        //this.chain.add(block);
//        System.out.println("created block " + block.getIndex());
        return block;
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

    public Block last_block(){
        if(chain.size() == 0){
            return null;
        }
        return chain.get(chain.size() - 1);
    }

    public void new_transaction(){

    }

    public void proof_of_work(){
        Block block  = null;
        while (true){
            block = new_block();
            if(valid_hash(block)){
                break;
            }
        }
        this.chain.add(block);
        System.out.println("mined a new block:出块了" + block);
    }

    public boolean valid_hash(Block block){
        return block.getHash().startsWith("0000");
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
