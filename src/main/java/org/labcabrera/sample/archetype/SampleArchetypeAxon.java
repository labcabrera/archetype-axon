package org.labcabrera.sample.archetype;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SampleArchetypeAxon {

	public static void main(String[] args) {
		SpringApplication.run(SampleArchetypeAxon.class, args);
	}

}
