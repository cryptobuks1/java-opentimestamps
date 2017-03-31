package com.eternitywall.http;

import com.eternitywall.http.Request;
import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertNotNull;

/**
 * Created by casatta on 28/02/17.
 */
public class TestHttps {

    @Test
    public void open() throws Exception {
        Request request = new Request(new URL("https://api.eternitywall.com"));
        String string = request.call().getString();
        assertNotNull(string);
        Request request2 = new Request(new URL("https://ots.eternitywall.it"));
        String string1 = request2.call().getString();
        assertNotNull(string1);
    }
}
