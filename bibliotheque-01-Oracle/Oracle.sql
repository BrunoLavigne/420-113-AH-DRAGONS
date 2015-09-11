DROP TABLE reservation CASCADE CONSTRAINTS PURGE;
DROP TABLE livre CASCADE CONSTRAINTS PURGE;
DROP TABLE membre CASCADE CONSTRAINTS PURGE;

CREATE TABLE membre (
idMembre        number(3) CHECK(idMembre > 0),
nom             varchar(10) NOT NULL,
telephone       number(10),
limitePret      number(2) CHECK(limitePret > 0 and limitePret <= 10),
nbpret          number(2) default 0 CHECK(nbpret >= 0),
PRIMARY KEY (idMembre),
CONSTRAINT limiteNbPret check(nbpret <= limitePret)
);

CREATE TABLE livre (
idLivre         number(3) check(idLivre > 0),
titre           varchar(10) NOT NULL,
auteur          varchar(10) NOT NULL,
dateAcquisition date not null,
idMembre        number(3),
datePret        date,
CONSTRAINT cleLivre PRIMARY KEY (idLivre),
CONSTRAINT refPretMembre FOREIGN KEY (idMembre) REFERENCES membre
);

CREATE TABLE reservation (
idReservation   number(3),
idMembre        number(3),
idLivre         number(3),
dateReservation date,
CONSTRAINT cleReservation PRIMARY KEY (idReservation),
CONSTRAINT cleCandidateReservation UNIQUE (idMembre,idLivre),
CONSTRAINT refReservationMembre FOREIGN KEY (idMembre) REFERENCES membre
  ON DELETE CASCADE,
CONSTRAINT refReservationLivre FOREIGN KEY (idLivre) REFERENCES livre
  ON DELETE CASCADE
);