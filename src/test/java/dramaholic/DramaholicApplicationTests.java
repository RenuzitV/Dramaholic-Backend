package dramaholic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import dramaholic.customer.Customer;
import dramaholic.customer.CustomerService;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@AutoConfigureMockMvc
class DramaholicApplicationTests {

	@Autowired
	private MockMvc mvc;
	private final Gson gson =
			new GsonBuilder().registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (date, type, jsonDeserializationContext) -> {

				return new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE)); // "yyyy-mm-dd"

			}).create();

	@Test
	public void testGetCustomers() throws Exception{
		mvc.perform(get("/api/customers"))
				.andExpect(status().isOk());
	}

	public Long testLoginCustomer(String username, String password) throws Exception {
		//make customer
		Customer customer = new Customer();
		customer.setUsername(username);
		customer.setPassword(password);

		//get json of customer
		String jsonString = gson.toJson(customer);

		//try login to see if we had the customer before
		MvcResult content = mvc.perform(post("/api/customers/login")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(jsonString)).andReturn();
		content.getResponse().getContentAsString();

		String res = content.getResponse().getContentAsString();
		if (NumberUtils.isParsable(res)) {
			return Long.parseLong(res);
		}
		return null;
	}

	public String testDeleteCustomer(String username, String password) throws Exception{
		Long id = testLoginCustomer(username, password);
		return testDeleteCustomer(id);
	}

	public String testDeleteCustomer(Long id) throws Exception{
		MvcResult content = mvc.perform(delete("/api/customers/"+id))
				.andExpect(status().isOk())
				.andReturn();

		return content.getResponse().getContentAsString();
	}


	@Test
	public void testCustomer() throws Exception{

		//make customer
		Customer customer = new Customer();
		customer.setName("Duy Nguyen");
		customer.setUsername("testcustomer123321");
		customer.setPassword("123321");
		customer.setDob(LocalDate.of(2022, 1, 22));
		customer.setEmail("asd@gmail.com");

		Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (date, type, jsonDeserializationContext) -> {

			return new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE)); // "yyyy-mm-dd"

		}).create();

		//get json of customer
		String jsonString = gson.toJson(customer);

		//tries to login, if possible delete that account
		Long id = testLoginCustomer(customer.getUsername(), customer.getPassword());
		if (id != null) {
			testDeleteCustomer(customer.getUsername(), customer.getPassword());
		}

		MvcResult result = mvc.perform(post("/api/customers")
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(jsonString))
				.andExpect(status().isCreated())
				.andReturn();

		String res = result.getResponse().getContentAsString();

		assert NumberUtils.isParsable(res);

		id = Long.parseLong(res);

		assert testDeleteCustomer(id).equals("Deleted");
	}
}
