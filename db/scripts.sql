-- create the main db
CREATE DATABASE `cities` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE cities;

-- create our source table
CREATE TABLE `source_cities` (
  `Country` varchar(50) DEFAULT NULL,
  `City` varchar(250) DEFAULT NULL,
  `AccentCity` varchar(250) DEFAULT NULL,
  `Region` varchar(50) DEFAULT NULL,
  `Population` varchar(50) DEFAULT NULL,
  `Latitude` float DEFAULT NULL,
  `Longitude` float DEFAULT NULL,
  KEY `citynames` (`City`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- import the Maxmind DB now into source_cities

-- create the destination table (we don't want/need all columns)
CREATE TABLE `destination_cities` (
  `Country` varchar(50) DEFAULT NULL,
  `City` varchar(250) DEFAULT NULL,
  `Population` varchar(50) DEFAULT NULL,
  KEY `citynames` (`City`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;