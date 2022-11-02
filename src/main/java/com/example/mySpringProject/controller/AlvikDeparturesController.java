package com.example.mySpringProject.controller;

import com.example.mySpringProject.models.DepartureModel;
import com.example.mySpringProject.service.DepartureFetchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AlvikDeparturesController {

    @Autowired
    DepartureFetchingService departureFetchingService;

    @GetMapping("/comingDeparturesFromAlvik")
    public List<DepartureModel> helloWorld() {
        return departureFetchingService.getTheNextEmptyTrain();
    }

    @GetMapping("/catFact")
    public String getCatFact() {
        return departureFetchingService.getCatFactFromApi();
    }


}


