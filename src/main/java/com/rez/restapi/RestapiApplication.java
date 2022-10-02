package com.rez.restapi;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.gson.Gson;

@SpringBootApplication
public class RestapiApplication {

	public static void main(String[] args)  throws Exception{
		Transcript transcript = new Transcript();
		transcript.setAudio_url("https://github.com/johnmarty3/JavaAPITutorial/blob/main/Thirsty.mp4?raw=true");
		Gson gson = new Gson();
		String json = gson.toJson(transcript);
		
		System.out.println(json);

		//Post Request 
		HttpRequest postRequest = HttpRequest.newBuilder()
		.uri(new URI("https://api.assemblyai.com/v2/transcript"))
		.header("Authorization",Constants.API_KEY)
		.POST(BodyPublishers.ofString(json))
		.build();


		HttpClient httpClient =HttpClient.newHttpClient();
		HttpResponse<String> postResponse = httpClient.send(postRequest,BodyHandlers.ofString());
		String body = postResponse.body();
		System.out.println(body);
		transcript = gson.fromJson(body, Transcript.class);

		//Get request
		HttpRequest getRequest = HttpRequest.newBuilder()
		.uri(new URI("https://api.assemblyai.com/v2/transcript/"+transcript.getId()))
		.header("Authorization",Constants.API_KEY)
		.build();
		while(true){
		
		HttpResponse<String> getResponse = httpClient.send(getRequest, BodyHandlers.ofString());
		 transcript =gson.fromJson(getResponse.body(),Transcript.class);

		 System.out.println(transcript.getStatus());
		 if("completed".equals(transcript.getStatus()) || "error".equals(transcript.getStatus())){
			break;
		 }
			Thread.sleep(1000);
		 }
		 System.out.println("Transcription completed");
			System.out.println(transcript.getText());



		}

		






	
	}
