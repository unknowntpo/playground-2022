CREATE TABLE accounts (
	id INT AUTO_INCREMENT PRIMARY KEY,
	owner TEXT,
	balance INT,
	currency TEXT,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
INSERT INTO accounts (owner, balance, currency)
VALUES
('one', 100, 'USD'),
('two', 100, 'USD'),
('three', 100, 'USD');
