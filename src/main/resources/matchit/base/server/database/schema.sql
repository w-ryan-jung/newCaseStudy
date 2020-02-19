-- This is the schema file that the database is initialized with. It is specific to the H2 SQL dialect.
-- Author: Rasmus Ros, rasmus.ros@cs.lth.se


-- User roles describe what each user can do on a generic level.
CREATE TABLE user_role(role_id TINYINT,
                       role VARCHAR(10) NOT NULL UNIQUE,
                       PRIMARY KEY (role_id));

CREATE TABLE user(user_id INT AUTO_INCREMENT NOT NULL,
                  role_id TINYINT NOT NULL,
                  username VARCHAR_IGNORECASE NOT NULL UNIQUE, -- username should be unique
                  salt BIGINT NOT NULL,
                  password_hash UUID NOT NULL,
                  PRIMARY KEY (user_id),
                  FOREIGN KEY (role_id) REFERENCES user_role (role_id),
                  CHECK (LENGTH(username) >= 4)); -- ensures that username have 4 or more characters

-- Sessions are indexed by large random numbers instead of a sequence of integers, because they could otherwise
-- be guessed by a malicious user.
CREATE TABLE session(session_uuid UUID DEFAULT RANDOM_UUID(),
                     user_id INT NOT NULL,
                     last_seen TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
                     PRIMARY KEY(session_uuid),
                     FOREIGN KEY(user_id) REFERENCES user(user_id) ON DELETE CASCADE);

INSERT INTO user_role VALUES (1, 'ADMIN'), (2, 'USER');
INSERT INTO user (role_id, username, salt, password_hash)
    VALUES (1, 'Admin', -2883142073796788660, '8dc0e2ab-4bf1-7671-c0c4-d22ffb55ee59'),
           (2, 'Test', 5336889820313124494, '144141f3-c868-85e8-0243-805ca28cdabd');
-- Example table containing some data per user, you are expected to remove this table in your project.

CREATE TABLE product(
    product_id INT AUTO_INCREMENT NOT NULL,
    product_name VARCHAR(20) NOT NULL ,
    price INT NOT NULL,
    PRIMARY KEY(product_name)
);

CREATE TABLE location(
    location_id INT AUTO_INCREMENT NOT NULL,
    location_name VARCHAR(20) NOT NULL ,
    PRIMARY KEY(location_name)
);

-- INSERT INTO product(product_name, price) VALUES
-- ('jTelefon',8900),
-- ('jPlatta',5700),
-- ('Päronklocka',11000);
--
-- INSERT INTO location(location_name) VALUES
-- ('Cupertino'),
-- ('Norrköping'),
-- ('Frankurt');

CREATE TABLE stock(
    stock_id INT AUTO_INCREMENT,
    user_id INT NOT NULL,
    location_name VARCHAR(20) NOT NULL,
    product_name VARCHAR (20) NOT NULL,
    amount INT NOT NULL,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,

    PRIMARY KEY(stock_id),
    FOREIGN KEY(user_id) REFERENCES user(user_id) ON DELETE CASCADE,
    FOREIGN KEY(location_name) REFERENCES location(location_name),
    FOREIGN KEY(product_name) REFERENCES product(product_name)
);

-- INSERT INTO stock(user_id, location_name, product_name, amount, date) VALUES
-- (1,'Cupertino','jTelefon',1000,1581794651104),
-- (1,'Cupertino','jPlatta',1000,1581794651104),
-- (1,'Cupertino','Päronklocka',1000,1581794651104),
-- (1,'Norrköping','jTelefon',1000,1581794651104),
-- (1,'Norrköping','jPlatta',1000,1581794651104),
-- (1,'Norrköping','Päronklocka',1000,1581794651104),
-- (1,'Frankurt','jTelefon',1000,1581794651104),
-- (1,'Frankurt','jPlatta',1000,1581794651104),
-- (1,'Frankurt','Päronklocka',1000,1581794651104);







