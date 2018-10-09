package me.changchao.alexa.ws.helloalexaskill;

import java.lang.reflect.Field;
import java.util.List;

import javax.servlet.http.HttpServlet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.amazon.ask.Skill;
import com.amazon.ask.Skills;
import com.amazon.ask.servlet.SkillServlet;
import com.amazon.ask.servlet.verifiers.SkillServletVerifier;

import lombok.SneakyThrows;
import me.changchao.alexa.ws.helloalexaskill.handlers.HelloIntentHandler;
import me.changchao.alexa.ws.helloalexaskill.handlers.LaunchRequestHandler;
import me.changchao.alexa.ws.helloalexaskill.handlers.SessionEndedRequestHandler;

@SpringBootApplication
public class HelloAlexaSkillApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloAlexaSkillApplication.class, args);
	}

	@SuppressWarnings("unchecked")
	@Bean
	Skill getSkill() {
		return Skills.standard()
				.addRequestHandlers(new HelloIntentHandler(), new LaunchRequestHandler(),
						new SessionEndedRequestHandler())
				.withSkillId("amzn1.ask.skill.c2842b02-7203-44b7-854f-9b62fbb4f33a").build();
	}

	@Bean
	@SneakyThrows
	public ServletRegistrationBean<HttpServlet> skillServlet(Skill skill) {
		ServletRegistrationBean<HttpServlet> servRegBean = new ServletRegistrationBean<>();
		SkillServlet servlet = new SkillServlet(skill);
		Field f = servlet.getClass().getDeclaredField("verifiers"); // NoSuchFieldException
		f.setAccessible(true);
		@SuppressWarnings("unchecked")
		List<SkillServletVerifier> verifiers = (List<SkillServletVerifier>) f.get(servlet); // IllegalAccessException
		verifiers.clear();
		servRegBean.setServlet(servlet);
		servRegBean.addUrlMappings("/cek/v1");
		servRegBean.setLoadOnStartup(1);
		return servRegBean;
	}
}
