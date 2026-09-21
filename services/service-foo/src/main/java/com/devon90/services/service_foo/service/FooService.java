package com.devon90.services.service_foo.service;

import org.springframework.stereotype.Service;
import com.devon90.services.service_foo.repository.FooRepository;
import com.devon90.services.service_foo.entity.Foo;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service 
public class FooService {

    private static final Logger logger = LoggerFactory.getLogger(FooService.class);

    private final FooRepository fooRepository;

    public FooService(FooRepository fooRepository) {
        this.fooRepository = fooRepository;
    }

    public List<Foo> findAll() {
        logger.info("Fetching all Foo entities. This is test log");
        return fooRepository.findAll();
    }

    public Foo create(Foo foo) {
        logger.info("Creating a new Foo entity: {}", foo);
        return fooRepository.save(foo);
    }
}
