# Jcasbin Demo

This is a demo of Jcasbin framework.
We use Jcasbin to do auth upon an API,
and the policies should be store in DB with JDBC,
and when new policies is updated, it should be save to DB,
so multiple instances can load it again.

## Arch:

```
Service <-> MySQL 
```

Connection: Mybatis, JDBC
Framework: Java.ws.rs

## Detail:

Should write tests to verify the spec is satisfied.
