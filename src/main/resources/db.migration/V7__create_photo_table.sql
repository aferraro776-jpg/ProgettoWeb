CREATE TABLE public.photos (
   id          INT           PRIMARY KEY,
   url         VARCHAR(255)  NOT NULL,
   postId      INT           NOT NULL,
   UNIQUE(url),
   FOREIGN KEY (postId) REFERENCES public.posts(id)
);