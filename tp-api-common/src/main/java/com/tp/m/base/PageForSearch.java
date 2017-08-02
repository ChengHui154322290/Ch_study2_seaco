package com.tp.m.base;

/**
 * Created by ldr on 9/23/2016.
 */
public class PageForSearch<T,R> extends Page<T> {
    private R sp;

    public R getSp() {
        return sp;
    }

    public void setSp(R sp) {
        this.sp = sp;
    }
}
