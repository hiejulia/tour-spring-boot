package com.example.ec.project.services;


import com.example.ec.project.domain.TourPackage;
import com.example.ec.project.repo.TourPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * TOUR PACKAGE SERVICE
 */


@Service
public class TourPackageService {
    private TourPackageRepository tourPackageRepository;


    @Autowired
    public TourPackageService(TourPackageRepository tourPackageRepository) {
        this.tourPackageRepository = tourPackageRepository;
    }

    //create one
    public TourPackage createTourPackage(String code, String name){
        if(!tourPackageRepository.exists(code)) {
            tourPackageRepository.save(new TourPackage(code, name));
        }
        return null;
    }

    public Iterable<TourPackage> lookup(){
        return tourPackageRepository.findAll();
    }


    public long total(){
        return tourPackageRepository.count();
    }


    ///get override method
}
