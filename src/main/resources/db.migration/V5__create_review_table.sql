CREATE TABLE public.reviews (
   id int4 NOT NULL,
   title varchar(50) NOT NULL,
   description varchar(200) NOT NULL,
   rating int4 NOT NULL,
   "date" date DEFAULT CURRENT_DATE NOT NULL,
   idUser int4 NULL,
   idRealEstate int4 NULL,
   CONSTRAINT reviews_id_check CHECK (((id >= 10000) AND (id <= 99999))),
   CONSTRAINT reviews_pkey PRIMARY KEY (id),
   CONSTRAINT reviews_valutazione_check CHECK (((rating >= 0) AND (rating <= 5))),
   CONSTRAINT reviews_idRealEstate_fkey FOREIGN KEY (idRealEstate) REFERENCES public."realEstate"(id),
   CONSTRAINT reviews_idUser_fkey FOREIGN KEY (idUser) REFERENCES public.users(id)
);