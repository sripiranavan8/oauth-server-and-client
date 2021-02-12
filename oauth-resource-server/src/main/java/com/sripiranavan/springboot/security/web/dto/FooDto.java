package com.sripiranavan.springboot.security.web.dto;

public class FooDto {
	private Long id;
	private String name;

	public FooDto() {
		super();
	}

	public FooDto(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
