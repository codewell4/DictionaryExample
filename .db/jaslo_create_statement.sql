CREATE TABLE "android_metadata" ("locale" TEXT DEFAULT 'en_US');
INSERT INTO "android_metadata" ("locale") VALUES ("en_US");

-- Using fulltext search v4
CREATE VIRTUAL TABLE "dictionary" USING FTS4(
  "_id" integer NOT NULL,
  "term" varchar(512) DEFAULT NULL,
  "description" text,
);

-- Using fulltext search v3
CREATE VIRTUAL TABLE "dictionary" USING FTS3(
  "_id" integer NOT NULL,
  "term" varchar(512) DEFAULT NULL,
  "description" text,
);