CREATE TABLE public.immobili (
                                 id int4 NOT NULL,
                                 titolo varchar(50) NOT NULL,
                                 numerolocali int4 NOT NULL,
                                 descrizione varchar(200) NOT NULL,
                                 metriquadri numeric(8, 2) NOT NULL,
                                 lat numeric(10, 8) NOT NULL,
                                 long numeric(10, 8) NOT NULL,
                                 created_at timestamp DEFAULT CURRENT_TIMESTAMP NULL,
                                 CONSTRAINT immobili_id_check CHECK (((id >= 10000) AND (id <= 99999))),
                                 CONSTRAINT immobili_pkey PRIMARY KEY (id)
);