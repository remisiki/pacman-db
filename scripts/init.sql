CREATE DATABASE core;
CREATE TABLE IF NOT EXISTS core.license (
	id INT NOT NULL AUTO_INCREMENT,
	name VARCHAR(20),
	CONSTRAINT pk PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS core.arch (
	id INT NOT NULL AUTO_INCREMENT,
	name VARCHAR(20),
	CONSTRAINT pk PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS core.packager (
	id INT NOT NULL AUTO_INCREMENT,
	name VARCHAR(31),
	email VARCHAR(63),
	CONSTRAINT pk PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS core.package (
	id INT NOT NULL AUTO_INCREMENT,
	name VARCHAR(31),
	version VARCHAR(31),
	description VARCHAR(255),
	csize INT,
	isize INT,
	md5sum CHAR(32),
	sha256sum CHAR(64),
	pgpSig VARCHAR(1023),
	url VARCHAR(255),
	licenseId INT,
	archId INT,
	buildDate DATETIME,
	packagerId INT,
	CONSTRAINT pk PRIMARY KEY (id),
	FOREIGN KEY (archId) REFERENCES arch(id),
	FOREIGN KEY (licenseId) REFERENCES license(id),
	FOREIGN KEY (packagerId) REFERENCES packager(id)
);
CREATE TABLE IF NOT EXISTS core.dependency (
	id INT NOT NULL AUTO_INCREMENT,
	packageId INT,
	dependId INT,
	CONSTRAINT pk PRIMARY KEY (id),
	FOREIGN KEY (packageId) REFERENCES package(id),
	FOREIGN KEY (dependId) REFERENCES package(id)
);