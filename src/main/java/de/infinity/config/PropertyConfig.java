package de.infinity.config;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.List;

/**
 * Configuration für die Anwendung. Inkludiert einen  PropertySourcesPlaceholderConfigurer.
 */
@Configuration
public class PropertyConfig {

    @Bean
    public static SpringPropertyPlaceholderConfigurer myPropertySourcesPlaceholderConfigurer() {
        SpringPropertyPlaceholderConfigurer p = new SpringPropertyPlaceholderConfigurer();

        Iterable<Resource> foundResources = com.google.common.collect.Iterables.filter(getResources(), new Predicate<Resource>() {
            @Override
            public boolean apply(Resource input) {
                return input.exists();
            }
        });

        p.setLocalOverride(true);
        p.setIgnoreResourceNotFound(true);
        p.setLocations(Iterables.toArray(foundResources, Resource.class));
        return p;
    }

    public static List<Resource> getResources() {
        List<Resource> resources = Lists.newArrayList();
        resources.add(new ClassPathResource("application.properties"));
        return resources;
    }
}
