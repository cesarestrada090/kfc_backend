This Application have the required use cases.

What this app have?

Spring JPA (Insert Record into db after some business logic)
Aspects (To know information about the request)

2 Types of Authentication ( Basic Authentication and Mocked Oauth)

Credentials:

For Basic Auth:
user: admin
pass: admin

For Mocked Oath
Bearer token: b8920c1b-9f3d-4054-bef6-1da40ca8288e

2 TEST HAS BEEN CREATED FOR BUSINESS LOGIC VALIDATION:

testCostLowerThanBalance
testCostGreaterThanBalance


SPRING BOOT ACTUATOR AT PORT 8082



BD Script:



CREATE TABLE calculator.`user` (
id INT auto_increment NOT NULL,
user_name varchar(100) NULL,
password varchar(100) NULL,
status TINYINT NULL,
CONSTRAINT contacts_pk PRIMARY KEY (id)
)

CREATE TABLE calculator.`operation` (
id INT auto_increment NOT NULL,
type varchar(100) NULL,
CONSTRAINT contacts_pk PRIMARY KEY (id)
)

CREATE TABLE record (
id INT PRIMARY KEY auto_increment,
amount VARCHAR(100) NOT NULL,
date_created datetime NOT NULL,
operation_response INT,
user_balance Decimal(10,2),
operation_id INT,
user_id INT,
FOREIGN KEY (operation_id) REFERENCES operation(id),
FOREIGN KEY (user_id) REFERENCES user(id)
);

insert into operation (type) values ('addition');
insert into operation (type) values ('subtraction');
insert into operation (type) values ('multiplication');
insert into operation (type) values ('division');
insert into operation (type) values ('square_root');
insert into operation (type) values ('random_string');

insert into `user` (user_name,password,status) values('testUser','123',1);
insert into user_balance (user_id,balance) values (1,100);