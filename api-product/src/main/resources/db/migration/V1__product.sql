CREATE TABLE product(
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  product_name VARCHAR(50),
  unit_price NUMERIC,
  category VARCHAR(50),
  image_link VARCHAR(120),
  number_sold NUMERIC,
  unit_cost NUMERIC
);
