package cl.sterbe.app;

import cl.sterbe.app.domains.models.profiles.*;
import cl.sterbe.app.infrastructure.documents.profiles.ProfileDocument;
import cl.sterbe.app.infrastructure.documents.users.RoleDocument;
import cl.sterbe.app.infrastructure.documents.users.UserDocument;
import cl.sterbe.app.infrastructure.repositories.profiles.ProfileRepository;
import cl.sterbe.app.infrastructure.repositories.users.RoleRepository;
import cl.sterbe.app.infrastructure.repositories.users.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

@SpringBootApplication
public class SpringWebfluxApiApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringWebfluxApiApplication.class, args);
	}

	//vamos a ingresar datos **********************

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProfileRepository profileRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ReactiveMongoTemplate reactiveMongoTemplate;

	private Logger logger = LoggerFactory.getLogger(SpringWebfluxApiApplication.class);

	@Override
	public void run(String... args) throws Exception {

		//Agregar datos a mongoDB
		this.reactiveMongoTemplate.dropCollection("users").subscribe();
		this.reactiveMongoTemplate.dropCollection("profiles").subscribe();
		this.reactiveMongoTemplate.dropCollection("roles").subscribe();

		RoleDocument role1 = new RoleDocument(null,"ROLE_MANAGER");
		RoleDocument role2 = new RoleDocument(null,"ROLE_USER");

		UserDocument user1;
		UserDocument user2;
		UserDocument user3;

		//-----------------------------------------------------------------------------------------------------------------------

		Lenguage lenguage1 = new Lenguage("Español",true);

		//-----------------------------------------------------------------------------------------------------------------------

		Employment employment1 = new Employment("Real Madrid FC","Jugador",
				"Madrid","una ciudad de España",true);

		Employment employment2 = new Employment("Manchester United","Jugador",
				"Manchester","una ciudad de Inglaterra",true);

		//-----------------------------------------------------------------------------------------------------------------------

		Home home1 = new Home("Madrid",new Date(),false);
		Home home2 = new Home("Machester",new Date(),true);

		//-------------------------------------------------------------------------------------------------------------------------

		ContactInformation contactInformation1 = new ContactInformation("+5699889797","rodrigo@gmail.com",
				null,null,null, Arrays.asList(lenguage1),"masculino", new Date());

		ContactInformation contactInformation2 = new ContactInformation("+5699849317","cristiano@gmail.com",
				Arrays.asList(home1,home2),Arrays.asList(employment1,employment2),null,
				Arrays.asList(lenguage1),"masculino", new Date());

		//-----------------------------------------------------------------------------------------------------

		Flux.just(role1,role2)
						.flatMap(roleRepository::save)
								.doOnNext(r -> logger.info("Se creo el rol: " + r.getName() + ", con el id: " + r.getId()))
										.thenMany(Flux.just(user1 = new UserDocument(null,"rodrigo@gmail.com",this.passwordEncoder.encode("123")
																,Arrays.asList(role1,role2),true,true,new Date(),null),
																user2 = new UserDocument(null,"cristiano@gmail.com",this.passwordEncoder.encode("123")
																		,Collections.singletonList(role2),true,true,new Date(),null),
																user3 = new UserDocument(null,"leonel@gmail.com",this.passwordEncoder.encode("123")
																		,Collections.singletonList(role2),true,true,new Date(),null)
														)
														.flatMap(userRepository::save)
														.doOnNext(u -> {
															logger.info("Se crea el usuario " + u.getEmail() + ", con el id: " + u.getId() + ", Password: " + u.getPassword());
														}).thenMany(
																Flux.just(
																		new ProfileDocument(null,"Rodrigo","Lazo",contactInformation1,user1,
																				Collections.emptyList(),Collections.emptyList(),Collections.emptyList(),Collections.emptyList(),new Date()),
																		new ProfileDocument(null,"Cristiano","Ronaldo",contactInformation2,user2,
																				Collections.emptyList(),Collections.emptyList(),Collections.emptyList(),Collections.emptyList(),new Date()),
																		new ProfileDocument(null,"Leonel","Messi",null,user3,
																				Collections.emptyList(),Collections.emptyList(),Collections.emptyList(),Collections.emptyList(),new Date())
																).flatMap(profileRepository::save))).subscribe();
	}
}
