package cn.byteboy.demo.jvm.nio.base;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author hongshaochuan
 * @date 2021/8/8
 */
public class Packet {

    private byte[] content;

    private ByteBuffer bb;

    public Packet(String msg) {
        content = msg.getBytes(StandardCharsets.UTF_8);
    }

    public Packet(byte[] msg) {
        content = msg;
    }

    public ByteBuffer getBuffer() {
        if (this.bb == null)
            createBB();
        return this.bb;
    }

    private synchronized void createBB() {
        this.bb = ByteBuffer.allocate(content.length + 4);
        this.bb.putInt(content.length); // len
        this.bb.put(content);
        this.bb.rewind();
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
