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


/**
 * Filter für HTTP-Requests, der HTTP-Requests ohne gültigen API-Key abbricht.
 */
public class ApiKeyFilter extends OncePerRequestFilter {

	private static Logger LOG = LoggerFactory.getLogger( ApiKeyFilter.class );

	
	/** "Datenbank" der aktiven API-Keys. */
	private final List<String> _apiKeyList = List.of( "abc123", "xyz123", "abc456" );


	/**
	 * Diese Methode bricht einen HTTP-Request ab, wenn kein gültiger API-Key
	 * in {@code request} als URL-Parameter enthalten ist.
	 *
	 * @param request HTTP-Anfrage, von der der API-Key gelesen wird
	 *
	 * @param response HTTP-Antwort
	 *
	 * @param filterChain Filterkette (wird aufgerufen für weitere Verarbeitung 
	 *                    des Requests)
	 */
	@Override
	public void doFilterInternal( HttpServletRequest  request,
			                      HttpServletResponse response,
			                      FilterChain         filterChain
			                    )
			throws ServletException, IOException {

		final String apiKey = request.getParameter( "apikey" );
		//final String apiKey = request.getHeader( "X-API-Key" );
		
		if ( apiKey == null ) {

			LOG.warn( "Request ganz ohne API-Key fuer Pfad {} .", 
					  request.getRequestURI() );

            response.setStatus( 401 ); // Unauthorized
            response.getWriter().write( "Kein API-Key" );

		} else if ( _apiKeyList.contains( apiKey ) == false ) {

			LOG.warn( "Ungueltiger API-Key \"{}\" in Request fuer Pfad {} .", 
					  apiKey, request.getRequestURI() );

            response.setStatus( 401 ); // Unauthorized
            response.getWriter().write( "API-Key nicht gueltig." );
			
		} else {

			filterChain.doFilter( request, response );
		}
	}

}
