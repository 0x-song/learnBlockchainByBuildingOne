package com.sz.blockchain.app;

import com.sz.blockchain.data.Blockchain;
import com.sz.blockchain.transaction.TXOutput;
import com.sz.blockchain.transaction.Transaction;

/**
 * 客户端，区块链的上层应用
 */
public class Client {

    public static Account getBalance(String address){
        Blockchain blockChain = Blockchain.createBlockChain();
        TXOutput[] utxOs = blockChain.findUTXOs(address);
        int balance = 0;
        if(utxOs != null && utxOs.length > 0){
            for (TXOutput utxO : utxOs) {
                balance += utxO.getValue();
            }
        }
        return new Account(address, balance);
    }

    public static void trade(String sendAddress, String receiverAddress, int amount) throws Exception {
        Blockchain blockChain = Blockchain.createBlockChain();
        Transaction transaction = Transaction.newTransaction(sendAddress, receiverAddress, amount, blockChain);
        blockChain.mineBlock(new Transaction[]{transaction});
    }


}
