package com.luchetti.springboot.firstApplication;

import com.luchetti.springboot.firstApplication.model.Person;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( testDateTime() );

        assertNull( testPersonNullEsrt("").getEsrt() );
        assertNull( testPersonNullEsrt(null).getEsrt() );

        String test = "1";
        assertEquals(String.format("Text '%s' could not be parsed at index 0",test), testPersonBadRequest(test));
    }
    
    /**
     * Acceptable Formats
	 * <ul>
	 * <li>null</li>
	 * <li>"" (empty string)</li>
	 * <li>2017-11-05 02:00:00</li>
	 * <li>2017-11-05 02:00:00Z</li>
	 * <li>2017-11-05 02:00:00-05:00</li>
	 * <li>2017-11-05 02:00:00-06:00</li>
	 * <li>2017-11-05T02:00:00</li>
	 * <li>2017-11-05T02:00:00Z</li>
	 * <li>2017-11-05T02:00:00-05:00</li>
	 * <li>2017-11-05T02:00:00-06:00</li>
     */
    public boolean testDateTime() {
    	Person p = new Person();
    	p.setEsrt(null);
    	p.setEsrt("");
    	p.setEsrt("2017-11-05 02:00:00");
    	p.setEsrt("2017-11-05 02:00:00Z");
    	p.setEsrt("2017-11-05 02:00:00-05:00");
    	p.setEsrt("2017-11-05 02:00:00-06:00");
    	p.setEsrt("2017-11-05T02:00:00");
    	p.setEsrt("2017-11-05T02:00:00Z");
    	p.setEsrt("2017-11-05T02:00:00-05:00");
    	p.setEsrt("2017-11-05T02:00:00-06:00");
    	return true;
    }

    public Person testPersonNullEsrt(String test) {
    	Person p = new Person();
    	p.setEsrt(test);
    	return p;
    }

    public String testPersonBadRequest(String badString) {
    	Person p = new Person();
    	try {
    		p.setEsrt(badString);
    	}catch(Exception e) {
    		return e.getMessage();
    	}
    	return null;
    }

}
