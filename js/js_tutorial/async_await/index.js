function getUser(userId, callback) {
    console.log('Get user from the database.');
    setTimeout(() => {
        callback({
            userId: userId,
            username: 'John'
        })
    }, 1000)
}

function getServices(user, callback) {
    console.log(`Get services of ${user.username} from the API.`)
    setTimeout(() => {
        callback(['Email', 'VPN', 'CDN'])
    }, 2 * 1000)
}

function getServiceCost(services, callback) {
    console.log(`Calculate service costs of ${services}`)
    setTimeout(() => {
        callback(services.length * 100)
    }, 3 * 1000)
}

getUser(100, (user) => {
    getServices(user, (services) => {
        getServiceCost(services, (cost) => {
            console.log(`The service cost is ${cost}`)
        })
    })
})
