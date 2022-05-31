package com.in28Minutes.springboot.controller;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.JsonSerializable;
import com.in28Minutes.springboot.Application;
import com.in28Minutes.springboot.model.Question;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SurveyControllerIT {

    @LocalServerPort
    private int port;

    private TestRestTemplate template = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    private String createHttpHeaders(String userId,String password){
        HttpHeaders headers = new HttpHeaders();
        String auth = userId+ ":"+password;
        byte[] encodeAuth = Base64.encode(auth.getBytes(Charset.forName("US-ASCII")));
        String headerValue = "Basic" + new String(encodeAuth);

        return headerValue;
    }

    @Before
    public void before() {
        headers.add("Authorization", createHttpAuthenticationHeaderValue(
                "user1", "secret1"));
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    }

    private String createHttpAuthenticationHeaderValue(String userId,
                                                       String password) {

        String auth = userId + ":" + password;

        byte[] encodedAuth = Base64.encode(auth.getBytes(Charset
                .forName("US-ASCII")));

        String headerValue = "Basic " + new String(encodedAuth);

        return headerValue;
    }

    @Test
    public void retrieveSurveyQuestion() throws Exception {

        String expected = "{id:Question1,description:Largest Country in the World,correctAnswer:Russia,options:[India,Russia,United States,China]}";

        ResponseEntity<String> response = template.exchange(
                createUrl("/surveys/Survey1/questions/Question1"),
                HttpMethod.GET, new HttpEntity<String>("DUMMY_DOESNT_MATTER",
                        headers), String.class);
        System.out.println(response.getBody());
        System.out.println(expected);

        //JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    private String createUrl(String uri) {
        return "http://localhost:" + port + uri;
    }

    @Test
    public void retrieveSurveyQuestions() throws Exception {
        ResponseEntity<List<Question>> response = template.exchange(
                createUrl("/surveys/Survey1/questions/"), HttpMethod.GET,
                new HttpEntity<String>("DUMMY_DOESNT_MATTER", headers),
                new ParameterizedTypeReference<List<Question>>() {
                });

        Question sampleQuestion = new Question("Question1",
                "Largest Country in the World", "Russia", Arrays.asList(
                "India", "Russia", "United States", "China"));

        assertTrue(response.getBody().contains(sampleQuestion));
    }

    @Test
    public void createSurveyQuestion() throws Exception {
        Question question = new Question("DOESN'T MATTER", "Smallest Number",
                "1", Arrays.asList("1", "2", "3", "4"));

        ResponseEntity<String> response = template.exchange(
                createUrl("/surveys/Survey1/questions/"), HttpMethod.POST,
                new HttpEntity<Question>(question, headers), String.class);

        assertThat(response.getHeaders().get(HttpHeaders.LOCATION).get(0),
                containsString("/surveys/Survey1/questions/"));
    }

    private String createURLWithPort(final String uri) {
        return "http://localhost:" + port + uri;
    }

    @Test
    public void testRetrieveSurveyQuestion() throws JSONException {

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = template.exchange(
                createURLWithPort("/surveys/Survey1/questions/Question1"),
                HttpMethod.GET, entity, String.class);

        String expected = "{id:Question1,description:Largest Country in the World,correctAnswer:Russia}";

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

}