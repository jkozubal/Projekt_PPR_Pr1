package projekt1;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class PeselTest {
    private Pesel pesel;

    @Before
    public void setup(){
        pesel = new Pesel();
    }
    //testy do peselu
    @Test
    void CorrectPeselTest() {
        assertTrue(Pesel.PESELChecker("90090515836"));
    }
    @Test
    void CorrectPeselLength(){
        assertTrue(Pesel.PESELChecker("80072909146"));
    }

    @Test
    void LengthTest(){
        assertFalse(Pesel.PESELChecker("231"));
    }

}