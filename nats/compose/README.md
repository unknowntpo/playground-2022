Create stream:

```
nats stream add mystream \
  --subjects "events.*" \
  --storage file \
  --retention limits \
  --max-msgs 1000 \
  --max-bytes 1G \
  --max-age 24
```


Use this command to generate pub sub:

```
nats bench \
    --js \
    --multisubject \
    --pub 3 --sub 10 \
    --msgs 200000 \
    --syncpub \
    --no-progress \
    --stream "mystream" \
    "events"
```
