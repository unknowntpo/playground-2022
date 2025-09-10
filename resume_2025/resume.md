# Eric (Chen-Chien) Chang

**Contact Information:**
- Email: e850506@gmail.com
- Phone: +886-932786506
- Github: https://github.com/unknowntpo/

## DESCRIPTION
- Apache Gravitino Committer and Apache Kafka Contributor (Apache Foundation Top-Level Projects)
- Experienced in Performance Tuning (SQL, Backend Service)
- Fast Learner, can pick up numerous toolings and use it to solve real world problems
- Tech Stack: Go, Java, Typescript, Python, PostgreSQL, Kafka, Docker, Kubernetes

## EXPERIENCE

### Lawsnote | January 2024 - December 2024
**Backend Software Engineer** | Taipei, Taiwan  
*Tech Stack: Typescript, NodeJS, Docker, MongoDB, Prometheus, Grafana*

- Reduced first byte latency from 100s to 5s (20x faster) on CPU-intensive computation results with HTTP Streaming
- Built data integrity monitoring system using MinIO events and Elasticsearch to proactively identify data loss, improving system reliability
- Identified and fixed NAPI-rs package problem with Pyroscope NodeJS profiler
- Built LLM service to parse Law-related text with ambiguous rules

### Mediatek (Contract) | February 2022 - March 2023
**Backend Software Engineer** | Hsinchu, Taiwan  
*Tech Stack: Go, Linux, Docker, PostgreSQL*

- Maintained a data warehouse for Low-Power Simulation and Benchmarking
- SQL Query Optimization: Reduced page-loading time of WebUI from 60s to 1s (60x faster) with Index-Only Scan: [Blog Post]
- Used Continuous Profiling tool: Pyroscope eBPF Spy to find out performance bottleneck
- Reduced time-consuming simulation job duration from 2hr to 1 min by leveraging PostgreSQL Bulk Import feature (bottleneck discovered by Pyroscope)
- Designed message queue based microservice architecture to scale up time-consuming tasks
- Reduced 50% memory consumption by using Golang sync.Pool: [Blog Post]
- Designed a integration test fixture factory to increase robustness of our tests
- Managed 5-node distributed service with tools like Prometheus, Grafana, OpenTelemetry, Pyroscope
- Shared knowledge with team members in a study group

### National Chen-Kung University | September 2017 - July 2019
**Research Assistant** | Tainan, Taiwan  
*Tech Stack: Python, C*

- FDM 3D Printing Related Research (Klipper Firmware, 3D Printing Filament Research)
- Experienced with MicroPython, Arduino

## OPENSOURCE CONTRIBUTION

### Apache Gravitino | 2024 - Present
**Apache Gravitino Committer**  
*Tech Stack: Java, Python, Docker, Kubernetes*

- Implemented container suite management for PostgreSQL and MySQL integration tests, significantly reducing resource consumption and improving test execution speed
- Developed robust Docker image build workflows from source, reduced Hive Docker image size by 420MB, ensuring reliable and reproducible builds
- Ported partition-related interfaces from Java to Python client, enhanced CLI with multiple output formats, broadening platform accessibility
- Optimized backend storage performance through batch listing implementation in RoleMetaService, reducing SQL query overhead and improving system scalability

### Apache Kafka | 2024 - Present
**Apache Kafka Contributor**  
*Tech Stack: Java, JVM*

- Identified and fixed JVM container awareness problem in producer-performance test IntelliJ Profiler

### redis/rueidis | Oct 2023 - Present
**Contributor to fast Golang Redis client (1.9k stars)**  
*Tech Stack: Go, Redis*

- Implemented go-redis API Adapter
- [Link to pull request]

## EDUCATION

### National Taiwan Ocean University | July 2013 - April 2017 (Drop Out)
**Electrical Engineering** | Taiwan

- Acquired self-learning ability to learn anything and helped me to get my first job
- Made me an open-minded person, willing to explore any interesting things
- Made me a person who can understand the question behind the question (QBQ), and make the right decision in system design