package com.adobe.aaa.cec.thing.api;

import com.adobe.aaa.cec.thing.impl.ThingJsonBuilderImpl;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.adapter.AdapterFactory;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * AdapterFactory to adapt to ThingJsonService
 */
@Component(metatype = true,
        label = "ThingJsonBuilder Adaptor Factory",
        description = "ThingJsonBuilder Adaptor Factory")
@Service
public class ThingJsonBuilderAdaptorFactory implements AdapterFactory {
    /**
     * The default logger
     */
    private static Logger log = LoggerFactory.getLogger(ThingJsonBuilderAdaptorFactory.class);

    @Property(name = "adapters", propertyPrivate = true)
    @SuppressWarnings({"UnusedDeclaration"})
    private static final String[] ADAPTER_CLASSES = {
            ThingJsonBuilder.class.getName()
    };

    @Property(name = "adaptables", propertyPrivate = true)
    @SuppressWarnings({"UnusedDeclaration"})
    private static final String[] ADAPTABLE_CLASSES = {
            ResourceResolver.class.getName()
    };

    @Activate
    private void activate(ComponentContext ctx) {
        log.debug("ThingJsonBuilder Adaptor Factory activated");
    }

    /**
     * {@inheritDoc}
     */
    public <AdapterType> AdapterType getAdapter(Object adaptable, Class<AdapterType> type) {
        if (ThingJsonBuilder.class == type && adaptable instanceof ResourceResolver) {
            return (AdapterType) new ThingJsonBuilderImpl((ResourceResolver) adaptable);
        }
        log.info("Can't adapt {} to {}", (adaptable == null) ? "null" : adaptable.getClass().getName(), type.getClass().getName());
        return null;
    }
}
