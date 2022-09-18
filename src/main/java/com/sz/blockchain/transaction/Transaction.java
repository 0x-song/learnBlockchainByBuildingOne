package com.sz.blockchain.transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * UTXO:未花费的交易输出
 * [CoinBase:------> zsquirrel 50] [[]---------> [{50,zsquirrel}]]      ---spent
 * [CoinBase:------> zsquirrel 50] [[]---------> [{50,zsquirrel}]]
 * [zsquirrel:-----> road2web3 5]  [{"genesis transaction id",0,zsquirrel}-----------> [{5, road2web3}]] [{"genesis transaction id",0,zsquirrel}----->[{45,zsquirrel}]]
 * [zsquirrel:-----> zsquirrel 45]
 */
public class Transaction {

    //此时交易的hash值
    private String id;

    //转账的input:本次转账的前置交易的output
    private List<Input> txInputs;

    //转账的output
    private List<Output> txOutputs;

    public Transaction(String id, List<Input> txInputs, List<Output> txOutputs) {
        this.id = id;
        this.txInputs = txInputs;
        this.txOutputs = txOutputs;
    }

    public static Transaction genesisTx(){
        List<Input> txIn = new ArrayList<>();
        List<Output> txOut = new ArrayList<>();
        txIn.add(new Input(null, -1, null));
        txOut.add(new Output(50, "1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa"));
        Transaction genesisTx = new Transaction("This is the genesis transaction", txIn, txOut);
        return genesisTx;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Input> getTxInputs() {
        return txInputs;
    }

    public void setTxInputs(List<Input> txInputs) {
        this.txInputs = txInputs;
    }

    public List<Output> getTxOutputs() {
        return txOutputs;
    }

    public void setTxOutputs(List<Output> txOutputs) {
        this.txOutputs = txOutputs;
    }
}
