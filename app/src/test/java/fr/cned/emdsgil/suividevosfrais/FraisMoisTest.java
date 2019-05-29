package fr.cned.emdsgil.suividevosfrais;

import org.junit.Test;

import static org.junit.Assert.*;

public class FraisMoisTest {

    /**
     * Test permettant de vérifier que le constructeur & le getter getMois() font bien leur travail
     */
    @Test
    public void getMois() {
        int input = 1;
        int expected = input;

        FraisMois fraisMois = new FraisMois(2019, input);
        int output = fraisMois.getMois();

        assertEquals(expected, output);
    }

    /**
     * Test permettant de s'assurer que le setter setMois() fonctionne bien comme prévu
     */
    @Test
    public void setMois() {
        int input = 1;
        int expected = input;

        FraisMois fraisMois = new FraisMois(2019, 0);
        fraisMois.setMois(input);

        int output = fraisMois.getMois();

        assertEquals(expected, output);
    }

    /**
     * Test permettant de vérifier que le constructeur & le getter getAnnee() font bien leur travail
     */
    @Test
    public void getAnnee() {
        int input = 2019;
        int expected = input;

        FraisMois fraisMois = new FraisMois(input, 0);
        int output = fraisMois.getAnnee();

        assertEquals(expected, output);
    }

    /**
     * Test permettant de s'assurer que le setter setAnnee() fonctionne bien comme prévu
     */
    @Test
    public void setAnnee() {
        int input = 1;
        int expected = input;

        FraisMois fraisMois = new FraisMois(0, 0);
        fraisMois.setAnnee(input);

        int output = fraisMois.getAnnee();

        assertEquals(expected, output);
    }

    /**
     * Test permettant de s'assurer que setEtape() et getEtape() fonctionnent bien comme prévu
     */
    @Test
    public void setEtape() {
        int input = 1;
        int expected = input;

        FraisMois fraisMois = new FraisMois(0, 0);
        fraisMois.setEtape(input);

        int output = fraisMois.getEtape();

        assertEquals(expected, output);
    }

    /**
     * Test permettant de s'assurer que setKm() et getKm() fonctionnent bien comme prévu
     */
    @Test
    public void setKm() {
        int input = 1;
        int expected = input;

        FraisMois fraisMois = new FraisMois(0, 0);
        fraisMois.setKm(input);

        int output = fraisMois.getKm();

        assertEquals(expected, output);
    }

    /**
     * Test permettant de s'assurer que setNuitee() et getNuitee() fonctionnent bien comme prévu
     */
    @Test
    public void setNuitee() {
        int input = 1;
        int expected = input;

        FraisMois fraisMois = new FraisMois(0, 0);
        fraisMois.setNuitee(input);

        int output = fraisMois.getNuitee();

        assertEquals(expected, output);
    }

    /**
     * Test permettant de s'assurer que setRepas() et getRepas() fonctionnent bien comme prévu
     */
    @Test
    public void setRepas() {
        int input = 1;
        int expected = input;

        FraisMois fraisMois = new FraisMois(0, 0);
        fraisMois.setRepas(input);

        int output = fraisMois.getRepas();

        assertEquals(expected, output);
    }
}