DROP TABLE IF EXISTS client CASCADE;
CREATE TABLE client (
                        utilisateur     varchar(255) NOT NULL,
                        motDePasse      varchar(255) NOT NULL,
                        acces           numeric(1) NOT NULL DEFAULT 1,
                        nom             varchar(25) NOT NULL,
                        prenom          varchar(25) NOT NULL,
                        age             numeric(3),
                        PRIMARY KEY (utilisateur)
);

DROP TABLE IF EXISTS chambre CASCADE;
CREATE TABLE chambre (
                         idChambre       numeric(3) check(idChambre > 0) ,
                         nomChambre      varchar(50) NOT NULL,
                         typeLit         varchar(25) NOT NULL,
                         prixBase        numeric(5,2),
                         PRIMARY KEY (idChambre)
);

DROP TABLE IF EXISTS reservation CASCADE;
CREATE TABLE reservation (
                             utilisateur     varchar(255) ,
                             idChambre       numeric(3) ,
                             prixTotal       numeric(5,2),
                             dateDebut       date ,
                             dateFin         date ,
                             PRIMARY KEY (utilisateur, idChambre) ,
                             FOREIGN KEY (utilisateur) REFERENCES client ON DELETE CASCADE,
                             FOREIGN KEY (idChambre) REFERENCES chambre ON DELETE CASCADE
);

DROP TABLE IF EXISTS commodite CASCADE;
CREATE TABLE commodite (
                           idCommodite     numeric(3) check(idCommodite > 0) ,
                           description     varchar(100),
                           surplusPrix     numeric(5,2),
                           PRIMARY KEY (idCommodite)
);

DROP TABLE IF EXISTS service CASCADE;
CREATE TABLE service (
                         idChambre       numeric(3) ,
                         idCommodite     numeric(3),
                         PRIMARY KEY (idChambre, idCommodite) ,
                         FOREIGN KEY (idChambre) REFERENCES chambre ON DELETE CASCADE,
                         FOREIGN KEY (idCommodite) REFERENCES commodite ON DELETE CASCADE
);
insert into client(utilisateur, motDePasse, acces, nom, prenom, age) values('admin', 'test', 0, 'admin','admin' , 0);