package com.tutorial.api.demo.database;

import com.tutorial.api.demo.models.Product;
import com.tutorial.api.demo.repositories.ProductRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Database {
    private static final Logger logger = LoggerFactory.getLogger(Database.class);
    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository){
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
//                Product productA = new Product("Macbook pro 15",2020,2500.0,"");
//                Product productB = new Product("Ipad abc abcd",2022,500.0,"");
//                logger.info("insert data: "+productRepository.save(productA));
//                logger.info("insert data: "+productRepository.save(productB));

            }
        };
    }
}
// Now connect with mysql using JPA
/*
docker run -d --rm --name mysql-spring-boot-tutorial \
-e MYSQL_ROOT_PASSWORD=123456 \
-e MYSQL_USER=toantruong \
-e MYSQL_PASSWORD=123456 \
-e MYSQL_DATABASE=test_db \
-P 3308:3306 \
--volume mysql-spring-boot-tutorial-volume:/var/lib/mysql \
mysql:latest

mysql -h localhost -P 3309 --protocol=tcp -u toantruong -p

docker run -d --rm --name mysql-spring-boot-tutorial \
-e MYSQL_ROOT_PASSWORD=123456 \
-e MYSQL_USER=toantruong \
-e MYSQL_DATABASE=test_db \
--name mysql-spring-boot-tutorial \
-p 3309:3306 \
--volume mysql-spring-boot-tutorial-volume:/var/lib/mysql \
-d mysql:8.0.28

docker exec -it mysql-spring-boot-tutorial bash
grant all privileges on *.* to bill@localhost identified by 'pass' with grant option;

docker run -d --rm --name mysql-spring \
-e MYSQL_ROOT_PASSWORD=123456 \
-e MYSQL_USER=toantruong \
-e MYSQL_DATABASE=test_db \
--name mysql-spring \
-p 3309:3306 \
--volume mysql-spring-volume:/var/lib/mysql \
-d mysql:latest

Then connect mysql =>
mysql -u root -p


'toantruong'@'172.17.0.1'

mysql> CREATE USER 'toantruong'@'172.17.0.1' IDENTIFIED BY '123456';
mysql> GRANT ALL PRIVILEGES ON *.* TO 'toantruong'@'172.17.0.1' WITH GRANT OPTION;
mysql> FLUSH PRIVILEGES;
* */














