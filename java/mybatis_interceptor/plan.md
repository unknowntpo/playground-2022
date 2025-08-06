# MyBatis interceptor example

Create a docker compose file with MySQL,
and an application that use MyBatis, JDBC to connect
to db.

And implement a interceptor, that for every `INSERT`, `UPDATE`, `DELETE`,
create an entry in `change_event` table.

Some necessary fields:

- id
- Timestamp
- table name
- data (contains actual data)
