# Stage 1: Builder
FROM postgres:16 AS builder

# Install build dependencies
RUN apt-get update && apt-get install -y \
    postgresql-server-dev-16 \
    build-essential \
    pkg-config \
    libssl-dev \
    git \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Install Rust
ENV RUSTUP_HOME=/usr/local/rustup \
    CARGO_HOME=/usr/local/cargo \
    PATH=/usr/local/cargo/bin:$PATH
RUN curl --proto '=https' --tlsv1.2 -sSf https://sh.rustup.rs | sh -s -- -y

# Set environment variables for pgrx
ENV CARGO_PGRX_VERSION=0.16.1
ENV PG_MAJOR=16

# Install cargo-pgrx
RUN cargo install --locked cargo-pgrx --version ${CARGO_PGRX_VERSION}

# Initialize pgrx using the installed postgres configuration
RUN cargo pgrx init --pg${PG_MAJOR} $(which pg_config)

# Clone and install pg_parquet
WORKDIR /tmp
RUN git clone https://github.com/CrunchyData/pg_parquet.git
WORKDIR /tmp/pg_parquet

# Build and install the extension into the builder's postgres directories
RUN cargo pgrx install --release --features pg${PG_MAJOR}

# Stage 2: Runtime
FROM postgres:16

# Install runtime dependencies
# libssl and ca-certificates are often required for Parquet S3/network functionality
RUN apt-get update && apt-get install -y \
    libssl3 \
    ca-certificates \
    && rm -rf /var/lib/apt/lists/*

# Copy artifacts from builder
# The extension shared object
COPY --from=builder /usr/lib/postgresql/16/lib/pg_parquet.so /usr/lib/postgresql/16/lib/
# The extension control file and SQL scripts
COPY --from=builder /usr/share/postgresql/16/extension/pg_parquet* /usr/share/postgresql/16/extension/

# Configure postgres to load the extension by default
RUN echo "shared_preload_libraries = 'pg_parquet'" >> /usr/share/postgresql/16/postgresql.conf.sample