package de.eldecker.dhbw.spring.kfzkennzeichen.rest.apikey;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.filter.OncePerRequestFilter;


/**
 * Diese Klasse aktiviert den Filter für den API-Key-Check, wenn das Profil
 * "apiKey" aktiv ist.
 */
@Configuration
@Profile("apikey")
public class ApiKeyKonfig {

	/**
	 * Filter, der nur Requests mit gültigem API-Key durchlässt.
	 */
	private ApiKeyFilter _apiKeyFilter = new ApiKeyFilter();
	
	
	/**
	 * Registriert eine Instanz der Klasse {@link ApiKeyFilter} für
	 * alle REST-Endpunkte.
	 * 
	 * @return {@code FilterRegistrationBean} mit {@link ApiKeyFilter}
	 */
    @Bean
    public FilterRegistrationBean<OncePerRequestFilter> registriereApiKeyFilter() {
    	    	
    	final FilterRegistrationBean<OncePerRequestFilter> frb = 
    												new FilterRegistrationBean<>(); 
    												    	    	
    	frb .setFilter( _apiKeyFilter );
    	frb .addUrlPatterns( "/unterscheidungszeichen/*" );
    	
    	return frb ;
    }
	
}
