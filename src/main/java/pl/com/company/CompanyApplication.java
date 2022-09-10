package pl.com.company;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.com.company.controller.EmployeeController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
public class CompanyApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompanyApplication.class, args);



	}


}
