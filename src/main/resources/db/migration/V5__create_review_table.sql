CREATE TABLE public.reviews (
                                   id int4 NOT NULL,
                                   title varchar(50) NOT NULL,
                                   description varchar(200) NOT NULL,
                                   rating int4 NOT NULL,
                                   "date" date DEFAULT CURRENT_DATE NOT NULL,
                                   id_user int4 NULL,
                                   id_real_estate int4 NULL,
                                   CONSTRAINT reviews_id_check CHECK (((id >= 10000) AND (id <= 99999))),
                                   CONSTRAINT reviews_pkey PRIMARY KEY (id),
                                   CONSTRAINT reviews_valutazione_check CHECK (((rating >= 0) AND (rating <= 5))),
                                   CONSTRAINT reviews_id_realEstate_fkey FOREIGN KEY (id_real_estate) REFERENCES public."real_estate"(id),
                                   CONSTRAINT reviews_id_user_fkey FOREIGN KEY (id_user) REFERENCES public.users(id)
);