package com.jinx.springbootinit.bizmq;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


@SpringBootTest
class BiConsumerTest {

    @Resource
    private BiProvider biProvider;
    @Test
    public  void testConsumer() {
        biProvider.sendMessage("");
    }

}