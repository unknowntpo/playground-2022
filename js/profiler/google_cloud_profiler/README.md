
Use this command to run program:

```
$ GOOGLE_APPLICATION_CREDENTIALS="path-to-credential.json" node app.js
```

On other shell, generate workload:

```
$ sh -c 'for i in {1..40} do curl http://localhost:9090/fibonacci/41; done'
```
