#!/bin/bash

docker run -d --rm --name socks5-proxy -p 1080:1080 serjs/go-socks5-
proxy