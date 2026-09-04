package com.dacaga.shortener.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Base62EncoderTest {

    @Test
    void encodes62As10(){
        Base62Encoder encoder = new Base62Encoder();
        assertEquals("10", encoder.encode(62L));
    }

    @Test
    void encodesZeroAsZero(){
        Base62Encoder encoder = new Base62Encoder();
        assertEquals("0", encoder.encode(0L));
    }

    @Test
    void encodesFiveAsFive(){
        Base62Encoder encoder = new Base62Encoder();
        assertEquals("5", encoder.encode(5L));
    }

    @Test
    void encodes61AsZ(){
        Base62Encoder encoder = new Base62Encoder();
        assertEquals("Z", encoder.encode(61L));
    }

    @Test
    void encodes63As11(){
        Base62Encoder encoder = new Base62Encoder();
        assertEquals("11", encoder.encode(63L));
    }

}
