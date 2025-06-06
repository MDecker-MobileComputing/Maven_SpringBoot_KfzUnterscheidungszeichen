package de.eldecker.dhbw.spring.kfzkennzeichen.rest.apikey;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class ApiKeyFilter extends OncePerRequestFilter {

	private static Logger LOG = LoggerFactory.getLogger( ApiKeyFilter.class );
	
	/** "Datenbank" der aktiven API-Keys. */
	private final List<String> _apiKeyList = List.of( "abc123", "xyz123", "abc456" );
		
	
	@Override
	public void doFilterInternal( HttpServletRequest  request, 
			                      HttpServletResponse response, 
			                      FilterChain         filterChain 
			                    )
			throws ServletException, IOException {


		final String apiKey = request.getHeader( "X-API-KEY" );
		
		if ( apiKey == null || _apiKeyList.contains( apiKey ) == false ) {
			
			LOG.warn( "Request ohne gueltigen API-Key: " + request.getRequestURI() ); 
					
            response.setStatus( 401 ); // Unauthorized
            response.getWriter().write( "Kein API-Key" );         
            
		} else {
			
			filterChain.doFilter( request, response );
		}		
	}

}
