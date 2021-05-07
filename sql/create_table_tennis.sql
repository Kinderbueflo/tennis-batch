/*============================================================*/
/*                         22/12/2020                         */
/*                         version 1.1                        */
/*============================================================*/


DROP TABLE IF EXISTS tennis_player CASCADE;
DROP TABLE IF EXISTS tennis_surface CASCADE;
DROP TABLE IF EXISTS tennis_level CASCADE;
DROP TABLE IF EXISTS tennis_tournament CASCADE;
DROP TABLE IF EXISTS tennis_score CASCADE;

/*============================================================*/
/*                     Table : tennis_player                  */
/*============================================================*/
CREATE TABLE tennis_player (
       player_id       SERIAL       NOT NULL,
	   last_name       VARCHAR(250),
       first_name      VARCHAR(250),
       name            VARCHAR(250)  NOT NULL,
       sexe            VARCHAR(10)  NOT NULL,
       PRIMARY KEY(player_id),
	   UNIQUE (name, sexe)
);

/*============================================================*/
/*                     Table : tennis_surface                 */
/*============================================================*/
CREATE TABLE tennis_surface (
       surface_id      SERIAL       NOT NULL,
	   surface         VARCHAR(10)  NOT NULL,
       PRIMARY KEY(surface_id)
);

/*============================================================*/
/*                     Table : tennis_level                   */
/*============================================================*/
CREATE TABLE tennis_level (
       level_id        SERIAL       NOT NULL,
	   level           VARCHAR(15)  NOT NULL,
       PRIMARY KEY(level_id)
);

/*============================================================*/
/*                     Table : tennis_tournament              */
/*============================================================*/
CREATE TABLE tennis_tournament (
       tournament_id   SERIAL       NOT NULL,
	   name            VARCHAR(50)  NOT NULL,
       surface         INTEGER      NOT NULL,
       level           INTEGER      NOT NULL,
       year            VARCHAR(5)   NOT NULL,	   
       PRIMARY KEY(tournament_id),
	   FOREIGN KEY(surface) REFERENCES tennis_surface(surface_id),
       FOREIGN KEY(level)   REFERENCES tennis_level(level_id),
	   UNIQUE (name, level, year)
);

/*============================================================*/
/*                     Table : tennis_score                   */
/*============================================================*/
CREATE TABLE tennis_score (
       score_id        SERIAL       NOT NULL,
	   player          INTEGER      NOT NULL,
	   opponent        INTEGER      NOT NULL,
       odds            NUMERIC      NOT NULL,
       victory         BOOLEAN      NOT NULL,
	   set_won         INTEGER      NOT NULL,
       tournament      INTEGER      NOT NULL,	   
       match_date      DATE         NOT NULL,	  
       PRIMARY KEY(score_id),
	   FOREIGN KEY(player)  REFERENCES tennis_player(player_id),
	   FOREIGN KEY(opponent)  REFERENCES tennis_player(player_id),
       FOREIGN KEY(tournament) REFERENCES tennis_tournament(tournament_id)
);
