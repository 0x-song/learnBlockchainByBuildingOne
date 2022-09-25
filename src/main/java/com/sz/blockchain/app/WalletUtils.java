package com.sz.blockchain.app;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WalletUtils {

    private static final int WALLET_NUMBER = 10;

    private static Map<String, Wallet> walletMap = new HashMap<>();

    private static WalletUtils walletUtils = new WalletUtils();


    public static WalletUtils newInstance(){
        return walletUtils;
    }

    private WalletUtils(){
        initWallet(WALLET_NUMBER);
    }

    private void initWallet(int walletNumber) {
        for (int i = 0; i < walletNumber; i++) {
            Wallet wallet = new Wallet();
            try {
                walletMap.put(wallet.getAddress(), wallet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Wallet getWallet(String address){
        return walletMap.get(address);
    }

    public String randomAddress(){
      int index = (int) (Math.random() * (walletMap.size() - 1));
        Set<String> strings = walletMap.keySet();
        int number = 0;
        for (String string : strings) {
            if(index == number){
                return string;
            }
            number ++;
        }
        return null;
    }
}
