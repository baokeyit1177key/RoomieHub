package org.example.roomiehub;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "EXE-BE", version = "1.0", description = "Information"))
public class RoomieHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoomieHubApplication.class, args);
    }

}
