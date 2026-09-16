package com.devon90.services.service_foo.service;

import org.springframework.stereotype.Service;
import com.devon90.services.service_foo.repository.FooRepository;
import com.devon90.services.service_foo.entity.Foo;
import java.util.List;

@Service 
public class FooService {
    
    private final FooRepository fooRepository;

    public FooService(FooRepository fooRepository) {
        this.fooRepository = fooRepository;
    }

    public List<Foo> findAll() {
        return fooRepository.findAll();
    }

    public Foo create(Foo foo) {
        return fooRepository.save(foo);
    }
}
