package com.sz;

import com.google.gson.Gson;
import com.sz.blockchain.Block;
import com.sz.blockchain.Blockchain;
import org.junit.jupiter.api.Test;

public class BlockChainTest {

    Gson gson = new Gson();

    @Test
    public void test1(){
        String coetent = "5701ac72fcb8a200fe12479c1d3669dc1c460edb30129f61fb1e53f8b7ea8d70";
        char[] chars = coetent.toCharArray();
        System.out.println(chars.length);
    }

    @Test
    public void test2() throws InterruptedException {
        Blockchain blockchain = new Blockchain();
        System.out.println(gson.toJson(blockchain));
        Thread.sleep(2000);
        Block secondBlock = blockchain.createBlock("the second block");
        blockchain.addBlock(secondBlock);
        System.out.println(gson.toJson(blockchain));
        Thread.sleep(2000);
        Block thirdBlock = blockchain.createBlock("the third block");
        blockchain.addBlock(thirdBlock);
        System.out.println(gson.toJson(blockchain));
    }
}
