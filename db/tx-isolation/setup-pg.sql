CREATE TABLE accounts (
	id SERIAL PRIMARY KEY,
	owner TEXT,
	balance INT,
	currency TEXT,
	created_at timestamp(0) with time zone NOT NULL DEFAULT NOW()
);
INSERT INTO accounts (owner, balance, currency)
VALUES
('one', 100, 'USD'),
('two', 100, 'USD'),
('three', 100, 'USD');
