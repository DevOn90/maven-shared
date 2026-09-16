package com.devon90.services.service_foo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import com.devon90.services.service_foo.entity.Foo;
import java.util.List;
import com.devon90.services.service_foo.service.FooService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController 
@RequestMapping("/v1/api/foo")
public class FooController {
    
    private final FooService fooService;

    public FooController(FooService fooService) {
        this.fooService = fooService;
    }

    @GetMapping
    public List<Foo> getAllFoos() {
        return fooService.findAll();
    }

    @PostMapping
    public Foo create(@RequestBody Foo foo) {
        return fooService.create(foo);
    }
    
}
