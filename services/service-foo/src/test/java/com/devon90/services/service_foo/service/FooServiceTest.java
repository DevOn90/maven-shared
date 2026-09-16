package com.devon90.services.service_foo.service;

import com.devon90.services.service_foo.entity.Foo;
import com.devon90.services.service_foo.repository.FooRepository;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FooServiceTest {

    @Mock
    private FooRepository fooRepository;

    @InjectMocks
    private FooService fooService;

    @Test
    void shouldFindAllFoos() {
        Foo foo = new Foo();
        foo.setName("Example Foo");

        when(fooRepository.findAll()).thenReturn(List.of(foo));

        List<Foo> result = fooService.findAll();

        assertThat(result).containsExactly(foo);
        verify(fooRepository).findAll();
    }

    @Test
    void shouldCreateFoo() {
        Foo foo = new Foo();
        foo.setName("Example Foo");

        when(fooRepository.save(foo)).thenReturn(foo);

        Foo result = fooService.create(foo);

        assertThat(result).isSameAs(foo);
        verify(fooRepository).save(foo);
    }
}
