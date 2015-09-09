DROP TABLE IF EXISTS livre CASCADE;
DROP TABLE IF EXISTS membre CASCADE;

CREATE TABLE membre (
idMembre INTEGER(3) check(idMembre > 0),
nom VARCHAR(10) NOT NULL,
telephone INTEGER(20),
limitePret INTEGER(2), check(limitePret > 0 and limitePret <= 10),
nbpret INTEGER(2) default 0 check(nbpret >= 0),
CONSTRAINT cleMembre PRIMARY KEY (idMembre),
CONSTRAINT limiteNbPret check(nbpret <= limitePret));

CREATE TABLE livre (
idLivre         INTEGER(3) check(idLivre > 0),
titre           varchar(10) NOT NULL,
auteur          varchar(10) NOT NULL,
dateAcquisition DATE not null,
idMembre        INTEGER(3),
datePret        DATE,
CONSTRAINT cleLivre PRIMARY KEY (idLivre),
CONSTRAINT refPretMembre FOREIGN KEY (idMembre) REFERENCES membre (idMembre));