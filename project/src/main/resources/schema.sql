CREATE TABLE IF NOT EXISTS users(
  id SERIAL,
  username VARCHAR(60) NOT NULL,
  password VARCHAR(60) NOT NULL,
  enabled BOOLEAN NOT NULL,
  locked BOOLEAN NOT NULL,
  email VARCHAR(255),
  registrationdate DATE NOT NULL,
  totaldatavolume BIGINT,
  dailyuploadvolume BIGINT,
  last_change TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  last_change_userid INTEGER,
  PRIMARY KEY (id),
  FOREIGN KEY (last_change_userid) REFERENCES users(id));

CREATE TABLE IF NOT EXISTS authorities(
  userid INTEGER NOT NULL,
  authority VARCHAR(50) NOT NULL,
  FOREIGN KEY (userid) REFERENCES users(id));

CREATE TABLE IF NOT EXISTS projects(
  id SERIAL,
  title VARCHAR(255),
  description TEXT,
  userid INTEGER NOT NULL,
  parentid INTEGER,
  updatedparent BOOLEAN NOT NULL,
  last_change TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  last_change_userid INTEGER,
  PRIMARY KEY (id),
  FOREIGN KEY (userid) REFERENCES users(id),
  FOREIGN KEY (parentid) REFERENCES projects(id),
  FOREIGN KEY (last_change_userid) REFERENCES users(id));

CREATE TABLE IF NOT EXISTS assets(
  id SERIAL,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  url varchar(2083),/* Internet Explorer supports url with length up to 2083 only, nvarchar to support all unicode characters  */
  metadata VARCHAR(255),
  astype SMALLINT NOT NULL,
  last_change TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  last_change_userid INTEGER,
  PRIMARY KEY (id),
  FOREIGN KEY (last_change_userid) REFERENCES users(id));

CREATE TABLE IF NOT EXISTS features(
  id SERIAL,
  title VARCHAR(255),
  description TEXT,
  parentid INTEGER,
  updatedparent BOOLEAN NOT NULL,
  last_change TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  last_change_userid INTEGER,
  PRIMARY KEY (id),
  FOREIGN KEY (parentid) REFERENCES features(id),
  FOREIGN KEY (last_change_userid) REFERENCES users(id));

CREATE TABLE IF NOT EXISTS artefacts(
  id SERIAL,
  title VARCHAR(255) NOT NULL,
  start VARCHAR(255),
  endmark VARCHAR(255),
  picturewidth VARCHAR(255),
  pictureheight VARCHAR(255),
  featureid INTEGER NOT NULL,
  assetid INTEGER NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (featureid) REFERENCES features(id),
  FOREIGN KEY (assetid) REFERENCES assets(id));

CREATE TABLE IF NOT EXISTS products(
  id SERIAL,
  title VARCHAR(255),
  description TEXT,
  last_change TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  last_change_userid INTEGER,
  PRIMARY KEY (id),
  FOREIGN KEY (last_change_userid) REFERENCES users(id));

CREATE TABLE IF NOT EXISTS tracking(
  itemid INTEGER NOT NULL,
  trtype SMALLINT NOT NULL,
  changemade TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  text TEXT);

CREATE TABLE IF NOT EXISTS projectsXproducts(
  projectid INTEGER NOT NULL,
  productid INTEGER NOT NULL,
  PRIMARY KEY (projectid, productid),
  FOREIGN KEY (projectid) REFERENCES projects(id),
  FOREIGN KEY (productid) REFERENCES products(id));

CREATE TABLE IF NOT EXISTS projectsXfeatures(
  projectid INTEGER NOT NULL,
  featureid INTEGER NOT NULL,
  PRIMARY KEY (projectid, featureid),
  FOREIGN KEY (projectid) REFERENCES projects(id),
  FOREIGN KEY (featureid) REFERENCES features(id));

CREATE TABLE IF NOT EXISTS productsXfeatures(
  productid INTEGER NOT NULL,
  featureid INTEGER NOT NULL,
  PRIMARY KEY (productid, featureid),
  FOREIGN KEY (productid) REFERENCES products(id),
  FOREIGN KEY (featureid) REFERENCES features(id));

CREATE TABLE IF NOT EXISTS featuresXassets(
  featureid INTEGER NOT NULL,
  assetid INTEGER NOT NULL,
  PRIMARY KEY (featureid, assetid),
  FOREIGN KEY (featureid) REFERENCES features(id),
  FOREIGN KEY (assetid) REFERENCES assets(id));

CREATE TABLE IF NOT EXISTS verificationtokens(
  id SERIAL,
  token VARCHAR(50) NOT NULL,
  userid INTEGER NOT NULL,
  expirationdate DATE,
  PRIMARY KEY (id),
  FOREIGN KEY (userid) REFERENCES users(id));

CREATE TABLE IF NOT EXISTS passwordresettokens(
  id SERIAL,
  token VARCHAR(50) NOT NULL,
  userid INTEGER NOT NULL,
  expirationdate DATE,
  PRIMARY KEY (id),
  FOREIGN KEY (userid) REFERENCES users(id));
