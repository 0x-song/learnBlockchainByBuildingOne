package com.sz.blockchain.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * 序列化、反序列化工具类
 * 我们需要将区块链持久化保存到硬盘上
 */
public class SerializeUtils {

    /**
     * 将字节数组转换成对象
     * 反序列化
     * @param bytes
     * @return
     */
    public static Object deserialize(byte[] bytes){
        Input input = new Input(bytes);
        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);
        Object o = kryo.readClassAndObject(input);
        input.close();
        return o;
    }

    /**
     * 序列化
     * @param o
     * @return
     */
    public static byte[] serialize(Object o){
        Output output = new Output(102400,1024000);
        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);
        kryo.writeClassAndObject(output, o);
        byte[] bytes = output.toBytes();
        output.close();
        return bytes;
    }
}
