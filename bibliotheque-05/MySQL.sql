DROP TABLE IF EXISTS reservation 	CASCADE;
DROP TABLE IF EXISTS pret 			CASCADE;
DROP TABLE IF EXISTS livre 			CASCADE;
DROP TABLE IF EXISTS membre 		CASCADE;

CREATE TABLE membre (
	idMembre        INTEGER(3) 		AUTO_INCREMENT 			CHECK(idMembre > 0),
	nom             VARCHAR(10) 	NOT NULL,
	telephone       bigint(10),
	limitePret      INTEGER(2) 		CHECK(limitePret > 0 AND limitePret <= 10),
	nbpret          INTEGER(2) 		DEFAULT 0 				CHECK(nbpret >= 0),
	CONSTRAINT 		cleMembre 		PRIMARY KEY (idMembre),
	CONSTRAINT 		limiteNbPret 	CHECK(nbpret <= limitePret)
);

CREATE TABLE livre (
	idLivre         INTEGER(3) 		AUTO_INCREMENT 			CHECK(idLivre > 0),
	titre           VARCHAR(100) 	NOT NULL,
	auteur          VARCHAR(100) 	NOT NULL,
	dateAcquisition TIMESTAMP	 	NOT NULL,
	CONSTRAINT 		cleLivre 		PRIMARY KEY (idLivre)
);

CREATE TABLE pret (
	idPret 			INTEGER(3) 		AUTO_INCREMENT 			CHECK (idPret > 0),
	idMembre 		INTEGER(3)  							CHECK (idMembre > 0),
	idLivre 		INTEGER(3) 								CHECK (idLivre > 0),
	datePret 		TIMESTAMP,
	dateRetour 		TIMESTAMP 		NULL,
	CONSTRAINT 		clePrimairePret PRIMARY KEY (idPret),
	CONSTRAINT 		refPretMembre 	FOREIGN KEY (idMembre) 	REFERENCES membre (idMembre),
	CONSTRAINT 		refPretLivre 	FOREIGN KEY (idLivre) 	REFERENCES livre (idLivre)
);

CREATE TABLE reservation (
	idReservation   INTEGER(3)		AUTO_INCREMENT 			CHECK (idReservation > 0),
	idMembre        INTEGER(3)								CHECK (idMembre > 0),
	idLivre         INTEGER(3)								CHECK (idLivre > 0),
	dateReservation TIMESTAMP,
	CONSTRAINT 		clePrimaireReservation					PRIMARY KEY (idReservation),
	CONSTRAINT 		cleEtrangereReservation 				UNIQUE (idMembre,idLivre),
	CONSTRAINT 		refReservationMembre 					FOREIGN KEY (idMembre) 	REFERENCES membre (idMembre)
	  ON DELETE CASCADE,
	CONSTRAINT 		refReservationLivre 					FOREIGN KEY (idLivre) 	REFERENCES livre (idLivre)
	  ON DELETE CASCADE
);