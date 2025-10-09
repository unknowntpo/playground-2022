

Meter

- run() 
    - with runnable method with any arg and return value 
    - when run() is called 

@Meter
public int func() {
    ...
}

for 10000 times:
    obj.func()

// req per sec
ThroughputMeter.getThroughput()
