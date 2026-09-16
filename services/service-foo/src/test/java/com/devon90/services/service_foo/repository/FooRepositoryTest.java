package com.devon90.services.service_foo.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.devon90.services.service_foo.entity.Foo;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class FooRepositoryTest {
    
    @Autowired
    private FooRepository fooRepository;

    @Test
    void shouldSaveAndFindFoo() {
        Foo foo = new Foo();
        foo.setName("Repository Test");

        Foo savedFoo = fooRepository.save(foo);

        assertThat(savedFoo.getId()).isNotNull();
        assertThat(savedFoo.getName()).isEqualTo("Repository Test");

        Foo foundFoo = fooRepository.findById(savedFoo.getId()).orElseThrow();

        assertThat(foundFoo.getName()).isEqualTo("Repository Test");
    } 
}
