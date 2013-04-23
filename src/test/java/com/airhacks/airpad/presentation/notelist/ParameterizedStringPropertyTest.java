package com.airhacks.airpad.presentation.notelist;

import javafx.beans.property.SimpleStringProperty;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author adam-bien.com
 */
public class ParameterizedStringPropertyTest {

    @Test
    public void parameterizedBinding() {
        SimpleStringProperty origin = new SimpleStringProperty();
        ParameterizedStringProperty ssp = new ParameterizedStringProperty("-{0}+");
        SimpleStringProperty dependent = new SimpleStringProperty();
        ssp.bind(origin);
        dependent.bind(ssp);
        String actual = ssp.get();
        String actualDependent = dependent.get();
        final String expectedEmplty = "-null+";
        assertThat(actual, is(expectedEmplty));
        assertThat(actualDependent, is(expectedEmplty));
        origin.set("42");
        actual = ssp.get();
        actualDependent = dependent.get();
        final String expectedWithParameter = "-42+";
        assertThat(actual, is(expectedWithParameter));
        assertThat(actualDependent, is(expectedWithParameter));
    }
}
