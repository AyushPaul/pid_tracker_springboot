package com.ayush.pidtracker;

import com.ayush.pidtracker.entity.ImageData;
import com.ayush.pidtracker.service.JwtService;
import com.ayush.pidtracker.service.StorageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.zip.DataFormatException;

@SpringBootApplication
@RestController
@RequestMapping("/image")
@CrossOrigin
@EnableScheduling
public class PidtrackerApplication {

	@Autowired
	private StorageService service;

	@Autowired
	private JwtService jwtService;

	@PostMapping
	public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file , @RequestParam("comment") String comment, @RequestParam("pass") String pass, @RequestParam("token") String token) throws IOException, ParseException {
//		System.console().printf(file.getOriginalFilename());
//		System.console().printf(file.getContentType());
		//String authHeader = request.getHeader("Authorization");
		//String token = authHeader.substring(7);
		String username = jwtService.extractUsername(token);
		String id = jwtService.extractId(token);
		String uploadImage = service.uploadImage(file,comment,pass,username);
		return ResponseEntity.status(HttpStatus.OK)
				.body(uploadImage);
	}

	@GetMapping("/{fileName}")
	public ResponseEntity<?> downloadImage(@PathVariable String fileName) throws DataFormatException, IOException {
		byte[] imageData=service.downloadImage(fileName);
		//System.console().printf(fileName);
		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.valueOf("image/png"))
				.body(imageData);

	}

	@GetMapping("/allFiles")
	public List<ImageData> getAllFiles(){
		List<ImageData> files = service.getAllFiles();
		return files;
		//return ResponseEntity.status(HttpStatus.OK).body(files);
	}

//	@Bean
//	public WebMvcConfigurer corsConfigurer() {
//		return new WebMvcConfigurer() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/**")
//
//
//						.allowedHeaders("file","comment","pass","Access-Control-Allow-Headers")
//						.exposedHeaders("Content-Disposition");
//			}
//		};
//	}

	public static void main(String[] args) {
		SpringApplication.run(PidtrackerApplication.class, args);
	}

}
