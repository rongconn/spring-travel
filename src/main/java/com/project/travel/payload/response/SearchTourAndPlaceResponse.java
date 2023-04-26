package com.project.travel.payload.response;

import com.project.travel.models.Place;
import com.project.travel.models.Tour;

import java.io.Serializable;
import java.util.List;

public class SearchTourAndPlaceResponse implements Serializable {
    private List<Tour> tours;
    private List<Place> places;

    public SearchTourAndPlaceResponse(List<Tour> tours, List<Place> places) {
        this.tours = tours;
        this.places = places;
    }

    public List<Tour> getTours() {
        return tours;
    }

    public void setTours(List<Tour> tours) {
        this.tours = tours;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }
}
