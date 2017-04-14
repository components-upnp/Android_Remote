package com.components.createch.androidremote;

import org.fourthline.cling.model.action.ActionArgumentValue;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

/**
 * Created by comkostiuk on 14/04/2017.
 */

public class TestSliderService {
    Susbcription sub;


    @Before
    public void setUp() {
        sub = new Susbcription("SliderController");
        sub.run();
        pause(7000);
        sub.executeAction("SetTarget",
                "NewTargetValue",
                19);
        pause(3000);
    }

    @Test
    public void testEvenementSetValeurValide() {
        pause(5000);
        sub.executeAction(
                "SetTarget",
                "NewTargetValue",
                21);
        pause(3000);
        assertEquals("21",this.sub.getStatus());
    }

    @Test
    public void testEvenementValeurNegative() {
        pause(5000);
        sub.executeAction("SetTarget",
                "NewTargetValue",
                -3);
        pause(3000);
        assertEquals("19",this.sub.getStatus());
    }

    @Test
    public void testEvenementValeurTropGrande() {
        pause(5000);
        sub.executeAction("SetTarget",
                "NewTargetValue",
                101);
        pause(3000);
        assertEquals("19",this.sub.getStatus());
    }

    @Test
    public void testSetValeurValide() {
        pause(5000);
        sub.executeAction("SetTarget",
                "NewTargetValue",
                23);
        pause(3000);
        ArrayList<Object> reponse = sub.executeAction("GetStatus",
                null,
                null);
        pause(2000);
        ActionArgumentValue r = (ActionArgumentValue) reponse.get(0);
        assertEquals("23",r.toString());
    }

    @Test
    public void testValeurNegative() {
        pause(5000);
        sub.executeAction("SetTarget",
                "NewTargetValue",
                -1);
        pause(3000);
        ArrayList<Object> reponse = sub.executeAction("GetStatus",
                null,
                null);
        pause(2000);
        ActionArgumentValue r = (ActionArgumentValue) reponse.get(0);
        assertEquals("19",r.toString());
    }

    @Test
    public void testValeurSupCent() {
        pause(5000);
        sub.executeAction("SetTarget",
                "NewTargetValue",
                101);
        pause(3000);
        ArrayList<Object> reponse = sub.executeAction("GetStatus",
                null,
                null);
        pause(2000);
        ActionArgumentValue r = (ActionArgumentValue) reponse.get(0);
        assertEquals("19",r.toString());
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
