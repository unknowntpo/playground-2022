FROM python:3.12-slim

WORKDIR /app

ENV PYTHONDONTWRITEBYTECODE=1 \
    PYTHONUNBUFFERED=1 \
    APP_MESSAGE="Hello from the Keel demo" \
    PORT=8080

COPY main.py .

EXPOSE 8080

CMD ["python", "main.py"]
