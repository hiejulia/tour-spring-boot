package com.example.ec.project.web;

import com.example.ec.project.domain.Tour;
import com.example.ec.project.domain.TourRating;
import com.example.ec.project.domain.TourRatingPk;
import com.example.ec.project.repo.TourRatingRepository;
import com.example.ec.project.repo.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.AbstractMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

/**
 * TOUR RATING CONTROLLER
 */


@RestController
@RequestMapping(path = "/tours/{tourId}/ratings")
public class TourRatingController {

    //DI
    TourRatingRepository tourRatingRepository;
    TourRepository tourRepository;

    @Autowired
    public TourRatingController(TourRatingRepository tourRatingRepository, TourRepository tourRepository) {
        this.tourRatingRepository = tourRatingRepository;
        this.tourRepository = tourRepository;
    }
    protected TourRatingController() {

    }

    /**
     * Create a Tour Rating.
     *
     * @param tourId
     * @param ratingDto
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createTourRating(@PathVariable(value = "tourId") int tourId, @RequestBody @Validated RatingDto ratingDto) {
        Tour tour = verifyTour(tourId);
        tourRatingRepository.save(new TourRating( new TourRatingPk(tour, ratingDto.getCustomerId()),
                ratingDto.getScore(), ratingDto.getComment()));
    }

    /**
     * Lookup a the Ratings for a tour.
     *
     * @param tourId
     * @param pageable
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Page<RatingDto> getAllRatingsForTour(@PathVariable(value = "tourId") int tourId, Pageable pageable) {
        Tour tour = verifyTour(tourId);
        Page<TourRating> tourRatingPage = tourRatingRepository.findByPkTourId(tour.getId(), pageable);
        List<RatingDto> ratingDtoList = tourRatingPage.getContent().stream().map(tourRating -> toDto(tourRating)).collect(Collectors.toList());
        return new PageImpl<RatingDto>(ratingDtoList, pageable, tourRatingPage.getTotalPages());
    }



    /**
     * Calculate the average Score of a Tour.
     *
     * @param tourId
     * @return Tuple of "average" and the average value.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/average")
    public AbstractMap.SimpleEntry<String, Double> getAverage(@PathVariable(value = "tourId") int tourId) {
        Tour tour = verifyTour(tourId);
        List<TourRating> ratings = tourRatingRepository.findByPkTourId(tourId);
        OptionalDouble average = ratings.stream().mapToInt(TourRating::getScore).average();
        double result = average.isPresent() ? average.getAsDouble():null;
        return new AbstractMap.SimpleEntry<String, Double>("average", result);
    }


    /**
     * UPDATE SCORE AND COMMENT OF A TOUR RATING
     * @param tourId, ratingDto
     * @return the modified rating dto
     */

    @RequestMapping(method = RequestMethod.PUT)
    public RatingDto updateWithPut(@PathVariable(value = "tourId") int tourId, @RequestBody @Validated RatingDto ratingDto) {
        TourRating rating = verifyTourRating(tourId, ratingDto.getCustomerId());
        rating.setScore(ratingDto.getScore());
        rating.setComment(ratingDto.getComment());
        return toDto(tourRatingRepository.save(rating));
    }










    /**
     * UPDATE SCORE OR COMMENT OF A TOUR RATING
     * @param : tourId, ratingDto
     * @return THe modified Rating DTO
     */
    @RequestMapping(method = RequestMethod.PATCH)
    public RatingDto updateWithPatch(@PathVariable(value = "tourId") int tourId,@RequestBody @Validated RatingDto ratingDto) {
        TourRating rating = verifyTourRating(tourId, ratingDto.getCustomerId());
        if (ratingDto.getScore() != null) {
            rating.setScore(ratingDto.getScore());
        }
        if (ratingDto.getComment() != null) {
            rating.setComment(ratingDto.getComment());
        }
        return toDto(tourRatingRepository.save(rating));
    }









    /**
     * DELETE A RATING OF A TOUR MADE BY A CUSTOMER
     * @param : tourId, customerId
     */
    @RequestMapping(method = RequestMethod.DELETE,path="/{customerId}")
    private void delete(@PathVariable(value = "tourId") int tourId,@PathVariable(value = "customerId") int customerId) {
        TourRating rating = verifyTourRating(tourId, customerId);
        tourRatingRepository.delete(rating);
    }










    /**
     * CONVERT THE TOURRATING ENTITY TO A RATINGDTO
     * @param tourRating
     * @return RatingDto
     */
    private RatingDto toDto(TourRating tourRating){
        return new RatingDto(tourRating.getScore(),tourRating.getComment(),tourRating.getPk().getCustomerId());
    }
















    /**
     * VERIFY AND RETURN THE TOUR RATING
     * @params: tourId, customerId
     * return the found TourRating
     * @thows error if no tourrating found
     */

    private TourRating verifyTourRating(int tourId, int customerId) throws NoSuchElementException {
        TourRating rating = tourRatingRepository.findByPkTourIdAndPkCustomerId(tourId, customerId);
        if(rating == null){
            throw new NoSuchElementException("Tour rating pair for request(" + tourId+" for customer "+customerId);
        }
        return rating;
    }









    /**
     * VERIFY AND RETURN THE TOUR
     * PARAM: tourId
     *
     * @param: tourId
     * @return the found Tour object
     * @throws NoSuchElementException if no tour found
     */


    private Tour verifyTour(int tourId) throws NoSuchElementException {
        Tour tour =tourRepository.findOne(tourId);
        if(tour == null){
            throw new NoSuchElementException("Tour does not exist " +tourId );
        }
        return tour;
    }









    /**
     * EXCEPTION HANDLER IF NO SUCH ELEMENTEXCEPTION IS THROWN IN THIS CONTROLLER
     * param: ex
     * return Error message String
     */

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String return400(NoSuchElementException ex) {
        return ex.getMessage();

    }



}

//pageable.



