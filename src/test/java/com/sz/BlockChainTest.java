package com.sz;

import com.google.gson.Gson;
import com.sz.blockchain.app.Account;
import com.sz.blockchain.app.Client;
import com.sz.blockchain.app.Wallet;
import com.sz.blockchain.app.WalletUtils;
import com.sz.blockchain.data.Blockchain;
import com.sz.blockchain.transaction.Transaction;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class BlockChainTest {

    Gson gson = new Gson();

    @Test
    public void test1(){
        String coetent = "5701ac72fcb8a200fe12479c1d3669dc1c460edb30129f61fb1e53f8b7ea8d70";
        char[] chars = coetent.toCharArray();
        System.out.println(chars.length);
    }

    /**
     * Account{address='zsquirrel', balance=100}
     * Account{address='zsquirrel', balance=95}
     * Account{address='road2web3', balance=5}
     * 转账案例完成
     * 引入签名之后，不使用该测试用例进行测试了
     * @throws Exception
     */
    @Test
    public void test2() throws Exception {
        Blockchain blockChain = Blockchain.createBlockChain();
        blockChain.mineBlock(new Transaction[]{Transaction.coinBaseTX("zsquirrel")});
        blockChain.mineBlock(new Transaction[]{Transaction.coinBaseTX("zsquirrel")});
        Account balance = Client.getBalance("zsquirrel");
        System.out.println(balance);
        blockChain.mineBlock(new Transaction[]{Transaction.newTransaction("zsquirrel","road2web3", 5, blockChain)});
        Account balance1 = Client.getBalance("zsquirrel");
        System.out.println(balance1);
        Account road2web3 = Client.getBalance("road2web3");
        System.out.println(road2web3);
    }

    @Test
    public void testWallet() throws IOException {
        Wallet wallet = new Wallet();
        String address = wallet.getAddress();
        System.out.println(address);
    }

    @Test
    public void test3() throws Exception {
        Blockchain blockChain = Blockchain.createBlockChain();
        String sender = WalletUtils.newInstance().randomAddress();
        String receiver = WalletUtils.newInstance().randomAddress();

        blockChain.mineBlock(new Transaction[]{Transaction.coinBaseTX(sender)});
        blockChain.mineBlock(new Transaction[]{Transaction.coinBaseTX(sender)});
        Account balance = Client.getBalance(sender);
        System.out.println(balance);
        blockChain.mineBlock(new Transaction[]{Transaction.newTransaction(sender,sender, 5, blockChain)});
        Account balance1 = Client.getBalance(sender);
        System.out.println(balance1);
        Account road2web3 = Client.getBalance(receiver);
        System.out.println(road2web3);
    }

    @Test
    public void testRandomAddress(){
        for (int i = 0; i < 10; i++) {
            WalletUtils walletUtils = WalletUtils.newInstance();
            String s = walletUtils.randomAddress();
            System.out.println(s);
        }

    }
}
