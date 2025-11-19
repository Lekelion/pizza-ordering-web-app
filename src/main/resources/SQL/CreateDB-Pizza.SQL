# TODO: Step 1 - execute this SQL inside MySQL (or inside Intelli-J)
DROP DATABASE IF EXISTS SpringPizza;
CREATE DATABASE SpringPizza;
USE SpringPizza;

GRANT ALL ON SpringPizza.* TO JavaApp;
FLUSH privileges;
