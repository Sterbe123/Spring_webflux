package cl.sterbe.app;

import cl.sterbe.app.documents.dao.users.RoleDAO;
import cl.sterbe.app.documents.entity.profiles.ContactInformation;
import cl.sterbe.app.documents.entity.profiles.Employment;
import cl.sterbe.app.documents.entity.profiles.Home;
import cl.sterbe.app.documents.entity.profiles.Lenguage;
import cl.sterbe.app.documents.models.profiles.*;
import cl.sterbe.app.documents.models.users.Role;
import cl.sterbe.app.documents.models.users.User;
import cl.sterbe.app.services.profiles.ProfileService;
import cl.sterbe.app.services.users.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.Date;

@SpringBootApplication
public class SpringWebfluxApiApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringWebfluxApiApplication.class, args);
	}

	//vamos a ingresar datos **********************

	@Autowired
	private UserService userService;

	@Autowired
	private ProfileService profileService;

	@Autowired
	private RoleDAO roleDAO;

	@Autowired
	private ReactiveMongoTemplate reactiveMongoTemplate;

	private Logger logger = LoggerFactory.getLogger(SpringWebfluxApiApplication.class);

	@Override
	public void run(String... args) throws Exception {

		//Agregar datos a mongoDB
		this.reactiveMongoTemplate.dropCollection("users").subscribe();
		this.reactiveMongoTemplate.dropCollection("profiles").subscribe();
		this.reactiveMongoTemplate.dropCollection("roles").subscribe();

		Role role1 = new Role(null,"ROLE_MANAGER");
		Role role2 = new Role(null,"ROLE_CLIENT");

		User user1;
		User user2;
		User user3;

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
						.flatMap(roleDAO::save)
								.doOnNext(r -> logger.info("Se creo el rol: " + r.getRole() + ", con el id: " + r.getId()))
										.thenMany(Flux.just(user1 = new User(null,"rodrigo@gmail.com","123",Arrays.asList(role1),true,true,new Date(),null),
																user2 = new User(null,"critiano@gmail.com","123",Arrays.asList(role2),true,true,new Date(),null),
																user3 = new User(null,"leonel@gmail.com","123",Arrays.asList(role2),true,true,new Date(),null)
														)
														.flatMap(userService::save)
														.doOnNext(u -> {
															logger.info("Se crea el usuario " + u.getEmail() + ", con el id: " + u.getId());
														}).thenMany(
																Flux.just(
																		new Profile(null,"Rodrigo","Lazo",contactInformation1,user1,
																				null,null,null,null,new Date()),
																		new Profile(null,"Cristiano","Ronaldo",contactInformation2,user2,
																				null,null,null,null,new Date()),
																		new Profile(null,"Leonel","Messi",null,user3,
																				null,null,null,null,new Date())
																).flatMap(profileService::save))).subscribe();
	}
}
