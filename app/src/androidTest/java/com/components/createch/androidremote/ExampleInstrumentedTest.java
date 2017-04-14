package com.components.createch.androidremote;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.InstrumentationInfo;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.android.AndroidUpnpServiceImpl;
import org.fourthline.cling.controlpoint.SubscriptionCallback;
import org.fourthline.cling.mock.MockRouter;
import org.fourthline.cling.mock.MockUpnpService;
import org.fourthline.cling.model.gena.CancelReason;
import org.fourthline.cling.model.gena.GENASubscription;
import org.fourthline.cling.model.message.StreamResponseMessage;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.message.header.SubscriptionIdHeader;
import org.fourthline.cling.model.message.header.TimeoutHeader;
import org.fourthline.cling.model.message.header.UpnpHeader;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.LocalService;
import org.fourthline.cling.model.meta.StateVariable;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private Remote app;
    private LocalService<SliderController> sliderService;
    private StateVariable status;
    private SubscriptionCallback callback;
    MockUpnpService service;


    @Rule
    public final ActivityTestRule<Remote> mRemote = new ActivityTestRule<Remote>(Remote.class);

    @Before
    public void setUp() {


        app = this.mRemote.getActivity();
        this.sliderService = app.getSliderController();

        service = createMockUpnpService();
      //TODO Recuperer et ajouter le device au service!!!

        pause(10000);

        service.getRegistry().addDevice(app.getUpnpService().getRegistry().getLocalDevice(app.getSliderUdn(),true));

        callback = new SubscriptionCallback(app.getUpnpService().getRegistry().getLocalDevice(app.getSliderUdn(),true).getServices()[0],600) {
            @Override
            protected void failed(GENASubscription subscription, UpnpResponse responseStatus, Exception exception, String defaultMsg) {
                System.err.println(defaultMsg);
            }

            @Override
            protected void established(GENASubscription subscription) {
                System.out.println("Established: " + subscription.getSubscriptionId() );
            }

            @Override
            protected void ended(GENASubscription subscription, CancelReason reason, UpnpResponse responseStatus) {
                assertNull(reason);
            }

            @Override
            protected void eventReceived(GENASubscription subscription) {
                System.out.println("Event: " + subscription.getCurrentSequence().getValue());

                Map<String, StateVariable> values = subscription.getCurrentValues();
                status = values.get("Status");

                System.out.println("Status is: " + status.toString());
            }

            @Override
            protected void eventsMissed(GENASubscription subscription, int numberOfMissedEvents) {

            }
        };
        this.service.getControlPoint().execute(callback);
    }

    private MockUpnpService createMockUpnpService() {
        return new MockUpnpService() {
            @Override
            protected MockRouter createRouter() {
                return new MockRouter(getConfiguration(), getProtocolFactory()) {
                    @Override
                    public StreamResponseMessage[] getStreamResponseMessages() {
                        return new StreamResponseMessage[] {
                                createSubscribeResponseMessage(),
                                createUnsubscribeResponseMessage()
                        };
                    }
                };
            }
        };
    }

    protected StreamResponseMessage createSubscribeResponseMessage() {
        StreamResponseMessage msg  = new StreamResponseMessage(new UpnpResponse(UpnpResponse.Status.OK));
        msg.getHeaders().add(UpnpHeader.Type.SID, new SubscriptionIdHeader("uuid:1234"));
        msg.getHeaders().add(UpnpHeader.Type.TIMEOUT, new TimeoutHeader(180));
        return msg;
    }

    protected StreamResponseMessage createUnsubscribeResponseMessage(){
        return new StreamResponseMessage(new UpnpResponse(UpnpResponse.Status.OK));
    }

    @Test
    public void useAppContext() throws Exception {

        assertNotNull(this.sliderService);

    }

    @Test
    public void test() {
        pause(2000);
        this.sliderService.getManager().getImplementation().setTarget("19");
        pause(2000);
        assertEquals("19",status.toString());
    }

    public static void pause(long ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }
}
