package my.demo.blockchain_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class BlockchainDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlockchainDemoApplication.class, args);
    }

}
