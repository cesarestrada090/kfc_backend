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
  name VARCHAR(255) NOT NULL,
  RUC VARCHAR(80) NOT NULL UNIQUE,
  legal_representation_person_id INT,
  description VARCHAR(255) NOT NULL,
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
  person_id INT,
  organization_id INT,
  FOREIGN KEY (person_id) REFERENCES person(id),
  FOREIGN KEY (organization_id) REFERENCES organization(id)
);

CREATE TABLE warehouse (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  address VARCHAR(255) NOT NULL,
  city VARCHAR(255) NOT NULL,
  status BOOLEAN NOT NULL DEFAULT true,
  organization_id INT,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  created_by INT
);

CREATE TABLE organization_warehouse (
  org_id INT NOT NULL,
  warehouse_id INT NOT NULL,
  PRIMARY KEY (org_id, warehouse_id),  -- Composite primary key
  FOREIGN KEY (org_id) REFERENCES organization(id),
  FOREIGN KEY (warehouse_id) REFERENCES warehouse(id)
);
