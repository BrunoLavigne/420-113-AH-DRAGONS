DROP TABLE IF EXISTS reservation 	CASCADE;
DROP TABLE IF EXISTS livre 			CASCADE;
DROP TABLE IF EXISTS membre 		CASCADE;

CREATE TABLE membre (
	idMembre        INTEGER(3) 		CHECK(idMembre > 0),
	nom             VARCHAR(10) 	NOT NULL,
	telephone       bigint(32),
	limitePret      INTEGER(2) 		CHECK(limitePret > 0 and limitePret <= 10),
	nbpret          INTEGER(2) 		DEFAULT 0 CHECK(nbpret >= 0),
	CONSTRAINT 		cleMembre 		PRIMARY KEY (idMembre),
	CONSTRAINT 		limiteNbPret 	CHECK(nbpret <= limitePret)
);

CREATE TABLE livre (
	idLivre         INTEGER(3) 		CHECK(idLivre > 0),
	titre           VARCHAR(10) 	NOT NULL,
	auteur          VARCHAR(10) 	NOT NULL,
	dateAcquisition TIMESTAMP 		NOT NULL,
	idMembre        INTEGER(3),
	datePret        TIMESTAMP,
	CONSTRAINT 		cleLivre 		PRIMARY KEY (idLivre),
	CONSTRAINT 		refPretMembre 	FOREIGN KEY (idMembre) REFERENCES membre (idMembre)
);

CREATE TABLE reservation (
	idReservation   INTEGER(3),
	idMembre        INTEGER(3),
	idLivre         INTEGER(3),
	dateReservation TIMESTAMP,
	CONSTRAINT 		cleReservation 			PRIMARY KEY (idReservation),
	CONSTRAINT 		cleCandidateReservation UNIQUE (idMembre,idLivre),
	CONSTRAINT 		refReservationMembre 	FOREIGN KEY (idMembre) 	REFERENCES membre (idMembre)
	  ON DELETE CASCADE,
	CONSTRAINT 		refReservationLivre 	FOREIGN KEY (idLivre) 	REFERENCES livre (idLivre)
	  ON DELETE CASCADE
);