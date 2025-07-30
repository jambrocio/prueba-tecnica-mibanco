CREATE TABLE IF NOT EXISTS users (
  id 				SERIAL PRIMARY KEY,
  username 			VARCHAR(25) UNIQUE NOT NULL,
  password			VARCHAR(255) NOT NULL,
  nombres			VARCHAR(255) NOT NULL,
  apellido_paterno	VARCHAR(255) NOT NULL,
  apellido_materno	VARCHAR(255) NOT NULL,
  email 			VARCHAR(255) NOT NULL,
  enabled 			BIT DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS roles (
  id 				SERIAL PRIMARY KEY,
  name	 			VARCHAR(25) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS users_roles (
  user_id 				INT REFERENCES users(id),
  role_id	 			INT REFERENCES roles(id),
  UNIQUE(user_id, role_id)
);

CREATE TABLE IF NOT EXISTS driver (
  id 				SERIAL PRIMARY KEY,
  dni	 			NUMERIC(8) UNIQUE NOT NULL,
  nombres			VARCHAR(255) NOT NULL,
  apellido_paterno	VARCHAR(255) NOT NULL,
  apellido_materno	VARCHAR(255) NOT NULL,
  email 			VARCHAR(255) NOT NULL,
  fecha_nacimiento	DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS usage_type (
  id 				SERIAL PRIMARY KEY,
  name				VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS vehicle (
  id 				SERIAL PRIMARY KEY,
  year	 			NUMERIC(4) UNIQUE NOT NULL,
  marca				VARCHAR(255) NOT NULL,
  modelo 			VARCHAR(255) NOT NULL,
  usage_type_id		INT REFERENCES usage_type(id),
  driver_id			INT REFERENCES driver(id)
);
