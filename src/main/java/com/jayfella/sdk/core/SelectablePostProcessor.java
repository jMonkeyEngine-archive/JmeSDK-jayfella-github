package com.jayfella.sdk.core;

import com.jayfella.sdk.service.RegistrationService;
import com.jayfella.sdk.service.registration.FilterRegistration;
import com.jme3.post.Filter;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class SelectablePostProcessor {

    private final Class<? extends Filter> filterClass;
    // private boolean enabled = false;
    private BooleanProperty enabled = new SimpleBooleanProperty();

    public SelectablePostProcessor(Class<? extends Filter> filterClass, boolean enabled) {
        this.filterClass = filterClass;
        this.enabled.setValue(enabled);
    }

    public String getFriendlyName() {
        return filterClass.getSimpleName()
                .replace("Filter", "") // remove the "Filter" at the end
                .replaceAll("(\\p{Ll})(\\p{Lu})","$1 $2"); // add spaces before every capital letter.
    }

    public Class<? extends Filter> getFilterClass() {
        return filterClass;
    }

    public boolean isEnabled() {
        return enabled.get();
    }

    public BooleanProperty enabledProperty() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled.setValue(enabled);

        FilterRegistration filterRegistration = ServiceManager.getService(RegistrationService.class).getFilterRegistration();
        filterRegistration.setEnabled(filterClass, enabled);
        filterRegistration.refreshFilters();
    }
}