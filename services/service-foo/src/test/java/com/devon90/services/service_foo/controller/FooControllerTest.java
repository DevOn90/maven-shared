package com.devon90.services.service_foo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.devon90.services.service_foo.service.FooService;
import com.devon90.services.service_foo.entity.Foo;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FooController.class)
public class FooControllerTest {
    
    @Autowired 
    private MockMvc mockMvc;

    @MockitoBean 
    private FooService fooService;

    @Test 
    void shouldReturnFoos() throws Exception {
        Foo foo = new Foo();
        foo.setName("Test Name");

        when(fooService.findAll()).thenReturn(List.of(foo));

        mockMvc.perform(get("/v1/api/foo"))
            .andExpect(status().isOk())
            .andExpect(content().json("""
                    [
                        {
                            "name": "Test Name"
                        }
                    ]
                    """));

    }

    @Test
    void shouldCreateFoo() throws Exception {
        Foo foo = new Foo();
        foo.setName("Created Foo");

        when(fooService.create(any(Foo.class)))
                .thenReturn(foo);

        mockMvc.perform(post("/v1/api/foo")
                .contentType("application/json")
                .content("""
                        {
                            "name": "Created Foo"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "name": "Created Foo"
                        }
                        """));
    }
}
