package cn.byteboy.demo.jvm.mqtt;

import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class DemoClient {

    String topic        = "MQTT Examples";
    String content      = "Message from MqttPublishSample";
    int qos             = 2;
    String broker       = "tcp://iot.eclipse.org:1883";
    String clientId     = "JavaSample";
    MemoryPersistence persistence = new MemoryPersistence();
    public static void main(String[] args) {

    }
}
