package com.example.ec.project.services;


import com.example.ec.project.domain.Difficulty;
import com.example.ec.project.domain.Region;
import com.example.ec.project.domain.Tour;
import com.example.ec.project.domain.TourPackage;
import com.example.ec.project.repo.TourPackageRepository;
import com.example.ec.project.repo.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

/**
 * TOUR SERVICE CLASS
 */
@Service
public class TourService {
    private TourPackageRepository tourPackageRepository;
    private TourRepository tourRepository;

    @Autowired
    public TourService(TourPackageRepository tourPackageRepository, TourRepository tourRepository) {
        this.tourPackageRepository = tourPackageRepository;
        this.tourRepository = tourRepository;
    }

    /**
     * CREATE A NEW TOUR OBJECT AND PERSIST IT TO THE DATABASE
     *
     * @param title
     * @param description
     * @param blurb
     * @param price
     * @param duration
     * @param bullets
     * @param keywords
     * @param tourPackageName
     * @param difficulty
     * @param region
     * @return TOUR
     */



    public Tour createTour(String title, String description, String blurb, Integer price,
                           String duration, String bullets,
                           String keywords, String tourPackageName, Difficulty difficulty, Region region ) {
        TourPackage tourPackage = tourPackageRepository.findByName(tourPackageName);
        if (tourPackage == null) {
            throw new RuntimeException("Tour package does not exist: " + tourPackageName);
        }
        return tourRepository.save(new Tour(id,title, description,blurb, price, duration,
                bullets, keywords, tourPackage, difficulty, region));
    }



    /**
     * CALCULATE THE NUMBER OF TOURS IN THE DATABASE
     * @return
     */

    public long total(){

        return tourRepository.count();
    }

    public Iterable<Tour> lookup(){
        return tourRepository.findAll();
    }
}
