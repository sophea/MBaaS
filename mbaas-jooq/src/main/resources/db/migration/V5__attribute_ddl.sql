CREATE TABLE `attribute` (

  attribute_id         VARCHAR(100) NOT NULL,

  collection_id        VARCHAR(100) NOT NULL,
  name                 VARCHAR(255) NOT NULL,

  sql_type             VARCHAR(50)  NOT NULL,
  java_type            VARCHAR(50)  NOT NULL,
  virtual              BIT(1)       NOT NULL,

  virtual_attribute_id VARCHAR(100),

  system               BIT(1)       NOT NULL,
  exposed              BIT(1)       NOT NULL,
  nullable             BIT(1)       NOT NULL,
  auto_increment       BIT(1)       NOT NULL,

  INDEX (name),
  INDEX (virtual_attribute_id),
  INDEX (collection_id),
  UNIQUE KEY (name, collection_id),
  PRIMARY KEY (attribute_id)

);