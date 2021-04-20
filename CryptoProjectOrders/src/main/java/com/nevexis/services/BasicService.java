package com.nevexis.services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;

@Service
public class BasicService {
	@PersistenceContext
	protected EntityManager em;
}