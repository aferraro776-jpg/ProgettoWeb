CREATE TABLE foto (
                      id          INT           PRIMARY KEY,
                      url         VARCHAR(255)  NOT NULL,
                      idAnnuncio  INT           NOT NULL,
                      UNIQUE(url),
                      FOREIGN KEY (idAnnuncio) REFERENCES annuncio(id)
);
