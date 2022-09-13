package com.sz.blockchain;

import com.sz.blockchain.data.Block;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class BlockchainTest {

    @Test
    public void test1(){
        Blockchain blockchain = new Blockchain();
//        System.out.println(blockchain);
        List<Block> chain = blockchain.getChain();
        String previous_hash = chain.get(chain.size() - 1).getHash();
        blockchain.new_block();
    }

    @Test
    public void test2(){
        Random random = new Random();
        long l = random.nextLong();
        String s = Long.toHexString(l);
        System.out.println(s);
    }

    @Test
    public void test3(){
        Blockchain blockchain = new Blockchain();
        blockchain.proof_of_work();
        blockchain.proof_of_work();
        blockchain.proof_of_work();
        blockchain.proof_of_work();
        System.out.println(blockchain.getChain());
    }
}
