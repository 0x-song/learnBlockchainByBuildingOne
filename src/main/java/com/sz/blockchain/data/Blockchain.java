package com.sz.blockchain.data;
import com.sz.blockchain.app.Wallet;
import com.sz.blockchain.app.WalletUtils;
import com.sz.blockchain.consensus.ProofOfWork;
import com.sz.blockchain.transaction.SpendableOutput;
import com.sz.blockchain.transaction.TXInput;
import com.sz.blockchain.transaction.TXOutput;
import com.sz.blockchain.transaction.Transaction;
import com.sz.blockchain.util.ArraysUtils;
import com.sz.blockchain.util.CryptoUtils;

import java.security.PrivateKey;
import java.util.*;

public class Blockchain {

    private List<Block> blockchain = new ArrayList<>();

    private static Blockchain blk = new Blockchain();

    public List<Block> getBlockchain() {
        return blockchain;
    }

    public void setBlockchain(List<Block> blockchain) {
        this.blockchain = blockchain;
    }

    public static Blockchain createBlockChain(){
        return blk;
    }

    /**
     * 创建一个区块
     * @param
     * @return
     */
    public Block createBlock(Transaction[] transactions){
        int size = blockchain.size();
        String previousHash = null;
        if(size == 0){
            previousHash = "0000000000000000000000000000000000000000000000000000000000000000";
        }else {
            previousHash = blockchain.get(size - 1).getHash();
        }
        Block block = new Block(size, previousHash, new Date(), transactions);
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
        Transaction[] transactions = new Transaction[]{Transaction.coinBaseTX(WalletUtils.newInstance().randomAddress())};
        return createBlock(transactions);
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

    /**
     * 挖矿
     * 在挖矿之前，先验证交易，交易没有问题，再去挖矿
     * @param transactions
     */
    public void mineBlock(Transaction[] transactions) throws Exception {
        for (Transaction transaction : transactions) {
            if(!transaction.isCoinBase()){
                if(verifyTransaction(transaction)){
                    throw new Exception("invalid transaction");
                }
            }
        }
        Block block = createBlock(transactions);
        addBlock(block);
    }

    private Blockchain(){
        Block genesisBlock = createGenesisBlock();
        addBlock(genesisBlock);
    }



    public TXOutput[] findUTXOs(String address){
        Wallet wallet = WalletUtils.newInstance().getWallet(address);
        byte[] publicKey = wallet.getPublicKey();
        byte[] pubKeyHash = CryptoUtils.ripeMD160Hash(publicKey);

        Transaction[] unspentTransactions = findUnspentTransactions(pubKeyHash);
        TXOutput[] UTXOs = {};
        if(unspentTransactions == null || unspentTransactions.length == 0){
            return UTXOs;
        }
        for (Transaction unspentTransaction : unspentTransactions) {
            for (TXOutput txOutput : unspentTransaction.getTxOutputs()) {
                if(txOutput.canUnlockUTXOs(pubKeyHash)){
                    UTXOs = ArraysUtils.add(UTXOs, txOutput);
                }
            }
        }
        return UTXOs;
    }
    /**
     * UTXO:未花费的交易输出
     * [CoinBase:------> zsquirrel 50] [[]---------> [{50,zsquirrel}]]      ---spent
     * [CoinBase:------> zsquirrel 50] [[]---------> [{50,zsquirrel}]]
     * [zsquirrel:-----> road2web3 5]  [{"1",0,zsquirrel}-----------> [{5, road2web3}]] [{"1",0,zsquirrel}----->[{45,zsquirrel}]]
     * [zsquirrel:-----> zsquirrel 45]
     * 终于搞明白了：交易输入、交易输出
     * 比如zsquirrel通过挖矿获得了50个btc，随后又通过挖矿又获得了50个btc
     * 随后zsquirrel给road2web3转了5个btc，那么会使用上述两个输出的一个作为输入
     * 比如使用第一笔，该笔输入必须全部花完。输入 zsquirrel,txId,-1 --------> 输出: 5,road2web3  45,zsquirrel 此时会有两个输出；一个交易，一个指向自己，为找零
     * @param pubKeyHash
     */
    public Transaction[] findUnspentTransactions(byte[] pubKeyHash){
        Map<String, int[]> spentTXOs = findAllSpentTXOs(pubKeyHash);
        Transaction[] unspentTXs = {};
        for (Block block : blockchain) {
            for (Transaction transaction : block.getTransactions()) {
                String txId = transaction.getId();
                int[] spentOutIndexArray = spentTXOs.get(txId);
                //交易中查看所有的output，如果该output已经在input中存在了，说明已经被消费了
                for (int outputIndex = 0; outputIndex < transaction.getTxOutputs().length; outputIndex++) {
                    //如果这个output在input中出现过，则已经消费过了，查找没有被消费过的
                    if(spentOutIndexArray != null && ArraysUtils.contains(spentOutIndexArray, outputIndex)){
                        continue;
                    }
                    if(transaction.getTxOutputs()[outputIndex].canUnlockUTXOs(pubKeyHash)){
                        unspentTXs = ArraysUtils.add(unspentTXs, transaction);
                    }
                }
            }
        }
        return unspentTXs;
    }

    /**
     * 查找所有的input，因为input里面存储的是上次交易的编号以及outputIndex，所以通过这种方式便可以知道哪些output被消费了
     * @param pubKeyHash
     * @return
     */
    private Map<String, int[]> findAllSpentTXOs(byte[] pubKeyHash) {
        Map<String, int[]> spentXOs = new HashMap<>();
        for (Block block : blockchain) {
            Transaction[] transactions = block.getTransactions();
            for (Transaction transaction : transactions) {
                if(transaction.isCoinBase()){
                    continue;
                }
                for (TXInput txInput : transaction.getTxInputs()) {
                    if(txInput.verifyPubKey(pubKeyHash)){
                        String txId = txInput.getTxId();
                        int[] spentOutIndexArray = spentXOs.get(txId);
                        if(spentOutIndexArray == null){
                            spentXOs.put(txId, new int[]{txInput.getTxOutputIndex()});
                        }else {
                            spentOutIndexArray = ArraysUtils.add(spentOutIndexArray, txInput.getTxOutputIndex());
                            spentXOs.put(txId, spentOutIndexArray);
                        }
                    }
                }
            }
        }
        return spentXOs;
    }

    /**
     * 寻找可用于进行交易的output，不需要加载出全部的output
     * @param pubKeyHash
     * @param amount
     * @return
     */
    public SpendableOutput findSpendableOutputs(byte[] pubKeyHash, int amount) {
        Transaction[] unspentTransactions = findUnspentTransactions(pubKeyHash);
        int accumulatedAmount = 0;
        Map<String, int[]> unspentOutputs = new HashMap<>();
        for (Transaction unspentTransaction : unspentTransactions) {
            String txId = unspentTransaction.getId();
            for (int outputId = 0; outputId < unspentTransaction.getTxOutputs().length; outputId++) {
                TXOutput txOutput = unspentTransaction.getTxOutputs()[outputId];
                if(txOutput.canUnlockUTXOs(pubKeyHash) &&  accumulatedAmount < amount){
                    accumulatedAmount += txOutput.getValue();
                }
                int[] outputIds = unspentOutputs.get(txId);
                if(outputIds == null){
                    outputIds = new int[]{outputId};
                }else {
                    outputIds = ArraysUtils.add(outputIds, outputId);
                }
                unspentOutputs.put(txId, outputIds);
            }
            if(accumulatedAmount >= amount){
                break;
            }
        }
        return new SpendableOutput(accumulatedAmount, unspentOutputs);
    }

    /**
     * 根据交易的编号查找指定的交易
     * @param txId
     * @return
     * @throws Exception
     */
    public Transaction findTX(String txId) throws Exception {
        for (Block block : blockchain) {
            Transaction[] transactions = block.getTransactions();
            for (Transaction transaction : transactions) {
                if(txId.equals(transaction.getId())){
                    return transaction;
                }
            }
        }
        throw new Exception("invalid txId");
    }

    /**
     * 进行交易签名
     * @param transaction
     * @param privateKey
     * @throws Exception
     */
    public void signTransaction(Transaction transaction, PrivateKey privateKey) throws Exception {
        Map<String, Transaction> preTXMap = new HashMap<>();
        for (TXInput txInput : transaction.getTxInputs()) {
            Transaction preTX = findTX(txInput.getTxId());
            preTXMap.put(txInput.getTxId(), preTX);
        }
        transaction.sign(privateKey, preTXMap);
    }


    /**
     * 验证交易
     * @param transaction
     * @return
     * @throws Exception
     */
    public boolean verifyTransaction(Transaction transaction) throws Exception {
        Map<String, Transaction> preTXMap = new HashMap<>();
        for (TXInput txInput : transaction.getTxInputs()) {
            Transaction preTX = findTX(txInput.getTxId());
            preTXMap.put(txInput.getTxId(), preTX);
        }
        return transaction.verify(preTXMap);
    }
}
