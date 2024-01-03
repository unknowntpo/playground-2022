import Koa from "koa";
import Router from "koa-router";

const app = new Koa()
const router = new Router()
router.get("/", async (ctx: Koa.Context) => {
    ctx.body = "Hello Koa";
});

app.use(router.routes())
const port = 8000
console.log(`starting app at ${port}`)
app.listen(port)
