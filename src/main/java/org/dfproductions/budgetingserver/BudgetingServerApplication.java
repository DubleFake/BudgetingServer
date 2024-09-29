package org.dfproductions.budgetingserver;

import org.dfproductions.budgetingserver.web.security.RestSecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(RestSecurityConfig.class)
public class BudgetingServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BudgetingServerApplication.class, args);
	}

}
