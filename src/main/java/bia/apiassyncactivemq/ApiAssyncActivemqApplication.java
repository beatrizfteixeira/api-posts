package bia.apiassyncactivemq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.jms.annotation.EnableJms;


@EnableFeignClients
@SpringBootApplication
@EnableJms
public class ApiAssyncActivemqApplication  {

	public static void main(String[] args) {
		SpringApplication.run(ApiAssyncActivemqApplication.class, args);
	}
}
