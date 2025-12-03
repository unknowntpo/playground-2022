# Dockerfile for PyFlink CDC Application
FROM flink:1.20-scala_2.12-java11

# Install JDK and build tools (for pemja compilation)
RUN apt-get update -y && \
    apt-get install -y openjdk-11-jdk curl build-essential && \
    rm -rf /var/lib/apt/lists/*

# Set JAVA_HOME (multi-arch support using Docker's TARGETARCH)
ARG TARGETARCH
ENV JAVA_HOME="/usr/lib/jvm/java-11-openjdk-${TARGETARCH}/"
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# Verify JAVA_HOME setup
RUN echo "Architecture: ${TARGETARCH}" && \
    echo "JAVA_HOME: ${JAVA_HOME}" && \
    ls -la ${JAVA_HOME}/include

# Install uv (faster Python package and version manager)
RUN curl -LsSf https://astral.sh/uv/install.sh | sh
ENV PATH="/root/.local/bin:/root/.cargo/bin:${PATH}"

# Install Python 3.11 using uv (faster than apt)
RUN uv python install 3.11

# Set working directory
WORKDIR /opt/flink

# Copy dependency files for layer caching
COPY pyproject.toml uv.lock ./

# Install dependencies using uv sync (creates .venv automatically)
RUN uv sync --frozen --no-dev

# Set PATH to use venv
ENV PATH="/opt/flink/.venv/bin:${PATH}"
ENV VIRTUAL_ENV="/opt/flink/.venv"

# Copy PyFlink job script
COPY flink_cdc_simple.py /opt/flink/job/flink_cdc_simple.py

# Copy Flink connector JARs to location expected by script
# Note: JARs are downloaded by download-jars.sh (not in git)
COPY lib/*.jar /opt/flink/job/lib/

# Copy entrypoint script
COPY entrypoint.sh /opt/flink/entrypoint.sh
RUN chmod +x /opt/flink/entrypoint.sh

WORKDIR /opt/flink

# Use custom entrypoint
ENTRYPOINT ["/opt/flink/entrypoint.sh"]
