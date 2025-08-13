-- Initialize jCasbin demo database
CREATE DATABASE IF NOT EXISTS jcasbin_demo;
USE jcasbin_demo;

-- Grant permissions to jcasbin user
GRANT ALL PRIVILEGES ON jcasbin_demo.* TO 'jcasbin'@'%';
FLUSH PRIVILEGES;

-- The casbin_rule table will be created automatically by jCasbin JDBC adapter