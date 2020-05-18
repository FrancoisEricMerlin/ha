
--
-- Structure for table hatest_hatest
--

DROP TABLE IF EXISTS hatest_hatest;
CREATE TABLE hatest_hatest (
id_hatest int AUTO_INCREMENT,
nom varchar(50) default '',
prenom long varchar,
adresse long varchar,
datenaissance date,
PRIMARY KEY (id_hatest)
);
