DROP DATABASE ratwsserver;
CREATE DATABASE ratwsserver;
-- CREATE USER 'admin'@'localhost' IDENTIFIED BY 'admin';
GRANT ALL PRIVILEGES ON ratwsserver.* TO 'admin'@'localhost';
FLUSH PRIVILEGES;
-- mysql -u root ratwsserver < /home/dgr/dev/RATPlatform/rat-wsserver/bin/ratwsserver.sql
USE ratwsserver;
-- ------------------------------------------
SET FOREIGN_KEY_CHECKS = 0; 

-- ----------------------------
-- permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions` (
	`permissionID` int(11) NOT NULL AUTO_INCREMENT,
	`roleName` varchar(255) NOT NULL,
	`domainUUID` varchar(255) NOT NULL,
	`permissionName` varchar(255) NOT NULL,
	PRIMARY KEY (`permissionID`)
) 
ENGINE=InnoDB 
DEFAULT CHARACTER SET utf8
DEFAULT COLLATE utf8_general_ci;

-- INSERT INTO `permissions` VALUES ('1', 'administrator', 'RAT', 'admin');
-- INSERT INTO `permissions` VALUES ('2', 'administrator', 'RAT', 'createcollaborationdomain');
-- INSERT INTO `permissions` VALUES ('3', 'administrator', 'RAT', 'createnewuser');
-- INSERT INTO `permissions` VALUES ('4', 'domainadmin', 'TestDomain', 'createnewuser');
-- INSERT INTO `permissions` VALUES ('5', 'domainadmin', 'TestDomain', 'comment');
-- INSERT INTO `permissions` VALUES ('6', 'domainadmin', 'TestDomain', 'createcollaborationdomain');
-- INSERT INTO `permissions` VALUES ('7', 'domainadmin', 'TestDomain', 'deletecollaborationdomain');
-- INSERT INTO `permissions` VALUES ('8', 'domainadmin', 'TestDomain', 'deleteuser');
-- INSERT INTO `permissions` VALUES ('9', 'domainadmin', 'TestDomain', 'deletecomment');
-- INSERT INTO `permissions` VALUES ('10', 'domainadmin', 'TestDomain', 'executeusercommands');
-- INSERT INTO `permissions` VALUES ('11', 'domainadmin', 'TestDomain', 'choosedomain');
-- ----------------------------
-- user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
	`userID` int(11) NOT NULL AUTO_INCREMENT,
	`userName` varchar(255) NOT NULL,
	`email` varchar(255) NOT NULL,
	`password` varchar(255) NOT NULL,
	`userUUID` varchar(255) NOT NULL,
	PRIMARY KEY (`userID`)
)
ENGINE=InnoDB 
DEFAULT CHARACTER SET utf8
DEFAULT COLLATE utf8_general_ci;

-- INSERT INTO `user` VALUES ('1', 'admin', 'admin@admin.it', 'admin');
-- INSERT INTO `user` VALUES ('2', 'dgr', 'dgr@dgr.it', 'dgr');

-- ----------------------------
-- role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
	`roleID` int(11) NOT NULL AUTO_INCREMENT,
	`roleName` varchar(255) NOT NULL,
	PRIMARY KEY (`roleID`)
) 
ENGINE=InnoDB 
DEFAULT CHARACTER SET utf8
DEFAULT COLLATE utf8_general_ci;

-- INSERT INTO `role` VALUES ('1', 'administrator');
-- INSERT INTO `role` VALUES ('2', 'domainadmin');

-- ----------------------------
-- user_role FORSE NON SERVE
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
	`userRoleID` int(11) NOT NULL AUTO_INCREMENT,
	`userUUID` varchar(255) NOT NULL,
	`roleName` varchar(255) NOT NULL,
	
	PRIMARY KEY (`userRoleID`)
)
ENGINE=InnoDB 
DEFAULT CHARACTER SET utf8
DEFAULT COLLATE utf8_general_ci;

-- INSERT INTO `user_role` VALUES ('1', 'admin', 'administrator');
-- INSERT INTO `user_role` VALUES ('2', 'dgr', 'domainadmin');

-- ----------------------------
-- user_domain
-- ----------------------------
DROP TABLE IF EXISTS `user_domain`;
CREATE TABLE `user_domain` (
	`userDomainID` int(11) NOT NULL AUTO_INCREMENT,
	`userUUID` varchar(255) NOT NULL,
	`domainUUID` varchar(255) NOT NULL,
	`domainName` varchar(255) NOT NULL,

	primary key (userDomainID)
)
ENGINE=InnoDB 
DEFAULT CHARACTER SET utf8
DEFAULT COLLATE utf8_general_ci;

-- INSERT INTO `user_domain` VALUES ('1', 'admin', 'RAT');
-- INSERT INTO `user_domain` VALUES ('2', 'dgr', 'TestDomain');
-- INSERT INTO `user_domain` VALUES ('3', 'dgr', 'TestDomain2');

-- ----------------------------
-- domain
-- ----------------------------
DROP TABLE IF EXISTS `domain`;
CREATE TABLE `domain` (
	`domainID` int(11) NOT NULL AUTO_INCREMENT,
	`domainName` varchar(255) NOT NULL,
	`domainUUID` varchar(255) NOT NULL,
	PRIMARY KEY (`domainID`)
)
ENGINE=InnoDB 
DEFAULT CHARACTER SET utf8
DEFAULT COLLATE utf8_general_ci;

-- INSERT INTO `domain` VALUES ('1', 'RAT');
-- INSERT INTO `domain` VALUES ('2', 'TestDomain');

-- ----------------------------
-- domain_role
-- ----------------------------
DROP TABLE IF EXISTS `domain_role`;
CREATE TABLE `domain_role` (
	`domainRoleID` int(11) NOT NULL NULL AUTO_INCREMENT,
	`domainUUID` varchar(255) NOT NULL,
	`userUUID` varchar(255) NOT NULL,
	`roleName` varchar(255) NOT NULL,
	primary key (domainRoleID)

)
ENGINE=InnoDB 
DEFAULT CHARACTER SET utf8
DEFAULT COLLATE utf8_general_ci;

-- INSERT INTO `domain_role` VALUES ('1', 'RAT', 'admin', 'administrator');
-- INSERT INTO `domain_role` VALUES ('2', 'TestDomain', 'dgr', 'domainadmin');
