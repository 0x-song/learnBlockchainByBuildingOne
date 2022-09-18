package com.sz.blockchain.data;
import com.sun.tools.classfile.ConstantPool;
import com.sz.blockchain.consensus.ProofOfWork;
import com.sz.blockchain.transaction.Input;
import com.sz.blockchain.transaction.Output;
import com.sz.blockchain.transaction.Transaction;

import java.util.*;

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
    public Block createBlock(String data, List<Transaction> transactions){
        int size = blockchain.size();
        String previousHash = null;
        if(size == 0){
            previousHash = "0000000000000000000000000000000000000000000000000000000000000000";
        }else {
            previousHash = blockchain.get(size - 1).getHash();
        }
        Block block = new Block(size, previousHash, new Date(), data, transactions);
        long nonce = ProofOfWork.findNonce(block);
        block.setNonce(nonce);
        block.setHash();
        return block;
    }

    /**
     * 创建创世区块
     * @return
     */
    public Block createGenesisBlock(){
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(Transaction.genesisTx());
        return createBlock("This is the genesis block of my blockchain", transactions);
    }

    /**
     * 将一个区块加入到区块链中
     * @param block
     */
    public void addBlock(Block block){
        boolean result = ProofOfWork.validatePow(block);
        if(!result){}
        blockchain.add(block);
    }

    public Blockchain(){
        Block genesisBlock = createGenesisBlock();
        addBlock(genesisBlock);
    }

    public List<Output> findUTXOs(String address){
        return null;
    }
}
