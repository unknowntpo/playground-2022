from typing import Union

from fastapi import APIRouter, status
from pydantic import BaseModel
from starlette.responses import HTMLResponse
from starlette.websockets import WebSocket, WebSocketDisconnect

from app.entities.item import Item


class ItemResponse(BaseModel):
    item_id: int
    q: Union[str, None] = None


class CreateItemResponse(BaseModel):
    item_name: str
    item_id: int


ChatRouter: APIRouter = APIRouter(prefix="/v1/chat", tags=["chat"])

"""
Reference:
https://fastapi.tiangolo.com/advanced/websockets/#in-production
"""

html = """
<!DOCTYPE html>
<html>
    <head>
        <title>Chat</title>
    </head>
    <body>
        <h1>WebSocket Chat</h1>
        <form action="" onsubmit="sendMessage(event)">
            <input type="text" id="messageText" autocomplete="off"/>
            <button>Send</button>
        </form>
        <ul id='messages'>
        </ul>
        <script>
            var ws = new WebSocket("ws://localhost:8000/v1/chat/ws");
            ws.onmessage = function(event) {
                var messages = document.getElementById('messages')
                var message = document.createElement('li')
                var content = document.createTextNode(event.data)
                message.appendChild(content)
                messages.appendChild(message)
            };
            function sendMessage(event) {
                var input = document.getElementById("messageText")
                ws.send(input.value)
                input.value = ''
                event.preventDefault()
            }
        </script>
    </body>
</html>
"""


@ChatRouter.get("/")
async def get():
    return HTMLResponse(html)


@ChatRouter.websocket("/ws")
async def websocket_endpoint(websocket: WebSocket):
    await websocket.accept()
    try:
        while True:
            data = await websocket.receive_text()
            await websocket.send_text(f"Message text was: {data}")
    except WebSocketDisconnect:
        print("Disconnected")

