FROM rust:1.77.0
WORKDIR /usr/src/app

COPY src ./src
COPY kube ./kube
COPY Cargo.toml Cargo.toml

RUN cargo build --release

CMD ["./target/release/controller"]
