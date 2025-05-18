from typing import Union, Annotated

from fastapi import APIRouter, status, Depends
from pydantic import BaseModel
from sqlmodel import Session
from starlette.responses import HTMLResponse
from starlette.websockets import WebSocket, WebSocketDisconnect

from app.entities.chat_message import ChatMessageBase, ChatMessage
from app.entities.item import Item
from app.infra.engine import engine


class ChatMessageCreateRequest(ChatMessageBase):
    pass

ChatRouter: APIRouter = APIRouter(prefix="/v1/chat", tags=["chat"])

def get_session():
    with Session(engine) as session:
        yield session

SessionDep = Annotated[Session, Depends(get_session)]

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
async def websocket_endpoint(websocket: WebSocket, session: SessionDep):
    await websocket.accept()
    try:
        while True:
            msg_json = await websocket.receive_json()
            msg = ChatMessage(**msg_json)
            session.add(msg)
            session.commit()
            session.refresh(msg)
            await websocket.send_json(msg.model_dump())
    except WebSocketDisconnect:
        print("Disconnected")

