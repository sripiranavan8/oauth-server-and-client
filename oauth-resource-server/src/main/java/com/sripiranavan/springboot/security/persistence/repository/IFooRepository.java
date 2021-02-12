package com.sripiranavan.springboot.security.persistence.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.sripiranavan.springboot.security.persistence.model.Foo;

public interface IFooRepository extends PagingAndSortingRepository<Foo, Long> {

}
