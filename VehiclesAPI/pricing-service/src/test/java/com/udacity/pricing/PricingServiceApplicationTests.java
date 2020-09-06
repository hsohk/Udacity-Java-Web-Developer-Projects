package com.udacity.pricing;

import com.udacity.pricing.api.PricingController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class PricingServiceApplicationTests {
	@Autowired
	private MockMvc mvc;

	@Test
	public void contextLoads() {
	}

	@Test
	public void getPrice() throws Exception{
		String jsonResult = mvc.perform(get("/services/price?vehicleId=1")).andReturn().getResponse().getContentAsString();
		mvc.perform(get("/services/price?vehicleId=1"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.vehicleId").value(1))
				.andExpect(status().isOk());
	}

}
