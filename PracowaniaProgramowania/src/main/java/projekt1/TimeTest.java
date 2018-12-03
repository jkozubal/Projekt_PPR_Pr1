package projekt1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimeTest {

    @Test
    void timeTest() {
        assertTrue(Pesel.TimeTest(770) == 25);
    }
    @Test
    void wrongTime(){
        assertFalse(Pesel.TimeTest(770) == 35);
    }

    @Test
    void beforeClasses(){
        assertTrue(Pesel.TimeTest(5*60) == ((3*60)+15));
    }

}