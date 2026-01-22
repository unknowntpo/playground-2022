import { check, sleep } from 'k6';
import { Writer, Reader, Connection, SchemaRegistry, CODEC_SNAPPY } from 'k6/x/kafka';
import encoding from 'k6/encoding';

// Configuration
const brokers = ['kafka-1:9092', 'kafka-2:9092', 'kafka-3:9092'];
const topic = 'upgrade-test-topic';

// Initialize Kafka Writer
const writer = new Writer({
    brokers: brokers,
    topic: topic,
    autoCreateTopic: true,
});

// Initialize Kafka Reader
const reader = new Reader({
    brokers: brokers,
    groupTopics: [topic],
    groupID: 'k6-consumer-group',
});

export const options = {
    vus: 1, // Start with 1 Virtual User to easily track message flow
    duration: '1h', // Run for a long time to cover the rolling upgrade
};

export function setup() {
    // Wait for metadata propagation
    sleep(5);
    console.log('Setup complete, starting test...');
}

export default function () {
    const correlationId = `k6-${__VU}-${__ITER}`;
    const payload = JSON.stringify({
        ts: Date.now(),
        msg: 'Kafka Upgrade Test',
        id: correlationId
    });

    // 1. Produce Message
    try {
        writer.produce({
            messages: [{
                key: encoding.b64encode(correlationId),
                value: encoding.b64encode(payload),
            }],
        });
    } catch (e) {
        console.error(`[Producer Error] ${e}`);
    }

    // 2. Consume Message
    try {
        const messages = reader.consume({ limit: 1 });
        check(messages, {
            'message received': (m) => m.length > 0,
            'correct message content': (m) => {
                if (m.length === 0) return false;
                const decoded = encoding.b64decode(m[0].value, 'std', 's');
                return JSON.parse(decoded).id === correlationId;
            },
        });
    } catch (e) {
        console.error(`[Consumer Error] ${e}`);
    }

    // Small delay to prevent flooding the logs
    sleep(0.5);
}

export function teardown() {
    writer.close();
    reader.close();
}
