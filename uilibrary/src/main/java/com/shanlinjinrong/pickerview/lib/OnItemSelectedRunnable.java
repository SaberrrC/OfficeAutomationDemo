package com.shanlinjinrong.pickerview.lib;

final class OnItemSelectedRunnable implements Runnable {
    final ShanLinWheelView loopView;

    OnItemSelectedRunnable(ShanLinWheelView loopview) {
        loopView = loopview;
    }

    @Override
    public final void run() {
        loopView.onItemSelectedListener.onItemSelected(loopView.getCurrentItem());
    }
}
