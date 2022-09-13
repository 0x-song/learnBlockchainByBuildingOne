package com.sz.blockchain.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Date;
import java.util.List;
@JsonPropertyOrder({"index","timestamp","pending_transaction","previous_hash"})
public class Block {

    private Integer index;

    private Date timestamp;

    private List pending_transaction;

    private String previous_hash;

    @JsonIgnore
    private String hash;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public List getPending_transaction() {
        return pending_transaction;
    }

    public void setPending_transaction(List pending_transaction) {
        this.pending_transaction = pending_transaction;
    }

    public String getPrevious_hash() {
        return previous_hash;
    }

    public void setPrevious_hash(String previous_hash) {
        this.previous_hash = previous_hash;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Block(Integer index, Date timestamp, List pending_transaction, String previous_hash) {
        this.index = index;
        this.timestamp = timestamp;
        this.pending_transaction = pending_transaction;
        this.previous_hash = previous_hash;
    }
}
