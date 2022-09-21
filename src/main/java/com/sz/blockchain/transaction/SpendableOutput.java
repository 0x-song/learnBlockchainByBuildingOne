package com.sz.blockchain.transaction;

import java.util.Map;

public class SpendableOutput {

    private int accumulatedAmount;

    private Map<String, int[]> unspentOutputs;

    public SpendableOutput(int accumulatedAmount, Map<String, int[]> unspentOutputs) {
        this.accumulatedAmount = accumulatedAmount;
        this.unspentOutputs = unspentOutputs;
    }

    public int getAccumulatedAmount() {
        return accumulatedAmount;
    }

    public void setAccumulatedAmount(int accumulatedAmount) {
        this.accumulatedAmount = accumulatedAmount;
    }

    public Map<String, int[]> getUnspentOutputs() {
        return unspentOutputs;
    }

    public void setUnspentOutputs(Map<String, int[]> unspentOutputs) {
        this.unspentOutputs = unspentOutputs;
    }
}
