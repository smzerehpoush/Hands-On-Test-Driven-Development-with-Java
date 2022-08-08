package com.tdd.productsupport.feedback;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FeedbackApplicationTests {

    @Test
    void testContextLoads() {
        assertThat("Mahdiyar").startsWith("M");
    }

}
