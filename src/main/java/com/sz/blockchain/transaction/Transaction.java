package com.sz.blockchain.transaction;

import com.sz.blockchain.app.Wallet;
import com.sz.blockchain.app.WalletUtils;
import com.sz.blockchain.data.Blockchain;
import com.sz.blockchain.util.ArraysUtils;
import com.sz.blockchain.util.Constant;
import com.sz.blockchain.util.CryptoUtils;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

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

    private boolean isCoinBase = false;


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
        TXInput txInput = new TXInput(null, -1, null, null);
//        TXOutput txOutput = new TXOutput(Constant.SUBSIDY, receiverAddress);
        TXOutput txOutput = TXOutput.newTXOutput(Constant.SUBSIDY, receiverAddress);
        Transaction tx = new Transaction(null, new TXInput[]{txInput}, new TXOutput[]{txOutput});
        tx.setId();
        tx.setCoinBase(true);
        return tx;
    }

    public static Transaction newTransaction(String send, String receiver, int amount, Blockchain blockchain) throws Exception {
        Wallet wallet = WalletUtils.newInstance().getWallet(send);
        byte[] publicKey = wallet.getPublicKey();
        byte[] pubKeyHash = CryptoUtils.ripeMD160Hash(publicKey);

        SpendableOutput spendableOutputs = blockchain.findSpendableOutputs(pubKeyHash, amount);
        Map<String, int[]> unspentOutputs = spendableOutputs.getUnspentOutputs();
        int accumulatedAmount = spendableOutputs.getAccumulatedAmount();
        if(accumulatedAmount < amount){
            throw new Exception("No Enough funds");
        }
        //需要把output构建成input
        TXInput[] txInputs = {};
        Set<String> txIds = unspentOutputs.keySet();
        for (String txId : txIds) {
            int[] outIds = unspentOutputs.get(txId);
            for (int outId : outIds) {
                txInputs = ArraysUtils.add(txInputs, new TXInput(txId, outId, null, publicKey));

            }
        }
        //构建output
        TXOutput[] txOutputs = {};
        txOutputs = ArraysUtils.add(txOutputs, TXOutput.newTXOutput(amount, receiver));
//        txOutputs = ArraysUtils.add(txOutputs, new TXOutput(amount, receiver));
        if(accumulatedAmount > amount){
            //如果余额大于需要转账的金额，那么需要设置找零
            txOutputs = ArraysUtils.add(txOutputs, TXOutput.newTXOutput((accumulatedAmount - amount), send));
//            txOutputs = ArraysUtils.add(txOutputs, new TXOutput(accumulatedAmount - amount, send));
        }
        Transaction newTX = new Transaction(null, txInputs, txOutputs);
        blockchain.signTransaction(newTX, wallet.getPrivateKey());
        newTX.setId();
        return newTX;
    }

    public boolean isCoinBase(){
        return isCoinBase;
    }

    public void setCoinBase(boolean coinBase) {
        isCoinBase = coinBase;
    }

    /**
     * 对交易进行签名
     * @param privateKey 私钥
     * @param previousTX 之前的交易信息
     */
    public void sign(PrivateKey privateKey, Map<String, Transaction> previousTX) throws Exception {
        if(this.isCoinBase()){
            return;
        }
        for (TXInput txInput : getTxInputs()) {
            if(previousTX.get(txInput.getTxId()) == null){
                throw new Exception("incorrect transaction");
            }
        }
        //生成一份副本对交易信息进行签名
        Transaction copyTX = copyTX();
        Security.addProvider(new BouncyCastleProvider());
        Signature eCDSASignature = Signature.getInstance("SHA256withECDSA", BouncyCastleProvider.PROVIDER_NAME);
        eCDSASignature.initSign(privateKey);
        for (int i = 0; i < copyTX.getTxInputs().length; i++) {
            TXInput copyTxInput = copyTX.getTxInputs()[i];
            Transaction preTX = previousTX.get(copyTxInput.getTxId());
            TXOutput preTxOutput = preTX.getTxOutputs()[copyTxInput.getTxOutputIndex()];
            copyTxInput.setPubKey(preTxOutput.getPubKeyHash());
            copyTxInput.setSignature(null);
            //设置交易的编号
            copyTX.setId();
            eCDSASignature.update(copyTX.getId().getBytes(StandardCharsets.UTF_8));
            byte[] sign = eCDSASignature.sign();
            this.getTxInputs()[i].setSignature(sign);
        }
    }

    /**
     * 对前面的交易进行验证
     * @param previousTX
     * @return
     */
    public boolean verify(Map<String, Transaction> previousTX) throws Exception {
        if(this.isCoinBase){
            return true;
        }
        for (TXInput txInput : getTxInputs()) {
            if(previousTX.get(txInput.getTxId()) == null){
                throw new Exception("incorrect transaction");
            }
        }
        Transaction copyTX = copyTX();
        Security.addProvider(new BouncyCastleProvider());
        ECParameterSpec ecParameters = ECNamedCurveTable.getParameterSpec("secp256k1");
        KeyFactory keyFactory = KeyFactory.getInstance("ECDSA", BouncyCastleProvider.PROVIDER_NAME);
        Signature eCDSAVerify = Signature.getInstance("SHA256withECDSA", BouncyCastleProvider.PROVIDER_NAME);
        for (int i = 0; i < getTxInputs().length; i++) {
            TXInput txInput = getTxInputs()[i];
            Transaction preTX = previousTX.get(txInput.getTxId());
            TXOutput preTxOutput = preTX.getTxOutputs()[txInput.getTxOutputIndex()];
            TXInput copyTxInput = copyTX.getTxInputs()[i];
            copyTxInput.setSignature(null);
            copyTxInput.setPubKey(preTxOutput.getPubKeyHash());
            copyTX.setId();
            BigInteger x = new BigInteger(1, Arrays.copyOfRange(txInput.getPubKey(), 1, 33));
            BigInteger y = new BigInteger(1, Arrays.copyOfRange(txInput.getPubKey(), 33, 65));
            ECPoint ecPoint = ecParameters.getCurve().createPoint(x, y, false);
            ECPublicKeySpec keySpec = new ECPublicKeySpec(ecPoint, ecParameters);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            eCDSAVerify.initVerify(publicKey);
            eCDSAVerify.update(copyTX.getId().getBytes(StandardCharsets.UTF_8));
            if(!eCDSAVerify.verify(txInput.getSignature())){
                return false;
            }
        }
        return true;
    }


    /**
     * 创建交易副本
     * @return
     */
    private Transaction copyTX() {
        TXInput[] copyTXInputs = new TXInput[getTxInputs().length];
        for (int i = 0; i < getTxInputs().length; i++) {
            TXInput txInput = getTxInputs()[i];
            copyTXInputs[i] = new TXInput(txInput.getTxId(), txInput.getTxOutputIndex(), null, null);
        }
        TXOutput[] copyTXOutputs = new  TXOutput[getTxOutputs().length];
        for (int i = 0; i < getTxOutputs().length; i++) {
            TXOutput txOutput = getTxOutputs()[i];
            copyTXOutputs[i] = new TXOutput(txOutput.getValue(), txOutput.getPubKeyHash());
        }
        return new Transaction(this.getId(), copyTXInputs, copyTXOutputs);
    }
}
