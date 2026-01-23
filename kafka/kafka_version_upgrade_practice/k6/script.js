import { check, sleep } from 'k6';
import { Writer, Reader, Connection, SchemaRegistry, SCHEMA_TYPE_STRING } from 'k6/x/kafka';

// Configuration
const brokers = ['kafka-1:9092', 'kafka-2:9092', 'kafka-3:9092'];
const topic = 'upgrade-test-topic';

// Initialize Kafka Writer with STRING serialization
const writer = new Writer({
    brokers: brokers,
    topic: topic,
    autoCreateTopic: true,
});

// Initialize Kafka Reader with STRING deserialization
const reader = new Reader({
    brokers: brokers,
    groupTopics: [topic],
    groupID: 'k6-consumer-group',
});

const schemaRegistry = new SchemaRegistry();

export const options = {
    scenarios: {
        producer: {
            executor: 'constant-vus',
            vus: 1,
            duration: '1h',
            exec: 'producer',
        },
        consumer: {
            executor: 'constant-vus',
            vus: 1,
            duration: '1h',
            exec: 'consumer',
        },
    },
};

export function setup() {
    // Wait for metadata propagation
    sleep(5);
    console.log('Setup complete, starting test...');
}

// Producer scenario
export function producer() {
    const correlationId = `k6-${__VU}-${__ITER}`;
    const payload = JSON.stringify({
        ts: Date.now(),
        msg: 'Kafka Upgrade Test',
        id: correlationId
    });

    try {
        writer.produce({
            messages: [{
                key: schemaRegistry.serialize({ data: correlationId, schemaType: SCHEMA_TYPE_STRING }),
                value: schemaRegistry.serialize({ data: payload, schemaType: SCHEMA_TYPE_STRING }),
            }],
        });
    } catch (e) {
        console.error(`[Producer Error] ${e}`);
    }

    sleep(0.5);
}

// Consumer scenario
export function consumer() {
    try {
        const messages = reader.consume({ limit: 10 });
        check(messages, {
            'messages received': (m) => m.length > 0,
            'messages have valid content': (m) => {
                if (m.length === 0) return false;
                for (const msg of m) {
                    try {
                        const value = schemaRegistry.deserialize({ data: msg.value, schemaType: SCHEMA_TYPE_STRING });
                        const parsed = JSON.parse(value);
                        if (parsed.id === undefined) return false;
                    } catch {
                        return false;
                    }
                }
                return true;
            },
        });
    } catch (e) {
        console.error(`[Consumer Error] ${e}`);
    }

    sleep(0.1);
}

export function teardown() {
    writer.close();
    reader.close();
}
