package com.sz.blockchain.transaction;

import com.sz.blockchain.data.Blockchain;
import com.sz.blockchain.util.Constant;
import com.sz.blockchain.util.CryptoUtils;

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
    private TXInput[] txTXInputs;

    //转账的output
    private TXOutput[] txTXOutputs;

    private boolean isCoinBase;


    public String getId() {
        return id;
    }

    public void setId() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(txTXInputs).append(txTXOutputs);
        String s = stringBuffer.toString();
        this.id = CryptoUtils.getTwiceSHA256(s);
    }

    public TXInput[] getTxInputs() {
        return txTXInputs;
    }

    public void setTxInputs(TXInput[] txTXInputs) {
        this.txTXInputs = txTXInputs;
    }

    public TXOutput[] getTxOutputs() {
        return txTXOutputs;
    }

    public void setTxOutputs(TXOutput[] txTXOutputs) {
        this.txTXOutputs = txTXOutputs;
    }

    public Transaction(String id, TXInput[] txTXInputs, TXOutput[] txTXOutputs) {
        this.id = id;
        this.txTXInputs = txTXInputs;
        this.txTXOutputs = txTXOutputs;
    }

    /**
     * CoinBase交易
     * @param receiverAddress
     * @return
     */
    public static Transaction coinBaseTX(String receiverAddress){
        TXInput txInput = new TXInput(null, -1, null);
        TXOutput txOutput = new TXOutput(Constant.SUBSIDY, receiverAddress);
        Transaction tx = new Transaction(null, new TXInput[]{txInput}, new TXOutput[]{txOutput});
        tx.setId();
        return tx;
    }

    public static Transaction newTransaction(String send, String receiver, int amount){
        return null;
    }

    public boolean isCoinBase(){
        return isCoinBase;
    }
}
