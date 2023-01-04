const nsq = require('nsqjs')

const reader = new nsq.Reader('sample_topic', 'default', {
    lookupdHTTPAddresses: 'localhost:4160'
})

reader.connect()

reader.on('message', msg => {
    console.log('Received message [%s]: %s', msg.id, msg.body.toString())
    msg.finish()
})
