package com.devon90.services.service_foo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.devon90.services.service_foo.entity.Foo;


public interface FooRepository extends JpaRepository<Foo, Long> {
    
}
