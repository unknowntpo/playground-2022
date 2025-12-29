import paho.mqtt.client as mqtt


def on_connect(client, userdata, flags, reason_code, properties):
    print(f"Connected: {reason_code}", flush=True)
    result = client.subscribe("testtopic/#", qos=0)
    print(f"Subscribed: {result}", flush=True)


def on_message(client, userdata, msg):
    try:
        payload = msg.payload.decode()
    except UnicodeDecodeError:
        payload = msg.payload.hex()
    print(f"Topic: {msg.topic} | Payload: {payload}", flush=True)


def on_disconnect(client, userdata, flags, reason_code, properties):
    print(f"Disconnected: {reason_code}", flush=True)


def main():
    client = mqtt.Client(mqtt.CallbackAPIVersion.VERSION2)
    client.on_connect = on_connect
    client.on_message = on_message
    client.on_disconnect = on_disconnect
    print("Connecting to broker.emqx.io:1883...", flush=True)
    client.connect("broker.emqx.io", 1883, 60)
    client.loop_forever()


if __name__ == "__main__":
    main()
