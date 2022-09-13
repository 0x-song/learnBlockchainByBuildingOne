package com.sz.blockchain;

import com.sz.blockchain.data.Block;
import org.junit.jupiter.api.Test;

import java.util.List;

public class BlockchainTest {

    @Test
    public void test1(){
        Blockchain blockchain = new Blockchain();
//        System.out.println(blockchain);
        List<Block> chain = blockchain.getChain();
        String previous_hash = chain.get(chain.size() - 1).getHash();
        blockchain.new_block(previous_hash);
    }
}
