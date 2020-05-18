/*
 * Copyright (c) 2002-2020, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES LOSS OF USE, DATA, OR PROFITS OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */

package fr.paris.lutece.plugins.hatest.web;

import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.web.LocalVariables;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import fr.paris.lutece.plugins.hatest.business.Hatest;
import fr.paris.lutece.plugins.hatest.business.HatestHome;
import java.util.List;
import java.sql.Date;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.portal.web.l10n.LocaleService;
/**
 * This is the business class test for the object Hatest
 */
public class HatestXPageTest extends LuteceTestCase
{
    private static final String NOM1 = "Nom1";
    private static final String NOM2 = "Nom2";
    private static final String PRENOM1 = "Prenom1";
    private static final String PRENOM2 = "Prenom2";
    private static final String ADRESSE1 = "Adresse1";
    private static final String ADRESSE2 = "Adresse2";
	private static final Date DATENAISSANCE1 = new Date( 1000000l );
    private static final Date DATENAISSANCE2 = new Date( 2000000l );

public void testXPage(  ) throws AccessDeniedException
	{
        // Xpage create test
        MockHttpServletRequest request = new MockHttpServletRequest( );
		MockHttpServletResponse response = new MockHttpServletResponse( );
		MockServletConfig config = new MockServletConfig( );

		HatestXPage xpage = new HatestXPage( );
		assertNotNull( xpage.getCreateHatest( request ) );
		
		request = new MockHttpServletRequest();
		request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "createHatest" ));
		
		LocalVariables.setLocal(config, request, response);
		
		request.addParameter( "action" , "createHatest" );
        request.addParameter( "nom" , NOM1 );
        request.addParameter( "prenom" , PRENOM1 );
        request.addParameter( "adresse" , ADRESSE1 );
        request.addParameter( "datenaissance" , DateUtil.getDateString( DATENAISSANCE1, LocaleService.getDefault( ) ) );
		request.setMethod( "POST" );
        
        assertNotNull( xpage.doCreateHatest( request ) );

		//modify Hatest	
		List<Integer> listIds = HatestHome.getIdHatestsList(); 

		assertTrue( !listIds.isEmpty( ) );

		request = new MockHttpServletRequest();
		request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );

		assertNotNull( xpage.getModifyHatest( request ) );

		response = new MockHttpServletResponse();
		request = new MockHttpServletRequest();
		LocalVariables.setLocal(config, request, response);
        request.addParameter( "nom" , NOM2 );
        request.addParameter( "prenom" , PRENOM2 );
        request.addParameter( "adresse" , ADRESSE2 );
        request.addParameter( "datenaissance" , DateUtil.getDateString( DATENAISSANCE2, LocaleService.getDefault( ) ) );

		request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "modifyHatest" ));
		request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
		assertNotNull( xpage.doModifyHatest( request ) );

		//do confirm remove Hatest
		request = new MockHttpServletRequest();
		request.addParameter( "action" , "confirmRemoveHatest" );
		request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
		request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "confirmRemoveHatest" ));;
		request.setMethod("GET");

		try
		{
			xpage.getConfirmRemoveHatest( request );
		}
		catch(Exception e)
		{
			assertTrue(e instanceof SiteMessageException);
		}

		//do remove Hatest
		response = new MockHttpServletResponse();
		request = new MockHttpServletRequest();
		LocalVariables.setLocal(config, request, response);
		request.addParameter( "action" , "removeHatest" );
		request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "removeHatest" ));
		request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
		assertNotNull( xpage.doRemoveHatest( request ) );

    }
    
}