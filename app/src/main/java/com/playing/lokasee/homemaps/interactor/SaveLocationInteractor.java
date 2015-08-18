package com.playing.lokasee.homemaps.interactor;

import com.google.android.gms.maps.model.LatLng;
import com.playing.lokasee.model.User;

import java.util.List;

/**
 * Created by mexan on 8/18/15.
 */
public interface SaveLocationInteractor {

    void saveLocation(String userId, String name, LatLng latLng);

    void fetchLocation(List<User> users);
}
