package rp3.auna.events;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by jvilla on 08/03/2017.
 */

public class EventBus {
    private static Bus bus=new Bus(ThreadEnforcer.ANY);

    public static Bus getBus(){
        return bus;
    }
}
