CREATE TABLE user (
  _id BIGINT PRIMARY KEY AUTO_INCREMENT ,
  name VARCHAR(255) NOT NULL,
  surname VARCHAR(255) NOT NULL,
  birthday datetime NOT NULL,
  address VARCHAR(255) NOT NULL,
  mail VARCHAR(255) NOT NULL UNIQUE ,
  phone INT(9) NOT NULL,
  password VARCHAR(255) NOT NULL
)CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE production(
  _id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL,
  year INTEGER NOT NULL,
  rated VARCHAR(255) NOT NULL,
  released VARCHAR(255) NOT NULL,
  runtime VARCHAR(255) NOT NULL,
  genre VARCHAR(255) NOT NULL,
  director VARCHAR(255) NOT NULL,
  writer TEXT NOT NULL,
  actors VARCHAR(255) NOT NULL,
  plot TEXT NOT NULL,
  language VARCHAR(255) NOT NULL,
  type VARCHAR(255) NOT NULL,
  image VARCHAR(255) NOT NULL
)CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE comment(
  _id BIGINT PRIMARY KEY AUTO_INCREMENT,
  idProd BIGINT NOT NULL,
  idUser BIGINT NOT NULL,
  text TEXT NOT NULL,
  creation_date datetime NOT NULL,
  modified_date datetime NOT NULL,
  CONSTRAINT userCommentPK FOREIGN KEY(idUser) REFERENCES user(_id),
  CONSTRAINT prodCommentPK FOREIGN KEY(idProd) REFERENCES production(_id)
)CHARACTER SET utf8 COLLATE utf8_general_ci;

INSERT INTO user
(name,surname,birthday,address,mail,phone,password)
VALUES
  ("/a/non","Anonymous",NOW(),"UNKNOWN","anon@not.needed",666666666,PASSWORD("algoquenuncarecordare123456789"))