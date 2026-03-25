CREATE TABLE public.venditori (
                                  id int4 NOT NULL,
                                  partitaiva varchar(11) NOT NULL,
                                  nome varchar(50) NOT NULL,
                                  cognome varchar(50) NOT NULL,
                                  email varchar(50) NOT NULL,
                                  datanascita date NOT NULL,
                                  "password" varchar(50) NOT NULL,
                                  CONSTRAINT venditori_email_key UNIQUE (email),
                                  CONSTRAINT venditori_id_check CHECK (((id >= 10000) AND (id <= 99999))),
                                  CONSTRAINT venditori_partitaiva_key UNIQUE (partitaiva),
                                  CONSTRAINT venditori_pkey PRIMARY KEY (id)
);