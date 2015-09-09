DROP TABLE IF EXISTS membre CASCADE;
DROP TABLE IF EXISTS livre CASCADE;

CREATE TABLE membre (
idMembre INTEGER(3) check(idMembre > 0),
nom VARCHAR(10) NOT NULL,
telephone INTEGER(20),
limitePret INTEGER(2), check(limitePret > 0 and limitePret <= 10),
nbpret INTEGER(2) default 0 check(nbpret >= 0),
CONSTRAINT cleMembre PRIMARY KEY (idMembre),
CONSTRAINT limiteNbPret check(nbpret <= limitePret));