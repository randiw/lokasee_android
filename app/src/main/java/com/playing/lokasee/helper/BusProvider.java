package com.playing.lokasee.helper;

import com.squareup.otto.Bus;

/**
 * Created by mexan on 8/21/15.
 */
public class BusProvider {

    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }

}
