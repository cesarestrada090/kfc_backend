create database kfc_backend;
use kfc_backend;

CREATE TABLE person (
  id INT AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(125) NOT NULL,
  last_name VARCHAR(125) NOT NULL,
  document_number VARCHAR(80) NOT NULL UNIQUE,
  phone_number VARCHAR(60) NOT NULL,
  email VARCHAR(60) NOT NULL
);

CREATE TABLE organization (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL UNIQUE,
  RUC VARCHAR(80) NOT NULL UNIQUE,
  legal_representation_person_id INT UNIQUE,
  description VARCHAR(255) NOT NULL UNIQUE,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (legal_representation_person_id) REFERENCES person(id)
);

CREATE TABLE user (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  person_id INT UNIQUE,
  organization_id INT UNIQUE,
  FOREIGN KEY (person_id) REFERENCES person(id),
  FOREIGN KEY (organization_id) REFERENCES organization(id)
);

