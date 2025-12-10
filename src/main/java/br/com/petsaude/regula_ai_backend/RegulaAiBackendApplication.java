package br.com.petsaude.regula_ai_backend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Regula.Ai Backend", version = "1", description = "API do projeto PetSaude Eixo-03 Grupo-05"))
public class RegulaAiBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(RegulaAiBackendApplication.class, args);
	}

}
