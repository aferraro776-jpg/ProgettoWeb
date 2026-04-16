CREATE TABLE public.sellers (
      id int4 NOT NULL,
      vatNumber varchar(11) NOT NULL,
      name varchar(50) NOT NULL,
      surname varchar(50) NOT NULL,
      email varchar(50) NOT NULL,
      birthDate date NOT NULL,
      "password" varchar(50) NOT NULL,
      CONSTRAINT sellers_email_key UNIQUE (email),
      CONSTRAINT sellers_id_check CHECK (((id >= 10000) AND (id <= 99999))),
      CONSTRAINT sellers_vatNumber_key UNIQUE (vatNumber),
      CONSTRAINT sellers_pkey PRIMARY KEY (id)
);