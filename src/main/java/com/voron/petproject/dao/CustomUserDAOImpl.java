package com.voron.petproject.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.hibernate.Session;

import com.voron.petproject.entity.UserEntity;

@Repository
public class CustomUserDAOImpl implements CustomUserDAO {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	@Transactional
	public Optional<UserEntity> findByUsername(String username) {
		System.out.println("\n\n==========>>>> CustomUserDAOImpl findByUsername" + username);
		Session currentSession = em.unwrap(Session.class);
		
		
		Query<UserEntity> theQuery = currentSession.createQuery("FROM UserEntity U WHERE U.username =:username", UserEntity.class);
		theQuery.setParameter("username", username);
		
		UserEntity user = theQuery.getSingleResult();
		System.out.println(user);

		return Optional.ofNullable(user);
	}

}
