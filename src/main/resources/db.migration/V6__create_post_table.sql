CREATE TABLE public.posts (
                              id int4 NOT NULL,
                              title varchar(50) NOT NULL,
                              description varchar(200) NOT NULL,
                              previousPrice numeric(10, 2) NULL,
                              currentPrice numeric(10, 2) NOT NULL,
                              createdAt date DEFAULT CURRENT_DATE NOT NULL,
                              idSeller int4 NULL,
                              idRealEstate int4 NULL,
                              CONSTRAINT posts_id_check CHECK ((id >= 10000) AND (id <= 99999)),
                              CONSTRAINT posts_pkey PRIMARY KEY (id),
                              CONSTRAINT posts_idRealEstate_fkey FOREIGN KEY (idRealEstate) REFERENCES public."realEstate"(id),
                              CONSTRAINT posts_idSeller_fkey FOREIGN KEY (idSeller) REFERENCES public.sellers(id)
);