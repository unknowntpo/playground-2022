FROM apache/flink:1.17-scala_2.12-java11
# FROM python:3.9-slim-bookworm

# ADD https://astral.sh/uv/0.6.10/install.sh /uv-installer.sh
# COPY --from=ghcr.io/astral-sh/uv:latest /uv /uvx /bin/

# # Install python3 and uv
RUN apt-get update -y && \
	apt-get install -y python3 python3-pip python3-dev python3-venv curl && \
	rm -rf /var/lib/apt/lists/*

# RUN ln -s /usr/bin/python3 /usr/bin/python
RUN python3 -m venv /opt/flink/venv

## FIXME: remember to refresh requirement.txt everytime.

COPY requirement.txt .

RUN /opt/flink/venv/bin/pip install -r requirement.txt

# let flink cluster use python interpreter at venv

ENV PATH="/opt/flink/venv/bin:$PATH"
# RUN ln -s /opt/flink/venv/bin/python /usr/bin/python

ENTRYPOINT ["/docker-entrypoint.sh"]

# CMD ["help"]
CMD ["tail", "-f", "/dev/null"]