package com.components.createch.androidremote;

import org.fourthline.cling.model.action.ActionArgumentValue;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

/**
 * Created by comkostiuk on 14/04/2017.
 */

public class TestButtonService {

    Susbcription sub;

    @Before
    public void setUp() {
        sub = new Susbcription("RemoteController");
        sub.run();
        pause(7000);
        sub.executeAction("SetTarget",
                "NewTargetValue",
                "AUCUN");
        pause(3000);
    }

    @Test
    public void testValInit() {
        ArrayList<Object> rep = sub.executeAction("GetStatus",
                null,
                null);
        ActionArgumentValue r = (ActionArgumentValue) rep.get(0);

        assertEquals("AUCUN",r.getArgument().toString());
    }

    //Permet de mettre l'exécution en pause, afin d'avoir le temps de recevoir les évènements
    public static void pause(long ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }
}
