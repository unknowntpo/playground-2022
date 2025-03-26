# Flink Docker Environment

This repository contains a Docker Compose setup for Apache Flink, including JobManager, TaskManager, and Web UI.

## Prerequisites

- Docker and Docker Compose installed on your machine
- Java 17 (for building Flink applications)
- Gradle (included as a wrapper)

## Getting Started

### 1. Build your Flink application

```shell
./run-flink.sh build
```

This will build your Flink application and place the JAR files in the `app/build/libs` directory, which is mounted into the Flink containers.

### 2. Start the Flink cluster

```shell
./run-flink.sh up
```

This will start:
- Flink JobManager
- Flink TaskManager
- Netcat service (to provide data source)
- The Flink Web UI will be available at http://localhost:8081

### 3. Submit a job

You can submit a Flink job using the Web UI or the Flink CLI inside the JobManager container:

```shell
./run-flink.sh deploy
```

This will deploy the example StreamingJob to the Flink cluster.

### 4. Send data to the job

For the Word Count example, send words to the netcat service:

```shell
./run-flink.sh send "hello world flink streaming"
```

You can send as many messages as you want using the send command. Each message will be processed by the Flink job.

### 5. Monitor your jobs

Access the Flink Web UI at http://localhost:8081 to monitor your running jobs, see task manager status, and check job metrics.

### 6. Shut down the cluster

```shell
./run-flink.sh down
```

## Configuration

You can adjust the Docker Compose file to:
- Change the Flink version (currently using Flink 1.18.0 with Java 17)
- Add more TaskManagers (modify the `scale` parameter)
- Mount additional volumes
- Modify the Flink configuration by editing the environment variables

## Java Version Compatibility

This project is configured to use Java 17. The Docker images used are `apache/flink:1.18.0-java17` to ensure compatibility with the compiled code. If you encounter Java version mismatch errors like:

```
Caused by: java.lang.UnsupportedClassVersionError: org/example/StreamingJob has been compiled by a more recent version of the Java Runtime
```

Make sure:
1. You're using the Java 17 compatible Flink Docker images (with `-java17` suffix)
2. Your application is compiled with Java 17 (configured in build.gradle.kts)

## Troubleshooting

If you encounter issues:

1. **Java Version Issues**:
   - Check that your Docker Compose file uses Flink images with Java 17 support
   - Verify your Gradle configuration is using Java 17

2. **Network Issues**:
   - The example now uses a dedicated netcat service container within the same Docker network
   - This solves connectivity issues between the Flink job and the data source

3. **Connection Refused Errors**:
   - If you see "Connection refused" errors, make sure the netcat service is running
   - Check the logs with `./run-flink.sh logs` to see if all services started properly

4. **Build Issues**:
   - Make sure all dependencies are correctly defined in build.gradle.kts
   - Use the ShadowJar plugin to create a proper fat JAR

5. **General Debugging**:
   - Check the container logs: `./run-flink.sh logs`
   - Ensure your network settings allow communication between containers
   - Verify that your JAR file is correctly mounted and accessible to the Flink containers 