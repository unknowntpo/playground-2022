import time
import paho.mqtt.client as mqtt


def main():
    client = mqtt.Client(mqtt.CallbackAPIVersion.VERSION2)
    client.connect("broker.emqx.io", 1883, 60)
    client.loop_start()

    topic = "testtopic/emqx_demo"
    for i in range(10):
        msg = f"Hello EMQX #{i}"
        client.publish(topic, msg)
        print(f"Published: {topic} -> {msg}")
        time.sleep(1)

    client.loop_stop()
    client.disconnect()


if __name__ == "__main__":
    main()
