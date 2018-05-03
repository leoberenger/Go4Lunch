package com.openclassrooms.go4lunch.managers;

import com.openclassrooms.go4lunch.models.User;
import com.openclassrooms.go4lunch.models.googlemaps.PlacesAPI;

import java.util.List;

public class WorkmatesMgr {

    private static final WorkmatesMgr ourInstance = new WorkmatesMgr();

    private List<User> workmates;

    public static WorkmatesMgr getInstance() {
        return ourInstance;
    }

    private WorkmatesMgr() {
    }

    public List<User> getWorkmates() {
        return workmates;
    }

    public void setWorkmates(List<User> workmates) {
        this.workmates = workmates;
    }

    public int getNbWorkmatesGoing(String placeId){
        int nbWorkmates = 0;

        for(int i = 0; i<workmates.size(); i++){
            if ((workmates.get(i).getSelectedRestoId() != null) && (workmates.get(i).getSelectedRestoId().equals(placeId))) {
                nbWorkmates++;
            }
        }

        return nbWorkmates;
    }

}
