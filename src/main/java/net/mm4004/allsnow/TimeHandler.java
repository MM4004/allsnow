package net.mm4004.allsnow;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;

public class TimeHandler {
    private static final int DAY_TWILIGHT = 12000;
    private static final int MORNING_TWILIGHT = 23000;

    private static final int DAY_WAIT_TICKS = 12000; // 10 minutes

    private int dayCounter = 0;

    private enum State {
        FROZEN_DAY,
        RUNNING_NIGHT
    }

    private State state = State.FROZEN_DAY;

    @SubscribeEvent
    public void onWorldTicks(TickEvent.WorldTickEvent e) {
        if (e.side != Side.SERVER) {
            return;
        }
        if (e.phase != TickEvent.Phase.END) {
            return;
        }

        long time = e.world.getWorldTime() % 24000;
        long base = e.world.getWorldTime() - time;

        switch (state) {

            case FROZEN_DAY:
                e.world.setWorldTime(base + DAY_TWILIGHT);

                dayCounter++;
                if (dayCounter >= DAY_WAIT_TICKS) {
                    dayCounter = 0;
                    state = State.RUNNING_NIGHT;
                }
                break;

            case RUNNING_NIGHT:
                if (time >= MORNING_TWILIGHT && time < MORNING_TWILIGHT + 5) {
                    e.world.setWorldTime(base + DAY_TWILIGHT);
                    state = State.FROZEN_DAY;
                }
                break;
        }
    }
}
