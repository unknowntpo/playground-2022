#!/bin/bash

# Set script to exit immediately if any command fails
set -e

# Function to display usage
function show_usage {
	echo "Usage: $0 [command]"
	echo "Commands:"
	echo "  build      - Build the Flink application"
	echo "  up         - Start the Flink cluster"
	echo "  down       - Stop the Flink cluster"
	echo "  deploy     - Deploy the application to the Flink cluster"
	echo "  logs       - View the logs of the Flink cluster"
	echo "  create-data - Create a sample input data file"
	echo "  view-data  - View the contents of the input data file"
	echo "  check      - Check if the application is running and see output"
	echo "  help       - Show this help message"
}

# Check if a command is provided
if [ $# -eq 0 ]; then
	show_usage
	exit 1
fi

# Process the command
case "$1" in
build)
	echo "Building the Flink application..."
	./gradlew clean shadowJar
	echo "Build completed. JAR file is located in app/build/libs/"
	;;
up)
	echo "Starting the Flink cluster..."
	# Create required directories
	mkdir -p data
	mkdir -p app/build/libs

	# Check if we built the jar
	JAR_FILE=$(find app/build/libs -name "*.jar" 2>/dev/null || true)
	if [ -z "$JAR_FILE" ]; then
		echo "No JAR file found. Building the application first..."
		./gradlew shadowJar
	fi

	docker-compose up -d
	echo "Flink cluster is running. Web UI available at http://localhost:8081"
	;;
down)
	echo "Stopping the Flink cluster..."
	docker-compose down
	echo "Flink cluster stopped."
	;;
deploy)
	echo "Deploying application to Flink cluster..."
	# Find the JAR file
	APP_JAR=$(find app/build/libs -name "*.jar" | head -n 1)
	if [ -z "$APP_JAR" ]; then
		echo "No JAR file found. Build the application first with './run-flink.sh build'"
		exit 1
	fi

	# Check if input file exists
	if [ ! -f "data/input.txt" ]; then
		echo "No input file found. Creating a sample input file..."
		mkdir -p data
		cat >data/input.txt <<EOF
Hello Apache Flink
This is a test file
It contains some text to be processed
Flink is a powerful stream processing framework
It can handle batch processing as well
This sample will be used to test the WordCount example
EOF
		echo "Sample input file created at data/input.txt"
	fi

	# Verify JAR exists in the container
	echo "Verifying JAR file in the container..."
	CONTAINER_JAR=$(docker exec -it flink-jobmanager find /opt/flink/usrlib -name "*.jar" | grep -o "flink-example.*\.jar" || true)
	if [ -z "$CONTAINER_JAR" ]; then
		echo "JAR file not found in container. Copying JAR file to container..."
		# If the JAR isn't in the container, restart the services to ensure volumes are properly mounted
		docker-compose down
		docker-compose up -d
		# Wait for services to fully start up
		echo "Waiting for Flink services to start..."
		sleep 10
	fi

	# Deploy with verbose logging enabled
	echo "Deploying JAR to Flink cluster..."
	BASENAME=$(basename "$APP_JAR")
	docker exec -it flink-jobmanager flink run \
		-Dlog.level=INFO \
		-Dlog.file.level=DEBUG \
		-Dlog.levels.org.example=DEBUG \
		-c org.example.StreamingJob /opt/flink/usrlib/"$BASENAME"

	echo "Application deployed."
	echo "Check logs with './run-flink.sh logs' to see the processing results."
	echo "Or check individual output with './run-flink.sh check'"
	;;
logs)
	echo "Showing Flink cluster logs..."
	docker-compose logs -f
	;;
taskmanager-logs)
	echo "Showing TaskManager logs..."
	docker logs flink-taskmanager -f
	;;
check)
	echo "Checking application output..."
	echo "=== TASK MANAGER LOGS (WORD COUNT) ==="
	docker logs flink-taskmanager | grep "WORD COUNT" | tail -n 20
	echo ""
	echo "=== JOB MANAGER DASHBOARD ==="
	echo "Check the Flink Web UI for more details: http://localhost:8081"
	;;
create-data)
	echo "Creating a sample input data file..."
	mkdir -p data
	cat >data/input.txt <<EOF
Hello Apache Flink
This is a test file
It contains some text to be processed
Flink is a powerful stream processing framework
It can handle batch processing as well
This sample will be used to test the WordCount example
EOF
	echo "Sample input file created at data/input.txt"
	echo "Run './run-flink.sh view-data' to see the contents"
	echo "Run './run-flink.sh deploy' to process the file"
	;;
view-data)
	echo "Contents of data/input.txt:"
	echo "----------------------------"
	if [ -f data/input.txt ]; then
		cat data/input.txt
	else
		echo "File not found. Create it with './run-flink.sh create-data'"
	fi
	echo "----------------------------"
	;;
help)
	show_usage
	;;
*)
	echo "Unknown command: $1"
	show_usage
	exit 1
	;;
esac
