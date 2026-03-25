CREATE TABLE public.users (
                               id int4 NOT NULL,
                               name varchar(50) NOT NULL,
                               surname varchar(50) NOT NULL,
                               email varchar(50) NOT NULL,
                               birthDate date NOT NULL,
                               "password" varchar(50) NOT NULL,
                               CONSTRAINT users_email_key UNIQUE (email),
                               CONSTRAINT users_id_check CHECK (((id >= 10000) AND (id <= 99999))),
                               CONSTRAINT users_pkey PRIMARY KEY (id)
);