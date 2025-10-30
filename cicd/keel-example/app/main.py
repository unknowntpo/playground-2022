#!/usr/bin/env python3
"""Minimal HTTP server that surfaces image metadata for Keel demo."""

from http.server import BaseHTTPRequestHandler, HTTPServer
import json
import os
import socket
from typing import Dict, Any


class HealthHandler(BaseHTTPRequestHandler):
    server_version = "KeelDemoHTTP/0.1"

    def _collect_payload(self) -> Dict[str, Any]:
        return {
            "message": os.environ.get("APP_MESSAGE", "Hello from the Keel demo"),
            "image_tag": os.environ.get("IMAGE_TAG", "unknown"),
            "hostname": socket.gethostname(),
        }

    def _write_json(self, payload: Dict[str, Any], status: int = 200) -> None:
        data = json.dumps(payload).encode("utf-8")
        self.send_response(status)
        self.send_header("Content-Type", "application/json")
        self.send_header("Content-Length", str(len(data)))
        self.end_headers()
        self.wfile.write(data)

    def do_GET(self) -> None:  # noqa: N802 (BaseHTTPRequestHandler signature)
        payload = self._collect_payload()
        payload["path"] = self.path
        self._write_json(payload)

    def log_message(self, fmt: str, *args: Any) -> None:  # noqa: D401
        """Silence default logging to keep container logs clean."""
        return


def main() -> None:
    port = int(os.environ.get("PORT", "8080"))
    server = HTTPServer(("", port), HealthHandler)
    server.serve_forever()


# test update
# test update 4:46

if __name__ == "__main__":
    main()
