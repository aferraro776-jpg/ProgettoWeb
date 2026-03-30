ALTER TABLE public."realEstate" RENAME COLUMN lat TO latit;
ALTER TABLE public."realEstate" RENAME COLUMN long TO longit;

ALTER TABLE public."realEstate" ADD COLUMN tipo VARCHAR(30) NOT NULL DEFAULT 'APPARTAMENTO';

ALTER TABLE public."realEstate" ALTER COLUMN "numberOfRooms" DROP NOT NULL;

ALTER TABLE public."realEstate" ADD COLUMN floor INT;
ALTER TABLE public."realEstate" ADD COLUMN "hasElevator" BOOLEAN;

ALTER TABLE public."realEstate" ADD COLUMN "hasGarden" BOOLEAN;
ALTER TABLE public."realEstate" ADD COLUMN "hasPool" BOOLEAN;
ALTER TABLE public."realEstate" ADD COLUMN "numberOfFloors" INT;

ALTER TABLE public."realEstate" ADD COLUMN width NUMERIC(6,2);
ALTER TABLE public."realEstate" ADD COLUMN height NUMERIC(6,2);
ALTER TABLE public."realEstate" ADD COLUMN "isElectric" BOOLEAN;

ALTER TABLE public."realEstate" ADD COLUMN cubatura NUMERIC(10,2);
ALTER TABLE public."realEstate" ADD COLUMN "destinazioneUso" VARCHAR(100);

ALTER TABLE public."realEstate" ADD COLUMN "tipoColtura" VARCHAR(50);