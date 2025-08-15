-- Create database schema for User-Role many-to-many relationship

-- Users table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Roles table
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- User-Role junction table (many-to-many)
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Insert sample data

-- Insert roles
INSERT INTO roles (name, description) VALUES
('ADMIN', 'Administrator with full system access'),
('USER', 'Regular user with standard permissions'),
('MODERATOR', 'Moderator with content management permissions'),
('VIEWER', 'Read-only access to the system');

-- Insert users
INSERT INTO users (name, email) VALUES
('John Doe', 'john.doe@example.com'),
('Jane Smith', 'jane.smith@example.com'),
('Mike Johnson', 'mike.johnson@example.com'),
('Sarah Wilson', 'sarah.wilson@example.com'),
('Tom Brown', 'tom.brown@example.com'),
('Lisa Davis', 'lisa.davis@example.com');

-- Assign users to roles (creating many-to-many relationships)
-- Admin role (id=1) has multiple users
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1), -- John Doe -> ADMIN
(2, 1), -- Jane Smith -> ADMIN
(3, 1); -- Mike Johnson -> ADMIN

-- User role (id=2) has multiple users
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 2), -- John Doe -> USER (user can have multiple roles)
(4, 2), -- Sarah Wilson -> USER
(5, 2), -- Tom Brown -> USER
(6, 2); -- Lisa Davis -> USER

-- Moderator role (id=3) has multiple users
INSERT INTO user_roles (user_id, role_id) VALUES
(2, 3), -- Jane Smith -> MODERATOR
(3, 3); -- Mike Johnson -> MODERATOR

-- Viewer role (id=4) has multiple users
INSERT INTO user_roles (user_id, role_id) VALUES
(4, 4), -- Sarah Wilson -> VIEWER
(5, 4), -- Tom Brown -> VIEWER
(6, 4); -- Lisa Davis -> VIEWER