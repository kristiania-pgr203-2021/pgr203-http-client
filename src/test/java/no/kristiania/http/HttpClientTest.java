package no.kristiania.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpClientTest {
    
    @Test
    void dummyTest() {
        assertEquals(42, 6*7);
    }
    
    @Test
    void shouldReturnStatusCode() {
        HttpClient client = new HttpClient("httpbin.org", 80, "/html");
        assertEquals(200, client.getStatusCode());
    }

    @Test
    void shouldReturn404StatusCode() {
        HttpClient client = new HttpClient("httpbin.org", 80, "/this-page-does-not-exist");
        assertEquals(404, client.getStatusCode());
    }

}