package com.cstc.stockregister.constant;

public enum EventTypes {
    /**
     * new block event notify type
     */
    BLOCK_NOTIFY(1),

    /**
     * contract event notify type
     */
    EVENT_LOG_PUSH(2),

    /**
     * others ex: sdk error message
     */
    OTHERS(3);

    private int value;

    EventTypes(Integer eventType) {
        this.value = eventType;
    }

    public int getValue() {
        return this.value;
    }
}
