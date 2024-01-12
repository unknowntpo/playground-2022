class RouteRegistry {
    private static routes: Map<string, Function> = new Map()
    public static getRoute(path: string): Function | undefined {
        return RouteRegistry.routes.get(path)
    }
    public static setRoute(path: string, fn: Function): void {
        RouteRegistry.routes.set(path, fn)
    }
}

function Route(path: string) {
    console.log(path)
    return function (target: any, propertyKey: string, descriptor?: TypedPropertyDescriptor<() => void>) {
        RouteRegistry.setRoute(path, target[propertyKey])
    }
}


function Empty() {
    console.log("EMpty")
    return function (target: any, propertyKey: string) {
        console.log("EMpty(): called with key" + propertyKey);
    }
}

class Controller {
    @Route("/hello")
    hello() {
        console.log("Controller:hello")
    }
    @Route("/goodbye")
    goodbye() {
        console.log("Controller:hello")
    }
}


const helloHandler = RouteRegistry.getRoute('/hello')
helloHandler!()

export { RouteRegistry };