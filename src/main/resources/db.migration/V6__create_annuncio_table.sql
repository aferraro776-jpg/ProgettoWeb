CREATE TABLE public.annuncio (
                                 id int4 NOT NULL,
                                 titolo varchar(50) NOT NULL,
                                 descrizione varchar(200) NOT NULL,
                                 prezzoprecedente numeric(10, 2) NULL,
                                 prezzoattuale numeric(10, 2) NOT NULL,
                                 "data" date DEFAULT CURRENT_DATE NOT NULL,
                                 idvenditore int4 NULL,
                                 idimmobile int4 NULL,
                                 CONSTRAINT annuncio_id_check CHECK (((id >= 10000) AND (id <= 99999))),
                                 CONSTRAINT annuncio_pkey PRIMARY KEY (id),
                                 CONSTRAINT annuncio_idimmobile_fkey FOREIGN KEY (idimmobile) REFERENCES public.immobili(id),
                                 CONSTRAINT annuncio_idvenditore_fkey FOREIGN KEY (idvenditore) REFERENCES public.venditori(id)
);