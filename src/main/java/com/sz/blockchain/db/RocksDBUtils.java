package com.sz.blockchain.db;

import com.sz.blockchain.data.Block;
import com.sz.blockchain.util.SerializeUtils;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

import java.util.HashMap;
import java.util.Map;

/**
 * blocks:存储链上所有区块的元数据
 * chainstate:存储区块链的状态.主要存储UTXO
 */
public class RocksDBUtils {

    private static final String DB_FILE = "blockchain.db";

    /**
     * rocksDB数据库的key值
     */
    private static final String BLOCK_BUCKET_KEY = "block";

    private static final String LATEST_BLOCK_HASH = "latest";

    private static RocksDB rocksDB;

    private static Map<String, byte[]> blockBucket = new HashMap<>();

    static {
        openDB();
        initBucket();
    }

    private static void openDB(){
        try {
            rocksDB = RocksDB.open(DB_FILE);
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
    }

    /**
     * 形成block------map序列化  映射键值对来充当bucket
     *
     */
    private static void initBucket() {
        byte[] bucketKey = SerializeUtils.serialize(BLOCK_BUCKET_KEY);
        try {
            byte[] bucketValue = rocksDB.get(bucketKey);
            if(bucketValue == null){
                rocksDB.put(bucketKey, SerializeUtils.serialize(blockBucket));
            }else {
                blockBucket = (Map<String, byte[]>) SerializeUtils.deserialize(bucketValue);
            }
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存区块链最近一个区块的hash值
     */
    public static void setLatestBlockHash(String latestBlockHash){
        try {
            blockBucket.put(LATEST_BLOCK_HASH, SerializeUtils.serialize(latestBlockHash));
            rocksDB.put(SerializeUtils.serialize(BLOCK_BUCKET_KEY), SerializeUtils.serialize(blockBucket));
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取区块链最近一个区块的hash值
     * @return
     */
    public static String getLatestBlockHash(){
        byte[] latestBlockHashByte = blockBucket.get(LATEST_BLOCK_HASH);
        if(latestBlockHashByte != null){
            return (String) SerializeUtils.deserialize(latestBlockHashByte);
        }
        return null;
    }

    /**
     * 将区块保存到数据库
     * @param block
     */
    public static void putBlock(Block block){
        try {
            blockBucket.put(block.getHash(), SerializeUtils.serialize(block));
            rocksDB.put(SerializeUtils.serialize(BLOCK_BUCKET_KEY), SerializeUtils.serialize(blockBucket));
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据区块hash查询区块
     * @param blockHash
     * @return
     */
    public static Block getBlock(String blockHash){
        Block block = (Block) SerializeUtils.deserialize(blockBucket.get(blockHash));
        return block;
    }

    /**
     * 关闭数据库
     */
    public static void close(){
        rocksDB.close();
    }

}
