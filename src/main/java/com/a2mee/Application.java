package com.a2mee;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.LocalDate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@PropertySource("classpath:sql.properties")
@SpringBootConfiguration
@EnableAutoConfiguration
@EnableScheduling
public class Application {
	public static void main(String[] args) {
		
		SpringApplication.run(Application.class, args);
		/*InetAddress ip;
		try {
			
			
			ip = InetAddress.getLocalHost();
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);

			byte[] mac = network.getHardwareAddress();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
			}
			//System.out.println("MAC:: "+sb);
			//String licekceKey ="00-15-5D-AF-DB-0A";//Jyoti Server Mac 
			//String licekceKey ="24-41-8C-8F-BE-75"; //Local Mac 
			//if(licekceKey.equalsIgnoreCase(sb.toString())){
				LocalDate curdate = LocalDate.now();
			LocalDate hardcodedDate = LocalDate.parse("2021-04-27");
					
					
					if (hardcodedDate.compareTo(curdate)>=0){
						System.out.println("Licence Valid");
						SpringApplication.run(Application.class, args);
						
					}else{
						
						System.out.println("Licence Exprided ::Contact A2mee pune  ");
						System.exit(0);
					}
				
				
			//	SpringApplication.run(Application.class, args);


			}else{
				System.out.println("Invalid Licencce :: Please Contact Appoint 2 meee ,Pune");

				System.exit(0);
				
			}
		} catch (UnknownHostException e) {

			e.printStackTrace();

		} catch (SocketException e){

			e.printStackTrace();

		}*/

	}
}
