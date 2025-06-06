package de.eldecker.dhbw.spring.kfzkennzeichen.rest.apikey;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.filter.OncePerRequestFilter;


@Configuration
@Profile("apikey")
public class ApiKeyKonfig {

	/**
	 * Filter, der nur Requests mit gültigem API-Key durchlässt.
	 */
	private ApiKeyFilter _apiKeyFilter = new ApiKeyFilter();
	
	
    @Bean
    public FilterRegistrationBean<OncePerRequestFilter> apiKeyFilter() {
    	    	
    	FilterRegistrationBean<OncePerRequestFilter> registrationBean = new FilterRegistrationBean<>();
    	    	
    	registrationBean.setFilter( _apiKeyFilter );
    	registrationBean.addUrlPatterns( "/unterscheidungszeichen/*" );
    	
    	return registrationBean;
    }
	
}
