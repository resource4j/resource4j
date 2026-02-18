module com.github.resource4j.core {
    requires com.github.resource4j.converters;
    requires static java.xml;
    requires static java.desktop;
    requires static org.slf4j;


    // Public API
    exports com.github.resource4j;
    exports com.github.resource4j.resources;
    exports com.github.resource4j.resources.context;
    exports com.github.resource4j.resources.discovery;
    exports com.github.resource4j.resources.cache;
    exports com.github.resource4j.resources.processors;
    exports com.github.resource4j.objects;
    exports com.github.resource4j.objects.exceptions;
    exports com.github.resource4j.objects.parsers;
    exports com.github.resource4j.objects.providers;
    exports com.github.resource4j.objects.providers.events;
    exports com.github.resource4j.objects.providers.mutable;
    exports com.github.resource4j.objects.providers.resolvers;
    exports com.github.resource4j.values;
    exports com.github.resource4j.i18n.plural_rules;
    exports com.github.resource4j.i18n.plural_rules.ldml;
}
