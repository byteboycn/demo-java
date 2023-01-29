-- 建库
DROP DATABASE IF EXISTS demo_ds_0;
DROP DATABASE IF EXISTS demo_ds_1;
DROP DATABASE IF EXISTS demo_ds_2;
CREATE DATABASE demo_ds_0;
CREATE DATABASE demo_ds_1;
CREATE DATABASE demo_ds_2;
-- 建表
DROP TABLE IF EXISTS demo_ds_0.t_order;
DROP TABLE IF EXISTS demo_ds_1.t_order;
DROP TABLE IF EXISTS demo_ds_2.t_order;
DROP TABLE IF EXISTS demo_ds_0.t_order_item;
DROP TABLE IF EXISTS demo_ds_1.t_order_item;
DROP TABLE IF EXISTS demo_ds_2.t_order_item;
DROP TABLE IF EXISTS demo_ds_0.t_address;
DROP TABLE IF EXISTS demo_ds_1.t_address;
DROP TABLE IF EXISTS demo_ds_2.t_address;
CREATE TABLE IF NOT EXISTS demo_ds_0.t_order (order_id BIGINT, order_type INT(11), user_id INT NOT NULL, address_id BIGINT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_id));
CREATE TABLE IF NOT EXISTS demo_ds_1.t_order (order_id BIGINT, order_type INT(11), user_id INT NOT NULL, address_id BIGINT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_id));
CREATE TABLE IF NOT EXISTS demo_ds_2.t_order (order_id BIGINT, order_type INT(11), user_id INT NOT NULL, address_id BIGINT NOT NULL, status VARCHAR(50), PRIMARY KEY (order_id));
CREATE TABLE IF NOT EXISTS demo_ds_0.t_order_item (order_item_id BIGINT AUTO_INCREMENT, order_id BIGINT, user_id INT NOT NULL, phone VARCHAR(50), status VARCHAR(50) , PRIMARY KEY (order_item_id));
CREATE TABLE IF NOT EXISTS demo_ds_1.t_order_item (order_item_id BIGINT AUTO_INCREMENT, order_id BIGINT, user_id INT NOT NULL, phone VARCHAR(50), status VARCHAR(50) , PRIMARY KEY (order_item_id));
CREATE TABLE IF NOT EXISTS demo_ds_2.t_order_item (order_item_id BIGINT AUTO_INCREMENT, order_id BIGINT, user_id INT NOT NULL, phone VARCHAR(50), status VARCHAR(50) , PRIMARY KEY (order_item_id));
CREATE TABLE IF NOT EXISTS demo_ds_0.t_address (address_id BIGINT NOT NULL, address_name VARCHAR(100) NOT NULL, PRIMARY KEY (address_id));
CREATE TABLE IF NOT EXISTS demo_ds_1.t_address (address_id BIGINT NOT NULL, address_name VARCHAR(100) NOT NULL, PRIMARY KEY (address_id));
CREATE TABLE IF NOT EXISTS demo_ds_2.t_address (address_id BIGINT NOT NULL, address_name VARCHAR(100) NOT NULL, PRIMARY KEY (address_id));