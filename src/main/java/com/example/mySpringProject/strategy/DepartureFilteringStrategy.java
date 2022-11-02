package com.example.mySpringProject.strategy;

import com.example.mySpringProject.models.DepartureModel;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DepartureFilteringStrategy {


    public List<DepartureModel> filterDepartures(final List<DepartureModel> alvikTrains, final List<DepartureModel> storaMossenTrains) {
        // skip first Alvik departure - take next - should I remove the departure?
        alvikTrains.remove(0);

        // compare Alvik departure time with stora mossen departure times
        return alvikTrains.stream()
                .filter(dep -> compareDepartureTimes(dep, storaMossenTrains))
                .collect(Collectors.toList());
    }

    private boolean compareDepartureTimes(final DepartureModel dep, List<DepartureModel> storaMossenTrains) {
        boolean isDepartingFromAlvik = true;
        for (DepartureModel storaMossenDep : storaMossenTrains) {
            int i = 1; // minutes 1,2,3
            while (i < 4) {
                long l = i;
                if (dep.getDepartureTime().minusMinutes(l).compareTo(storaMossenDep.getDepartureTime()) == 0) {
                    // if there is a departure time matching it means the train did not start from Alvik
                    isDepartingFromAlvik = false;
                }
                i++;
            }
            // if no departures from stora mossen match the alvik departure we return true - this is the departure we want

        }
        return isDepartingFromAlvik;
    }
}
