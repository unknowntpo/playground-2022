import time

from fastapi import FastAPI, Body
from pydantic import BaseModel
from ray import serve
from ray.serve.handle import DeploymentHandle
from transformers import pipeline
import torch

app = FastAPI()


class BadwordBody(BaseModel):
    text: str


@serve.deployment(autoscaling_config={"min_replicas": 1, "max_replicas": 4})
@serve.ingress(app)
class OffensiveLanguageDetector:
    def __init__(self):
        self.classifier = pipeline(
            "text-classification",
            model="martin-ha/toxic-comment-model",
            device=torch.device("cuda:0") if torch.cuda.is_available() else "cpu",
        )

    @app.post("/detect")
    async def detect_offensive_content(self, body: BadwordBody):
        results = self.classifier(body.text)
        return {
            "is_offensive": any(pred["label"] == "toxic" for pred in results),
            "predictions": results,
        }


# Deploy the service
serve.run(target=OffensiveLanguageDetector.bind(), route_prefix="/")

# Modified deployment configuration
serve.start(http_options={"host": "0.0.0.0", "port": 8000})  # Explicit host binding
serve.run(target=OffensiveLanguageDetector.bind(), route_prefix="/")

# Keep the script alive
while True:
    time.sleep(1)
