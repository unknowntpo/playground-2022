FROM python:3.12

# Install uv (faster Python package and version manager)
RUN curl -LsSf https://astral.sh/uv/install.sh | sh
ENV PATH="/root/.local/bin:/root/.cargo/bin:${PATH}"

WORKDIR /app

# Install Python 3.12 using uv (faster than apt)
RUN uv python install 3.12

COPY pyproject.toml uv.lock ./

# Install dependencies using uv sync (creates .venv automatically)
RUN uv sync --frozen --no-dev

COPY . .

# Default command
CMD ["bash"]
