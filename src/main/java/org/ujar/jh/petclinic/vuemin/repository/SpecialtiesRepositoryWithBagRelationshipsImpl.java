package org.ujar.jh.petclinic.vuemin.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.ujar.jh.petclinic.vuemin.domain.Specialties;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class SpecialtiesRepositoryWithBagRelationshipsImpl implements SpecialtiesRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Specialties> fetchBagRelationships(Optional<Specialties> specialties) {
        return specialties.map(this::fetchVets);
    }

    @Override
    public Page<Specialties> fetchBagRelationships(Page<Specialties> specialties) {
        return new PageImpl<>(fetchBagRelationships(specialties.getContent()), specialties.getPageable(), specialties.getTotalElements());
    }

    @Override
    public List<Specialties> fetchBagRelationships(List<Specialties> specialties) {
        return Optional.of(specialties).map(this::fetchVets).orElse(Collections.emptyList());
    }

    Specialties fetchVets(Specialties result) {
        return entityManager
            .createQuery(
                "select specialties from Specialties specialties left join fetch specialties.vets where specialties is :specialties",
                Specialties.class
            )
            .setParameter("specialties", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Specialties> fetchVets(List<Specialties> specialties) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, specialties.size()).forEach(index -> order.put(specialties.get(index).getId(), index));
        List<Specialties> result = entityManager
            .createQuery(
                "select distinct specialties from Specialties specialties left join fetch specialties.vets where specialties in :specialties",
                Specialties.class
            )
            .setParameter("specialties", specialties)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
