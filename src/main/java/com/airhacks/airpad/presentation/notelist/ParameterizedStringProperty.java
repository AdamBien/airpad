package com.airhacks.airpad.presentation.notelist;

import java.text.MessageFormat;
import javafx.beans.property.StringPropertyBase;

/**
 *
 * @author adam-bien.com
 */
public class ParameterizedStringProperty extends StringPropertyBase {

    private String template;

    public ParameterizedStringProperty(String template) {
        this.template = template;
    }

    @Override
    public Object getBean() {
        return "";
    }

    @Override
    public String getName() {
        return "name";
    }

    @Override
    public String get() {
        String content = super.get();
        return MessageFormat.format(template, content);
    }
}
