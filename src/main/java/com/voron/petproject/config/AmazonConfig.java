package com.voron.petproject.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AmazonConfig {

	@Value("${amazon.secret.key}")
	private String AMAZON_SECRET_KEY;
	
	@Value("${amazan.access.key}")
	private String AMAZON_ACCESS_KEY;

	@Bean
	public AmazonS3 s3() {
		final BasicAWSCredentials awsCredentials = new BasicAWSCredentials(AMAZON_ACCESS_KEY, AMAZON_SECRET_KEY);
		
		AmazonS3 s3Client = AmazonS3ClientBuilder
							.standard()
							.withRegion(Regions.EU_WEST_1)
							.withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
							.build();
		return s3Client;
	};
}
