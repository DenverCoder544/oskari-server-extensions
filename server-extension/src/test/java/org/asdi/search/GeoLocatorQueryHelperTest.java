package org.asdi.search;

import fi.nls.oskari.util.IOHelper;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * Created by SMAKINEN on 8.9.2017.
 */
public class GeoLocatorQueryHelperTest {

    @BeforeClass
    public static void setUp() throws Exception {
        // use relaxed comparison settings
        XMLUnit.setIgnoreComments(true);
        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true);
        XMLUnit.setIgnoreAttributeOrder(true);

        GeoLocatorCountries countries = GeoLocatorCountries.getInstance();
        String response = IOHelper.readString(GeoLocatorQueryHelperTest.class.getResourceAsStream("geolocator-country-filter-response.xml"));
        Map<String, String> map = countries.parseCountryMap(response);
        countries.setCountryMap(map);
    }

    @Test
    public void testGetFilterMultipleAdmins() throws Exception {
        GeoLocatorQueryHelper helper = new GeoLocatorQueryHelper();
        String adminFilter = helper.getFilter("asdf", "no");
        System.out.println(adminFilter);

        String expectedFilter = IOHelper.readString(getClass().getResourceAsStream("geolocator-admin-filter-expected-norway.xml"));
        Diff xmlDiff = new Diff(adminFilter, expectedFilter);
        assertTrue("Should get expected admin filter " + xmlDiff, xmlDiff.similar());
    }

    @Test
    public void testGetFilterSingleAdmin() throws Exception {
        GeoLocatorQueryHelper helper = new GeoLocatorQueryHelper();
        String adminFilter = helper.getFilter("asdf", "fi");

        String expectedFilter = IOHelper.readString(getClass().getResourceAsStream("geolocator-admin-filter-expected-finland.xml"));
        Diff xmlDiff = new Diff(adminFilter, expectedFilter);
        assertTrue("Should get expected admin filter " + xmlDiff, xmlDiff.similar());
    }

    @Test
    public void testGetFilterNoInputSingleAdmin() throws Exception {
        GeoLocatorQueryHelper helper = new GeoLocatorQueryHelper();
        String adminFilter = helper.getFilter("", "fi");

        String expectedFilter = IOHelper.readString(getClass().getResourceAsStream("geolocator-admin-filter-expected-no-input-fi.xml"));
        Diff xmlDiff = new Diff(adminFilter, expectedFilter);
        assertTrue("Should get expected admin filter " + xmlDiff, xmlDiff.similar());
    }
    @Test
    public void testGetFilterNoInputMultipleAdmins() throws Exception {
        GeoLocatorQueryHelper helper = new GeoLocatorQueryHelper();
        String adminFilter = helper.getFilter("", "no");

        String expectedFilter = IOHelper.readString(getClass().getResourceAsStream("geolocator-admin-filter-expected-no-input-no.xml"));
        Diff xmlDiff = new Diff(adminFilter, expectedFilter);
        assertTrue("Should get expected admin filter " + xmlDiff, xmlDiff.similar());
    }

    @Test
    public void testGetFilterNoCountry() throws Exception {
        GeoLocatorQueryHelper helper = new GeoLocatorQueryHelper();
        String adminFilter = helper.getFilter("asdf", null);

        String expectedFilter = IOHelper.readString(getClass().getResourceAsStream("geolocator-admin-filter-expected-user-input-only.xml"));
        Diff xmlDiff = new Diff(adminFilter, expectedFilter);
        assertTrue("Should get expected admin filter " + xmlDiff, xmlDiff.similar());
    }
}