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

import fr.paris.lutece.plugins.hatest.business.Hatest;
import fr.paris.lutece.plugins.hatest.business.HatestHome;
import java.util.List;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.portal.web.LocalVariables;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import java.sql.Date;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.portal.web.l10n.LocaleService;
import java.io.IOException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminAuthenticationService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
/**
 * This is the business class test for the object Hatest
 */
public class HatestJspBeanTest extends LuteceTestCase
{
    private static final String NOM1 = "Nom1";
    private static final String NOM2 = "Nom2";
    private static final String PRENOM1 = "Prenom1";
    private static final String PRENOM2 = "Prenom2";
    private static final String ADRESSE1 = "Adresse1";
    private static final String ADRESSE2 = "Adresse2";
	private static final Date DATENAISSANCE1 = new Date( 1000000l );
    private static final Date DATENAISSANCE2 = new Date( 2000000l );

public void testJspBeans(  ) throws AccessDeniedException
	{	
     	MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		MockServletConfig config = new MockServletConfig();

		//display admin Hatest management JSP
		HatestJspBean jspbean = new HatestJspBean();
		String html = jspbean.getManageHatests( request );
		assertNotNull(html);

		//display admin Hatest creation JSP
		html = jspbean.getCreateHatest( request );
		assertNotNull(html);

		//action create Hatest
		request = new MockHttpServletRequest();

        request.addParameter( "nom" , NOM1 );
        request.addParameter( "prenom" , PRENOM1 );
        request.addParameter( "adresse" , ADRESSE1 );
        request.addParameter( "datenaissance" , DateUtil.getDateString( DATENAISSANCE1, LocaleService.getDefault( ) ) );
		request.addParameter("action","createHatest");
        request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "createHatest" ));
		request.setMethod( "POST" );
		response = new MockHttpServletResponse( );
		AdminUser adminUser = new AdminUser( );
		adminUser.setAccessCode( "admin" );

		try 
		{
			AdminAuthenticationService.getInstance( ).registerUser(request, adminUser);
			html = jspbean.processController( request, response ); 
			
			// MockResponse object does not redirect, result is always null
			assertNull( html );
		}
		catch (AccessDeniedException e)
		{
			fail("access denied");
		}
		catch (UserNotSignedException e) 
		{
			fail("user not signed in");
		}

		//display modify Hatest JSP
		request = new MockHttpServletRequest();
        request.addParameter( "nom" , NOM1 );
        request.addParameter( "prenom" , PRENOM1 );
        request.addParameter( "adresse" , ADRESSE1 );
        request.addParameter( "datenaissance" , DateUtil.getDateString( DATENAISSANCE1, LocaleService.getDefault( ) ) );
		List<Integer> listIds = HatestHome.getIdHatestsList();
        assertTrue( !listIds.isEmpty( ) );
        request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
		jspbean = new HatestJspBean();

		assertNotNull( jspbean.getModifyHatest( request ) );

		//action modify Hatest
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
        request.addParameter( "nom" , NOM2 );
        request.addParameter( "prenom" , PRENOM2 );
        request.addParameter( "adresse" , ADRESSE2 );
        request.addParameter( "datenaissance" , DateUtil.getDateString( DATENAISSANCE2, LocaleService.getDefault( ) ) );
		request.setRequestURI("jsp/admin/plugins/example/ManageHatests.jsp");
		//important pour que MVCController sache quelle action effectuer, sinon, il redirigera vers createHatest, qui est l'action par défaut
		request.addParameter("action","modifyHatest");
		request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "modifyHatest" ));
		adminUser = new AdminUser();
		adminUser.setAccessCode("admin");

		try 
		{
			AdminAuthenticationService.getInstance( ).registerUser(request, adminUser);
			html = jspbean.processController( request, response ); 

			// MockResponse object does not redirect, result is always null
			assertNull( html );
		}
		catch (AccessDeniedException e)
		{
			fail("access denied");
		}
		catch (UserNotSignedException e) 
		{
			fail("user not signed in");
		}
		
		//get remove Hatest
		request = new MockHttpServletRequest();
        //request.setRequestURI("jsp/admin/plugins/example/ManageHatests.jsp");
        request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
		jspbean = new HatestJspBean();
		request.addParameter("action","confirmRemoveHatest");
		assertNotNull( jspbean.getModifyHatest( request ) );
				
		//do remove Hatest
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		request.setRequestURI("jsp/admin/plugins/example/ManageHatestts.jsp");
		//important pour que MVCController sache quelle action effectuer, sinon, il redirigera vers createHatest, qui est l'action par défaut
		request.addParameter("action","removeHatest");
		request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "removeHatest" ));
		request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
		request.setMethod("POST");
		adminUser = new AdminUser();
		adminUser.setAccessCode("admin");

		try 
		{
			AdminAuthenticationService.getInstance( ).registerUser(request, adminUser);
			html = jspbean.processController( request, response ); 

			// MockResponse object does not redirect, result is always null
			assertNull( html );
		}
		catch (AccessDeniedException e)
		{
			fail("access denied");
		}
		catch (UserNotSignedException e) 
		{
			fail("user not signed in");
		}	
     
     }
}
