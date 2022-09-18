package com.sz.blockchain;
import java.util.Date;
import java.util.List;


import java.util.ArrayList;

public class Blockchain {

    private List<Block> blockchain = new ArrayList<>();

    public List<Block> getBlockchain() {
        return blockchain;
    }

    public void setBlockchain(List<Block> blockchain) {
        this.blockchain = blockchain;
    }

    /**
     * 创建一个区块
     * @param data
     * @return
     */
    public Block createBlock(String data){
        int size = blockchain.size();
        String previousHash = null;
        if(size == 0){
            previousHash = "0000000000000000000000000000000000000000000000000000000000000000";
        }else {
            previousHash = blockchain.get(size - 1).getHash();
        }
        Block block = new Block(size, previousHash, new Date(), data);
        block.setHash();
        return block;
    }

    /**
     * 创建创世区块
     * @return
     */
    public Block createGenesisBlock(){
        return createBlock("This is the genesis block of my blockchain");
    }

    /**
     * 将一个区块加入到区块链中
     * @param block
     */
    public void addBlock(Block block){
        blockchain.add(block);
    }

    public Blockchain(){
        Block genesisBlock = createGenesisBlock();
        addBlock(genesisBlock);
    }
}
