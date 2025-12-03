-- Seed initial blog posts
INSERT INTO posts (title, content, created_at) VALUES
    ('Getting Started with Apache Flink', 'Apache Flink is a powerful stream processing framework...', NOW() - INTERVAL '30 days'),
    ('Introduction to Python', 'Python is a versatile programming language...', NOW() - INTERVAL '25 days'),
    ('Database CDC Patterns', 'Change Data Capture is a technique for tracking database changes...', NOW() - INTERVAL '20 days'),
    ('Real-time Analytics', 'Real-time analytics enables immediate insights from streaming data...', NOW() - INTERVAL '15 days'),
    ('Microservices Architecture', 'Microservices provide scalability and maintainability...', NOW() - INTERVAL '10 days'),
    ('Docker Best Practices', 'Containerization with Docker simplifies deployment...', NOW() - INTERVAL '9 days'),
    ('PostgreSQL Performance Tuning', 'Optimizing PostgreSQL queries for better performance...', NOW() - INTERVAL '8 days'),
    ('Event-Driven Architecture', 'Event-driven systems provide loose coupling and scalability...', NOW() - INTERVAL '7 days'),
    ('Stream Processing Concepts', 'Understanding windows, watermarks, and state in stream processing...', NOW() - INTERVAL '6 days'),
    ('Building Data Pipelines', 'Best practices for building reliable data pipelines...', NOW() - INTERVAL '5 days'),
    ('Machine Learning Basics', 'Introduction to machine learning algorithms and techniques...', NOW() - INTERVAL '4 days'),
    ('Web Development Trends', 'Latest trends in modern web development...', NOW() - INTERVAL '3 days'),
    ('Cloud Computing Overview', 'Understanding cloud platforms and services...', NOW() - INTERVAL '2 days'),
    ('API Design Principles', 'RESTful API design best practices...', NOW() - INTERVAL '1 day'),
    ('Software Testing Strategies', 'Comprehensive guide to testing methodologies...', NOW());
