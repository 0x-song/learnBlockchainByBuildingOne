package com.sz.blockchain.util;

import java.util.Random;

public class RandomUtils {

    public static String randomHexString(){
        Random random = new Random();
        long l = random.nextLong();
        String s = Long.toHexString(l);
        return s;
    }
}
