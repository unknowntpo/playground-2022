#!/bin/bash
# Download Flink connector JARs if they don't exist

set -e

LIB_DIR="lib"
mkdir -p "$LIB_DIR"

echo "=================================================="
echo "Downloading Flink Connector JARs"
echo "=================================================="

# JAR versions
FLINK_VERSION="1.20"
CDC_VERSION="3.3.0"
JDBC_VERSION="3.2.0-1.19"
POSTGRES_VERSION="42.7.1"

# JAR URLs
CDC_JAR_URL="https://repo1.maven.org/maven2/org/apache/flink/flink-sql-connector-postgres-cdc/${CDC_VERSION}/flink-sql-connector-postgres-cdc-${CDC_VERSION}.jar"
JDBC_JAR_URL="https://repo1.maven.org/maven2/org/apache/flink/flink-connector-jdbc/${JDBC_VERSION}/flink-connector-jdbc-${JDBC_VERSION}.jar"
POSTGRES_JAR_URL="https://repo1.maven.org/maven2/org/postgresql/postgresql/${POSTGRES_VERSION}/postgresql-${POSTGRES_VERSION}.jar"

# Download function
download_jar() {
    local url=$1
    local filename=$(basename "$url")
    local filepath="$LIB_DIR/$filename"

    if [ -f "$filepath" ]; then
        echo "[✓] $filename already exists"
    else
        echo "[→] Downloading $filename..."
        curl -L -o "$filepath" "$url"
        echo "[✓] Downloaded $filename"
    fi
}

# Download JARs
download_jar "$CDC_JAR_URL"
download_jar "$JDBC_JAR_URL"
download_jar "$POSTGRES_JAR_URL"

echo ""
echo "=================================================="
echo "All JARs downloaded successfully!"
echo "=================================================="
echo ""
ls -lh "$LIB_DIR"
