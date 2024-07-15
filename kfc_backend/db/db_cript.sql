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
  username VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  person_id INT,
  type INT DEFAULT 0,
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

CREATE TABLE mechanic (
  id INT AUTO_INCREMENT PRIMARY KEY,
  licence_code VARCHAR(55) NOT NULL,
  specialization VARCHAR(155),
  notes VARCHAR(655),
  status BOOLEAN NOT NULL DEFAULT true,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  person_id INT,
  organization_id INT,
  FOREIGN KEY (person_id) REFERENCES person(id),
  FOREIGN KEY (organization_id) REFERENCES organization(id)
);

CREATE TABLE workshop (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  code VARCHAR(55) NOT NULL,
  description VARCHAR(355),
  status BOOLEAN NOT NULL DEFAULT true,
  organization_id INT NOT NULL,
  FOREIGN KEY (organization_id) REFERENCES organization(id)
);

CREATE TABLE unit (
  id INT AUTO_INCREMENT PRIMARY KEY,
  type VARCHAR(255) NOT NULL,  -- (e.g., truck, machinery, etc.)
  model VARCHAR(255), -- Honda, Volvo, Sony, Phillips
  serial_number VARCHAR(255), -- Machinery Serial Number
  registration_plate VARCHAR(155),  -- C5D-480
  description VARCHAR(255), -- description of the maintenance
  organization_id INT NULL,
  status BOOLEAN NOT NULL DEFAULT true,
  created_by INT NOT NULL,
  last_updated_by INT NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (organization_id) REFERENCES organization(id),
  FOREIGN KEY (created_by) REFERENCES user(id),
  FOREIGN KEY (last_updated_by) REFERENCES user(id)
);

CREATE TABLE maintenance (
  id INT AUTO_INCREMENT PRIMARY KEY,
  unit_id INT NOT NULL,
  workshop_id INT NOT NULL,
  maintenance_date DATETIME NOT NULL,
  description VARCHAR(255) NOT NULL,
  completed BOOLEAN NOT NULL DEFAULT false,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (unit_id) REFERENCES unit(id),
  FOREIGN KEY (workshop_id) REFERENCES workshop(id)
);

CREATE TABLE maintenance_detail (
  id INT AUTO_INCREMENT PRIMARY KEY,
  maintenance_id INT NOT NULL,
  -- product_id INT NOT NULL,
  quantity INT NOT NULL,
  description VARCHAR(255) NOT NULL,
  FOREIGN KEY (maintenance_id) REFERENCES maintenance(id)
  -- FOREIGN KEY (product_id) REFERENCES product(id) --
);

CREATE TABLE brand (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  created_by INT,
  updated_by INT,
  FOREIGN KEY (created_by) REFERENCES user(id),
  FOREIGN KEY (updated_by) REFERENCES user(id)
);

CREATE TABLE product_type (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  category VARCHAR(255),
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  created_by INT,
  updated_by INT,
  FOREIGN KEY (created_by) REFERENCES user(id),
  FOREIGN KEY (updated_by) REFERENCES user(id)
);

CREATE TABLE product (
  id INT AUTO_INCREMENT PRIMARY KEY,
  product_code VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255),
  warranty VARCHAR(255),
  weight DECIMAL,
  dimensions VARCHAR(255),
  serial_number VARCHAR(255),
  bar_code VARCHAR(255),
  expiration_time INT,
  brand_id INT NOT NULL,
  product_type_id INT NOT NULL,
  organization_id INT NULL,
  manufacturing_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  created_by INT,
  updated_by INT,
  FOREIGN KEY (brand_id) REFERENCES brand(id),
  FOREIGN KEY (organization_id) REFERENCES organization(id),
  FOREIGN KEY (product_type_id) REFERENCES product_type(id),
  FOREIGN KEY (created_by) REFERENCES user(id),
  FOREIGN KEY (updated_by) REFERENCES user(id)
);

CREATE TABLE supplier (
  id INT AUTO_INCREMENT PRIMARY KEY,
  supplier_code VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255),
  contact VARCHAR(255),
  position_contact VARCHAR(255),
  postal_code VARCHAR(10),
  address VARCHAR(255),
  province VARCHAR(50),
  city VARCHAR(50),
  country VARCHAR(50),
  telephone VARCHAR(100),
  fax VARCHAR(100),
  email VARCHAR(100),
  homepage VARCHAR(100),
  notes VARCHAR(100),
  organization_id INT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  created_by INT,
  updated_by INT,
  FOREIGN KEY (organization_id) REFERENCES organization(id),
  FOREIGN KEY (created_by) REFERENCES user(id),
  FOREIGN KEY (updated_by) REFERENCES user(id)
);

CREATE TABLE product_supplier (
  id INT AUTO_INCREMENT PRIMARY KEY,
  product_id INT NOT NULL,
  supplier_id INT NOT NULL,
  organization_id INT NULL,
  cost DECIMAL,
  discount DECIMAL,
  delivery_time INT,
  payment_conditions VARCHAR(255),
  status BOOLEAN NOT NULL DEFAULT true,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  created_by INT,
  updated_by INT,
  FOREIGN KEY (organization_id) REFERENCES organization(id),
  FOREIGN KEY (product_id) REFERENCES product(id),
  FOREIGN KEY (supplier_id) REFERENCES supplier(id),
  FOREIGN KEY (created_by) REFERENCES user(id),
  FOREIGN KEY (updated_by) REFERENCES user(id)
);

CREATE TABLE product_warehouse (
  id INT AUTO_INCREMENT PRIMARY KEY,
  product_supplier_id INT NOT NULL,
  warehouse_id INT NOT NULL,
  organization_id INT NULL,
  location VARCHAR(255),
  stock VARCHAR(255),
  status BOOLEAN NOT NULL DEFAULT true,
  created_by INT,
  updated_by INT,
  FOREIGN KEY (product_supplier_id) REFERENCES product_supplier(id),
  FOREIGN KEY (warehouse_id) REFERENCES warehouse(id),
  FOREIGN KEY (organization_id) REFERENCES organization(id),
  FOREIGN KEY (created_by) REFERENCES user(id),
  FOREIGN KEY (updated_by) REFERENCES user(id)
);