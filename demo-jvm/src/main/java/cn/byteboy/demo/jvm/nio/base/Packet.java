package cn.byteboy.demo.jvm.nio.base;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author hongshaochuan
 * @date 2021/8/8
 */
public class Packet {

    private byte[] content;

    public Packet(String msg) {
        content = msg.getBytes(StandardCharsets.UTF_8);
    }

    public Packet(byte[] msg) {
        content = msg;
    }

    public ByteBuffer getBuffer() {
        return ByteBuffer.wrap(content);
    }

    public String getMsg() {
        if (content != null) {
            return new String(content, StandardCharsets.UTF_8);
        }
        return null;
    }

    public byte[] getBytesMsg() {
        return content;
    }
}
