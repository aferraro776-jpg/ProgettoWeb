CREATE TABLE public.utenti (
                               id int4 NOT NULL,
                               nome varchar(50) NOT NULL,
                               cognome varchar(50) NOT NULL,
                               email varchar(50) NOT NULL,
                               datanascita date NOT NULL,
                               "password" varchar(50) NOT NULL,
                               CONSTRAINT utenti_email_key UNIQUE (email),
                               CONSTRAINT utenti_id_check CHECK (((id >= 10000) AND (id <= 99999))),
                               CONSTRAINT utenti_pkey PRIMARY KEY (id)
);