package com.oliver.eshop;

import com.oliver.eshop.h2.order.repository.OrderJpaRepository;
import com.oliver.eshop.h2.product.repository.ProductJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AbstractIT {

    @Autowired
    protected TestRestTemplate restTemplate;
    @Autowired
    protected ProductJpaRepository productJpaRepository;
    @Autowired
    protected OrderJpaRepository orderJpaRepository;

}
