package com.laserfountain.happysphere;


public class ButtonLink {

    protected enum Type {
        PlusOne, MinusOne
    }


    int from;
    int to;
    Type type;

    ButtonLink(int from, int to, Type type) {
        this.from = from;
        this.to = to;
        this.type = type;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }
    public Type getType() {
        return type;
    }
}
