CREATE TABLE public."realEstate" (
                                   id int4 NOT NULL,
                                   title varchar(50) NOT NULL,
                                   numberOfRooms int4 NOT NULL,
                                   description varchar(200) NOT NULL,
                                   squareMetres numeric(8, 2) NOT NULL,
                                   lat numeric(10, 8) NOT NULL,
                                   long numeric(10, 8) NOT NULL,
                                   address varchar(200) NOT NULL,
                                   createdAt timestamp DEFAULT CURRENT_TIMESTAMP NULL,
                                   CONSTRAINT realEstate_id_check CHECK ((id >= 10000) AND (id <= 99999)),
                                   CONSTRAINT realEstate_pkey PRIMARY KEY (id)
);