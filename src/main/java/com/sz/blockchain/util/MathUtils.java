package com.sz.blockchain.util;

import java.util.Random;

public class MathUtils {

    public static int uint(int number){
        if(number >= 0){
            return number;
        }else {
            return -number;
        }
    }

    public static long randomLong(){
        Random random = new Random();
        return random.nextLong();
    }
}
