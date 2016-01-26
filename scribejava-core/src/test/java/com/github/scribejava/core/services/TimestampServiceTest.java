package com.github.scribejava.core.services;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class TimestampServiceTest {

    private TimestampServiceImpl service;

    @Before
    public void setUp() {
        service = new TimestampServiceImpl();
        service.setTimer(new TimerStub());
    }

    @Test
    public void shouldReturnTimestampInSeconds() {
        final String expected = "1000";
        assertEquals(expected, service.getTimestampInSeconds());
    }

    @Test
    public void shouldReturnNonce() {
        final String expected = "1042";
        assertEquals(expected, service.getNonce());
    }

    private static class TimerStub extends TimestampServiceImpl.Timer {

        @Override
        public Long getMilis() {
            return 1000000L;
        }

        @Override
        public Integer getRandomInteger() {
            return 42;
        }
    }
}
