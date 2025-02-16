package com.github.frankiie.springboot.domain.collection.repository.springdata;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.github.frankiie.springboot.domain.collection.entity.Collection;
import com.github.frankiie.springboot.domain.course.entity.Course;
import com.github.frankiie.springboot.domain.management.repository.SoftDeleteRepository;

import java.util.Optional;

@Repository
public interface SpringDataCollectionRepository extends SoftDeleteRepository<Collection> {

    @Override
    @Transactional
    @Modifying
    // @Query(DELETE_BY_ID)
    void deleteById(Long id);

    @Override
    @Transactional
    default void delete(Collection user) {
        deleteById(user.getId());
    }

    @Override
    @Transactional
    default void deleteAll(Iterable<? extends Collection> entities) {
        entities.forEach(entity -> deleteById(entity.getId()));
    }

    // Boolean existsByEmail(String email);

    // @Query("""
    //     select user from #{#entityName} user
    //     left join fetch user.roles
    //     where user.id = ?1 
    // """)
    // Optional<Course> findByIdFetchRoles(Long id);

}