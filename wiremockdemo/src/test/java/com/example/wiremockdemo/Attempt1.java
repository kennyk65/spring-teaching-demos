package com.example.wiremockdemo;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;

import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;




// This is based on the wiremock documentation at https://wiremock.org/docs/junit-jupiter/
// When using maciejwalkowiak 2.0 It fails with java.lang.IncompatibleClassChangeError: class org.eclipse.jetty.http2.server.HttpChannelOverHTTP2 has interface org.eclipse.jetty.server.HttpChannel as super class
// When using wiremock/3.3.1      it fails with java.lang.IncompatibleClassChangeError: class org.eclipse.jetty.http2.server.HttpChannelOverHTTP2 has interface org.eclipse.jetty.server.HttpChannel as super class


@WireMockTest
public class Attempt1 {

    @Test
    void test_something_with_wiremock(WireMockRuntimeInfo wmRuntimeInfo) {
        // The static DSL will be automatically configured for you
        stubFor(get("/static-dsl").willReturn(ok()));

        // Instance DSL can be obtained from the runtime info parameter
        WireMock wireMock = wmRuntimeInfo.getWireMock();
        wireMock.register(get("/instance-dsl").willReturn(ok()));

        // Info such as port numbers is also available
        int port = wmRuntimeInfo.getHttpPort();

        // Do some testing...
    }
    
}
