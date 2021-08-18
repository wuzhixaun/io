package com.wuzx.io.bio.charset;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/**
 * @author wuzhixuan
 * @version 1.0.0
 * @ClassName ChartDemo.java
 * @Description TODO
 * @createTime 2021年08月10日 11:42:00
 */
public class ChartDemo {


    public static void main(String[] args) throws CharacterCodingException {
        Charset cs = Charset.forName("UTF-8");
        CharsetDecoder csdecoder = cs.newDecoder();
        CharsetEncoder csencoder = cs.newEncoder();
        String st = "Example of Encode and Decode in Java NIO.";
        ByteBuffer bb = ByteBuffer.wrap(st.getBytes());
        CharBuffer cb = csdecoder.decode(bb);
        ByteBuffer newbb = csencoder.encode(cb);
        while (newbb.hasRemaining()) {
            char ca = (char) newbb.get();
            System.out.print(ca);
        }
        newbb.clear();

    }
}
