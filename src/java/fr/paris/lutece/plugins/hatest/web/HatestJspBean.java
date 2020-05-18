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
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
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
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.url.UrlItem;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * This class provides the user interface to manage Hatest features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageHatests.jsp", controllerPath = "jsp/admin/plugins/hatest/", right = "HATEST_MANAGEMENT" )
public class HatestJspBean extends AbstractManageHaJspBean
{
    // Templates
    private static final String TEMPLATE_MANAGE_HATESTS = "/admin/plugins/hatest/manage_hatests.html";
    private static final String TEMPLATE_CREATE_HATEST = "/admin/plugins/hatest/create_hatest.html";
    private static final String TEMPLATE_MODIFY_HATEST = "/admin/plugins/hatest/modify_hatest.html";

    // Parameters
    private static final String PARAMETER_ID_HATEST = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_HATESTS = "hatest.manage_hatests.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_HATEST = "hatest.modify_hatest.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_HATEST = "hatest.create_hatest.pageTitle";

    // Markers
    private static final String MARK_HATEST_LIST = "hatest_list";
    private static final String MARK_HATEST = "hatest";

    private static final String JSP_MANAGE_HATESTS = "jsp/admin/plugins/hatest/ManageHatests.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_HATEST = "hatest.message.confirmRemoveHatest";

    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "hatest.model.entity.hatest.attribute.";

    // Views
    private static final String VIEW_MANAGE_HATESTS = "manageHatests";
    private static final String VIEW_CREATE_HATEST = "createHatest";
    private static final String VIEW_MODIFY_HATEST = "modifyHatest";

    // Actions
    private static final String ACTION_CREATE_HATEST = "createHatest";
    private static final String ACTION_MODIFY_HATEST = "modifyHatest";
    private static final String ACTION_REMOVE_HATEST = "removeHatest";
    private static final String ACTION_CONFIRM_REMOVE_HATEST = "confirmRemoveHatest";

    // Infos
    private static final String INFO_HATEST_CREATED = "hatest.info.hatest.created";
    private static final String INFO_HATEST_UPDATED = "hatest.info.hatest.updated";
    private static final String INFO_HATEST_REMOVED = "hatest.info.hatest.removed";
    
    // Session variable to store working values
    private Hatest _hatest;
    
    /**
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_HATESTS, defaultView = true )
    public String getManageHatests( HttpServletRequest request )
    {
        _hatest = null;
        List<Hatest> listHatests = HatestHome.getHatestsList(  );
        Map<String, Object> model = getPaginatedListModel( request, MARK_HATEST_LIST, listHatests, JSP_MANAGE_HATESTS );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_HATESTS, TEMPLATE_MANAGE_HATESTS, model );
    }

    /**
     * Returns the form to create a hatest
     *
     * @param request The Http request
     * @return the html code of the hatest form
     */
    @View( VIEW_CREATE_HATEST )
    public String getCreateHatest( HttpServletRequest request )
    {
        _hatest = ( _hatest != null ) ? _hatest : new Hatest(  );

        Map<String, Object> model = getModel(  );
        model.put( MARK_HATEST, _hatest );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_CREATE_HATEST ) );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_HATEST, TEMPLATE_CREATE_HATEST, model );
    }

    /**
     * Process the data capture form of a new hatest
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_CREATE_HATEST )
    public String doCreateHatest( HttpServletRequest request ) throws AccessDeniedException
    {
        populate( _hatest, request, getLocale( ) );

        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_CREATE_HATEST ) )
        {
            throw new AccessDeniedException ( "Invalid security token" );
        }

        // Check constraints
        if ( !validateBean( _hatest, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_HATEST );
        }

        HatestHome.create( _hatest );
        addInfo( INFO_HATEST_CREATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_HATESTS );
    }

    /**
     * Manages the removal form of a hatest whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_HATEST )
    public String getConfirmRemoveHatest( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_HATEST ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_HATEST ) );
        url.addParameter( PARAMETER_ID_HATEST, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_HATEST, url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a hatest
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage hatests
     */
    @Action( ACTION_REMOVE_HATEST )
    public String doRemoveHatest( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_HATEST ) );
        HatestHome.remove( nId );
        addInfo( INFO_HATEST_REMOVED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_HATESTS );
    }

    /**
     * Returns the form to update info about a hatest
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_HATEST )
    public String getModifyHatest( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_HATEST ) );

        if ( _hatest == null || ( _hatest.getId(  ) != nId ) )
        {
            _hatest = HatestHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel(  );
        model.put( MARK_HATEST, _hatest );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_MODIFY_HATEST ) );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_HATEST, TEMPLATE_MODIFY_HATEST, model );
    }

    /**
     * Process the change form of a hatest
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_MODIFY_HATEST )
    public String doModifyHatest( HttpServletRequest request ) throws AccessDeniedException
    {
        populate( _hatest, request, getLocale( ) );

        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_MODIFY_HATEST ) )
        {
            throw new AccessDeniedException ( "Invalid security token" );
        }

        // Check constraints
        if ( !validateBean( _hatest, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_HATEST, PARAMETER_ID_HATEST, _hatest.getId( ) );
        }

        HatestHome.update( _hatest );
        addInfo( INFO_HATEST_UPDATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_HATESTS );
    }
}
