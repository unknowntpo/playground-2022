var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var RouteRegistry = /** @class */ (function () {
    function RouteRegistry() {
    }
    RouteRegistry.getRoute = function (path) {
        return RouteRegistry.routes.get(path);
    };
    RouteRegistry.setRoute = function (path, fn) {
        RouteRegistry.routes.set(path, fn);
    };
    RouteRegistry.routes = new Map();
    return RouteRegistry;
}());
function Route(path) {
    console.log(path);
    return function (target, propertyKey, descriptor) {
        RouteRegistry.setRoute(path, target[propertyKey]);
    };
}
function Empty() {
    console.log("EMpty");
    return function (target, propertyKey) {
        console.log("EMpty(): called with key" + propertyKey);
    };
}
var Controller = /** @class */ (function () {
    function Controller() {
    }
    Controller.prototype.hello = function () {
        console.log("Controller:hello");
    };
    Controller.prototype.goodbye = function () {
        console.log("Controller:hello");
    };
    __decorate([
        Route("/hello")
    ], Controller.prototype, "hello", null);
    __decorate([
        Route("/goodbye")
    ], Controller.prototype, "goodbye", null);
    return Controller;
}());
var helloHandler = RouteRegistry.getRoute('/hello');
helloHandler();
export { RouteRegistry };
//# sourceMappingURL=index.js.map