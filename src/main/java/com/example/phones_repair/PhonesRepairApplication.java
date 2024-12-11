package com.example.phones_repair;

import javafx.application.Application;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.spring.SpringFxWeaver;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PhonesRepairApplication {
	public static void main(String[] args) {
		Application.launch(MainAppWindow.class, args);
	}
	@Bean
	public FxWeaver fxWeaver(ConfigurableApplicationContext applicationContext) {
		return new SpringFxWeaver(applicationContext);
	}
}
