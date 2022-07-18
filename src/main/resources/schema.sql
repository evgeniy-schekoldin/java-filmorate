DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE users (
	user_id int8 NOT NULL GENERATED ALWAYS AS IDENTITY,
	email varchar(40) NOT NULL,
	login varchar(20) NOT NULL,
	user_name varchar(20) NULL,
	birthday varchar(10) NULL,
	CONSTRAINT users_pk PRIMARY KEY (user_id),
	CONSTRAINT users_un UNIQUE (email, login)
);

DROP TABLE IF EXISTS mpa_values CASCADE;
CREATE TABLE mpa_values (
    mpa_id int4 NOT NULL GENERATED ALWAYS AS IDENTITY,
    mpa_name varchar(40) NOT NULL,
    CONSTRAINT rating_values_pk PRIMARY KEY (mpa_id)
    );

DROP TABLE IF EXISTS films CASCADE;
CREATE TABLE films (
	film_id int8 NOT NULL GENERATED ALWAYS AS IDENTITY,
	film_name varchar(40) NOT NULL,
	description varchar NULL,
	release_date varchar(10) NOT NULL,
	duration int4 NOT NULL,
	mpa_id int4 NULL,
    CONSTRAINT films_pk PRIMARY KEY (film_id),
    CONSTRAINT films_fk FOREIGN KEY (mpa_id) REFERENCES mpa_values(mpa_id)
);

DROP TABLE IF EXISTS film_likes;
CREATE TABLE film_likes (
	user_id int8 NOT NULL,
	film_id int8 NOT NULL,
    CONSTRAINT film_likes_pk PRIMARY KEY (user_id, film_id),
    CONSTRAINT film_likes_fk_1 FOREIGN KEY (film_id) REFERENCES films(film_id),
    CONSTRAINT film_likes_fk_2 FOREIGN KEY (user_id) REFERENCES users(user_id)
);

DROP TABLE IF EXISTS genre_values CASCADE ;
CREATE TABLE genre_values (
    genre_id int4 NOT NULL GENERATED ALWAYS AS IDENTITY,
    genre_name varchar(40) NOT NULL,
    CONSTRAINT genre_values_pk PRIMARY KEY (genre_id)
    );

DROP TABLE IF EXISTS film_genres CASCADE;
CREATE TABLE film_genres (
	film_id int8 NOT NULL,
	genre_id int4 NOT NULL,
	CONSTRAINT film_genres_pk PRIMARY KEY (film_id, genre_id),
    CONSTRAINT film_genres_fk_1 FOREIGN KEY (film_id) REFERENCES films(film_id),
    CONSTRAINT film_genres_fk_2 FOREIGN KEY (genre_id) REFERENCES genre_values(genre_id)
);

DROP TABLE IF EXISTS user_friends;
CREATE TABLE user_friends (
	user_id int8 NOT NULL,
	friend_id int8 NOT NULL,
	accepted bool NOT NULL,
	CONSTRAINT user_friends_pk PRIMARY KEY (user_id, friend_id),
	CONSTRAINT friends_relations_fk_1 FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
	CONSTRAINT friends_relations_fk_2 FOREIGN KEY (friend_id) REFERENCES users(user_id) ON DELETE CASCADE
);