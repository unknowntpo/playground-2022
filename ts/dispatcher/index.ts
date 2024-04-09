import { socksDispatcher } from "fetch-socks";

const dispatcher = socksDispatcher({
	type: 5,
	// host: "localhost",
	// port: 1080,
	host: "tor.lawsnote.com",
	port: 10001,
});

async function main() {
	const response = await fetch("http://example.com", {
		dispatcher: dispatcher,
	}
	);
	//   const response = await fetch(url, {
	//     // 預設的 `requestOptions`
	//     ...this.requestInit,
	//     // 代理伺服器
	//     dispatcher: this.proxyAgent ?? undefined,
	//     // 用戶端傳入的 `requestOptions`，將覆蓋掉預設 requestOptions 的相同屬性者
	//     ...requestInit,
	//   });


	console.log(response.status);
	console.log(await response.text());
}

main()

// PROXY_HOST='http://tor.lawsnote.com:8118'
// PROXY_DYNAMIC_HOST='http://tor.lawsnote.com:8118'
// # PROXY_STATIC_HOSTS='http://tor.lawsnote.com:10001,http://tor.lawsnote.com:10002,http://tor.lawsnote.com:10003'
// PROXY_STATIC_HOSTS='socks5://tor.lawsnote.com:10001,socks5://tor.lawsnote.com:10002,socks5://tor.lawsnote.com:10003'


