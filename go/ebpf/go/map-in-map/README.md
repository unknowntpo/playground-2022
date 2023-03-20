
Build the example

```
$ GOARCH=amd64 GOOS=linux go build .
```

Copy binary to lima virtual machine

```
$ limactl  copy ./map-in-map default:~/map-in-map
```

in lima:

```
$ sudo ./map-in-map
```
