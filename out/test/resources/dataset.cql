CREATE KEYSPACE IF NOT EXISTS education WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

CREATE TABLE IF NOT EXISTS education.users (
	id int PRIMARY KEY,
	username text,
	password text,
	email text
);

INSERT INTO users (id, username, password, email) values (1, 'user1', 'password', 'email1@mail.com');
INSERT INTO users (id, username, password, email) values (2, 'user2', 'password', 'email2@mail.com');
INSERT INTO users (id, username, password, email) values (3, 'user3', 'password', 'email3@mail.com');