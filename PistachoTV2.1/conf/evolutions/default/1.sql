# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table `COMMENT` (`_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,`idProd` BIGINT NOT NULL,`idUser` BIGINT NOT NULL,`title` VARCHAR(254) NOT NULL,`text` text NOT NULL,`creation_date` DATE NOT NULL,`modified_date` DATE NOT NULL);
create table `PRODUCTION` (`_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,`title` VARCHAR(254) NOT NULL,`year` INTEGER NOT NULL,`rated` VARCHAR(254) NOT NULL,`released` VARCHAR(254) NOT NULL,`runtime` VARCHAR(254) NOT NULL,`genre` VARCHAR(254) NOT NULL,`director` VARCHAR(254) NOT NULL,`writer` text NOT NULL,`actors` VARCHAR(254) NOT NULL,`plot` text NOT NULL,`language` VARCHAR(254) NOT NULL,`typeProd` VARCHAR(254) NOT NULL,`image` VARCHAR(254) NOT NULL);
create table `USER` (`_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,`name` VARCHAR(254) NOT NULL,`surname` VARCHAR(254) NOT NULL,`birthday` VARCHAR(254) NOT NULL,`address` VARCHAR(254) NOT NULL,`email` VARCHAR(254) NOT NULL,`phone` INTEGER NOT NULL,`password` VARCHAR(254) NOT NULL);
create unique index `U_email` on `USER` (`email`);
alter table `COMMENT` add constraint `FK_Prod` foreign key(`idProd`) references `PRODUCTION`(`_id`) on update NO ACTION on delete NO ACTION;
alter table `COMMENT` add constraint `FK_User` foreign key(`idUser`) references `USER`(`_id`) on update NO ACTION on delete NO ACTION;

# --- !Downs

ALTER TABLE COMMENT DROP FOREIGN KEY FK_Prod;
ALTER TABLE COMMENT DROP FOREIGN KEY FK_User;
drop table `USER`;
drop table `PRODUCTION`;
drop table `COMMENT`;

