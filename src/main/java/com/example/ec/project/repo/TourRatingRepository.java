package com.example.ec.project.repo;


import com.example.ec.project.domain.TourRating;
import com.example.ec.project.domain.TourRatingPk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@RepositoryRestResource(exported = false)
public interface TourRatingRepository extends CrudRepository<TourRating, TourRatingPk>{
    /**
     * look up all the tour ratings for a tour
     * tourId: tour id
     * return a list of tourRatings
     */

    List<TourRating> findByPkTourId(Integer tourId);

    /**
     * look up a page of TourRatings for a tour
     * tourId
     * pageable details
     * return a page of tour ratings
     *
     */

    Page<TourRating> findByPkTourId(Integer tourId, Pageable pageable);

    /**
     * look up a tour rating by the tourId and customerId
     * params : tourId, customerId
     * return TourRating or null
     */
    TourRating findByPkTourIdAndPkCustomerId(Integer tourId, Integer customerId);





}
