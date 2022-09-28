package com.sz.blockchain.consensus;

import com.sz.blockchain.data.Block;
import com.sz.blockchain.util.Constant;
import com.sz.blockchain.util.CryptoUtils;
import com.sz.blockchain.util.MathUtils;

import java.util.Random;

/**
 * PoW共识算法
 */
public class ProofOfWork {

    public static int getTarget(){
        int target = 1;
        target = target << MathUtils.uint(256 - Constant.POW_DIFFICULTY);
        return target;
    }

    public static long findNonce(Block block){
        long nonce;
        while (true){
            nonce = MathUtils.randomLong();
            String info = block.getBlockInfoWithNonce(nonce);
            String twiceSHA256 = CryptoUtils.getTwiceSHA256(info);
            int hashCode = twiceSHA256.hashCode();
            if(hashCode < getTarget()){
                break;
            }else {
                nonce ++;
            }
        }
        return nonce;
    }

    public static boolean validatePow(Block block) throws Exception{
        String info = block.getBlockInfoWithNonce(block.getNonce());
        String twiceSHA256 = CryptoUtils.getTwiceSHA256(info);
        int hashCode = twiceSHA256.hashCode();
        if(hashCode < getTarget()){
            return true;
        }
        return false;
    }

}
