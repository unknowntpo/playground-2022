import { HttpApiBuilder, HttpApiSwagger, HttpMiddleware, HttpServer } from "@effect/platform"
import { NodeHttpServer } from "@effect/platform-node"
import { Layer } from "effect"
import { createServer } from "http"
import { HttpAccountsLive } from "./Accounts/Http.js"
import { Api } from "./Api.js"
import { HttpGroupsLive } from "./Groups/Http.js"
import { HttpPeopleLive } from "./People/Http.js"

const ApiLive = HttpApiBuilder.api(Api).pipe(
  Layer.provide(HttpAccountsLive),
  Layer.provide(HttpGroupsLive),
  Layer.provide(HttpPeopleLive)
)

export const HttpLive = HttpApiBuilder.serve(HttpMiddleware.logger).pipe(
  Layer.provide(HttpApiSwagger.layer()),
  Layer.provide(HttpApiBuilder.middlewareCors()),
  Layer.provide(ApiLive),
  HttpServer.withLogAddress,
  Layer.provide(NodeHttpServer.layer(createServer, { port: 3000 }))
)