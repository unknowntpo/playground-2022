const redis = require('redis');

const client = redis.createClient({
    url: 'redis://localhost:6379',
});

try {
    client.connect();
} catch (err) {
    console.log(err);
}

const ping_pong = async function () {
    const [ping, get, quit] = await Promise.all([
        client.ping(),
        client.get('key'),
        client.quit()
    ]); // ['PONG', null, 'OK']

    console.log(ping, get, quit);
}

ping_pong();
