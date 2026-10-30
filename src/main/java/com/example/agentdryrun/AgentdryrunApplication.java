package com.example.agentdryrun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AgentdryrunApplication {

	public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AgentdryrunApplication.class, args);
	}

}
