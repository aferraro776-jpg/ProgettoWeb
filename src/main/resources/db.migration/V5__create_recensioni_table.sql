CREATE TABLE public.recensioni (
                                   id int4 NOT NULL,
                                   titolo varchar(50) NOT NULL,
                                   descrizione varchar(200) NOT NULL,
                                   valutazione int4 NOT NULL,
                                   "data" date DEFAULT CURRENT_DATE NOT NULL,
                                   idutente int4 NULL,
                                   idimmobile int4 NULL,
                                   CONSTRAINT recensioni_id_check CHECK (((id >= 10000) AND (id <= 99999))),
                                   CONSTRAINT recensioni_pkey PRIMARY KEY (id),
                                   CONSTRAINT recensioni_valutazione_check CHECK (((valutazione >= 0) AND (valutazione <= 6))),
                                   CONSTRAINT recensioni_idimmobile_fkey FOREIGN KEY (idimmobile) REFERENCES public.immobili(id),
                                   CONSTRAINT recensioni_idutente_fkey FOREIGN KEY (idutente) REFERENCES public.utenti(id)
);