package org.example;

public enum Dcf {
    DCF1(1),
    DCF2(2),
    DCF3(3),
    DCF7(7),
    DCF8(8);
    int id;

    Dcf(int id) {
        this.id = id;
    }

    static Dcf getDcf(int id) {
        for (Dcf value : values()) {
            if (value.getId() == id) {
                return value;
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }
}
