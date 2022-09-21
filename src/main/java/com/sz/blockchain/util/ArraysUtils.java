package com.sz.blockchain.util;

import com.sz.blockchain.transaction.TXOutput;
import com.sz.blockchain.transaction.Transaction;

import java.util.Arrays;

public class ArraysUtils {

    public static int[] add(int[] arrays, int b){
        int[] newArray = new int[arrays.length + 1];
        for (int i = 0; i < arrays.length; i++) {
            newArray[i] = arrays[i];
        }
        newArray[arrays.length] = b;
        return newArray;
    }

    public static boolean contains(int[] array, int i) {
        for (int j = 0; j < array.length; j++) {
            if(i == array[j]){
                return true;
            }
        }
        return false;
    }

    public static Transaction[] add(Transaction[] unspentTXs, Transaction transaction) {
        Transaction[] newTX = new Transaction[unspentTXs.length + 1];
        for (int i = 0; i < unspentTXs.length; i++) {
            newTX[i] = unspentTXs[i];
        }
        newTX[unspentTXs.length] = transaction;
        return newTX;
    }

    public static TXOutput[] add(TXOutput[] utxOs, TXOutput txOutput) {
        TXOutput[] newUTXOs = new TXOutput[utxOs.length + 1];
        for (int i = 0; i < utxOs.length; i++) {
            newUTXOs[i] = utxOs[i];
        }
        newUTXOs[utxOs.length] = txOutput;
        return newUTXOs;
    }
}
