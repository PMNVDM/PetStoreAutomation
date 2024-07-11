package api.test;

import org.apache.logging.log4j.*;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.UserEndPoints2;
import api.payload.User;
import groovyjarjarantlr4.v4.runtime.misc.LogManager;
import io.restassured.response.Response;

public class UserTest2 {
	User userPayload;
	Faker faker;
	
	public Logger logger;
	@BeforeClass
	public void setup() {
		faker = new Faker();
		userPayload = new User();
		
		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setUsername(faker.name().username());
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPassword(faker.internet().password());
		userPayload.setPhone(faker.phoneNumber().cellPhone());
		logger = org.apache.logging.log4j.LogManager.getLogger(this.getClass());
		logger.debug("debugging...");
	}
	@Test(priority = 1)
	public void testPostUser() {
		logger.info("***** Reading User Info *********");
		Response response = UserEndPoints2.createUser(userPayload);
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(), 200);
		logger.info("***** User Created *********");
	}
	@Test(priority = 2)
	public void testGetUserByName() {
		logger.info("***** Reading user info *********");
		Response response = UserEndPoints2.readUser(this.userPayload.getUsername());
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
		logger.info("***** User Info is displayed*********");
	}
	@Test(priority = 3)
	public void testUpdateUserByName() {
		logger.info("***** Updating user*********");
		//update data using payload
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		Response response = UserEndPoints2.updateUser(this.userPayload.getUsername(),userPayload);
		response.then().log().body();
		
		Assert.assertEquals(response.getStatusCode(), 200);
		//checking after update
		Response responseAfterUpdate = UserEndPoints2.readUser(this.userPayload.getUsername());
		Assert.assertEquals(responseAfterUpdate.getStatusCode(), 200);
		logger.info("***** User is updated*********");
	}
	@Test(priority = 4)
	public void testDeleteUser() {
		logger.info("***** Deleting user*********");
		Response response = UserEndPoints2.deleteUser(this.userPayload.getUsername());
		Assert.assertEquals(response.statusCode(), 200);
		logger.info("***** User deleted*********");
	}
}
