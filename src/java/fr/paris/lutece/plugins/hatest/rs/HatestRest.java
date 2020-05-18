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

package fr.paris.lutece.plugins.hatest.rs;

import fr.paris.lutece.plugins.hatest.business.Hatest;
import fr.paris.lutece.plugins.hatest.business.HatestHome;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import fr.paris.lutece.util.json.ErrorJsonResponse;
import fr.paris.lutece.util.json.JsonResponse;
import fr.paris.lutece.util.json.JsonUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import java.sql.Date;
import java.util.List;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * HatestRest
 */
@Path( RestConstants.BASE_PATH + Constants.API_PATH + Constants.VERSION_PATH + Constants.HATEST_PATH )
public class HatestRest
{
    private static final int VERSION_1 = 1;
    private final Logger _logger = Logger.getLogger( RestConstants.REST_LOGGER );
    
    /**
     * Get Hatest List
     * @param nVersion the API version
     * @return the Hatest List
     */
    @GET
    @Path( StringUtils.EMPTY )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getHatestList( @PathParam( Constants.VERSION ) Integer nVersion )
    {
        if ( nVersion == VERSION_1 )
        {
            return getHatestListV1( );
        }
        _logger.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) )
                .build( );
    }
    
    /**
     * Get Hatest List V1
     * @return the Hatest List for the version 1
     */
    private Response getHatestListV1( )
    {
        List<Hatest> listHatests = HatestHome.getHatestsList( );
        
        if ( listHatests.isEmpty( ) )
        {
            return Response.status( Response.Status.NO_CONTENT )
                .entity( JsonUtil.buildJsonResponse( new JsonResponse( Constants.EMPTY_OBJECT ) ) )
                .build( );
        }
        return Response.status( Response.Status.OK )
                .entity( JsonUtil.buildJsonResponse( new JsonResponse( listHatests ) ) )
                .build( );
    }
    
    /**
     * Create Hatest
     * @param nVersion the API version
     * @param nom the nom
     * @param prenom the prenom
     * @param adresse the adresse
     * @param datenaissance the datenaissance
     * @return the Hatest if created
     */
    @POST
    @Path( StringUtils.EMPTY )
    @Produces( MediaType.APPLICATION_JSON )
    public Response createHatest(
    @FormParam( Constants.HATEST_ATTRIBUTE_NOM ) String nom,
    @FormParam( Constants.HATEST_ATTRIBUTE_PRENOM ) String prenom,
    @FormParam( Constants.HATEST_ATTRIBUTE_ADRESSE ) String adresse,
    @FormParam( Constants.HATEST_ATTRIBUTE_DATENAISSANCE ) String datenaissance,
    @PathParam( Constants.VERSION ) Integer nVersion )
    {
		if ( nVersion == VERSION_1 )
		{
		    return createHatestV1( nom, prenom, adresse, datenaissance );
		}
        _logger.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) )
                .build( );
    }
    
    /**
     * Create Hatest V1
     * @param nom the nom
     * @param prenom the prenom
     * @param adresse the adresse
     * @param datenaissance the datenaissance
     * @return the Hatest if created for the version 1
     */
    private Response createHatestV1( String nom, String prenom, String adresse, String datenaissance )
    {
        if ( StringUtils.isEmpty( nom ) || StringUtils.isEmpty( prenom ) || StringUtils.isEmpty( adresse ) || StringUtils.isEmpty( datenaissance ) )
        {
            _logger.error( Constants.ERROR_BAD_REQUEST_EMPTY_PARAMETER );
            return Response.status( Response.Status.BAD_REQUEST )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.BAD_REQUEST.name( ), Constants.ERROR_BAD_REQUEST_EMPTY_PARAMETER ) ) )
                    .build( );
        }
        
        Hatest hatest = new Hatest( );
    	hatest.setNom( nom );
    	hatest.setPrenom( prenom );
    	hatest.setAdresse( adresse );
	    hatest.setDatenaissance( Date.valueOf( datenaissance ) );
        HatestHome.create( hatest );
        
        return Response.status( Response.Status.OK )
                .entity( JsonUtil.buildJsonResponse( new JsonResponse( hatest ) ) )
                .build( );
    }
    
    /**
     * Modify Hatest
     * @param nVersion the API version
     * @param id the id
     * @param nom the nom
     * @param prenom the prenom
     * @param adresse the adresse
     * @param datenaissance the datenaissance
     * @return the Hatest if modified
     */
    @PUT
    @Path( Constants.ID_PATH )
    @Produces( MediaType.APPLICATION_JSON )
    public Response modifyHatest(
    @PathParam( Constants.ID ) Integer id,
    @FormParam( Constants.HATEST_ATTRIBUTE_NOM ) String nom,
    @FormParam( Constants.HATEST_ATTRIBUTE_PRENOM ) String prenom,
    @FormParam( Constants.HATEST_ATTRIBUTE_ADRESSE ) String adresse,
    @FormParam( Constants.HATEST_ATTRIBUTE_DATENAISSANCE ) String datenaissance,
    @PathParam( Constants.VERSION ) Integer nVersion )
    {
        if ( nVersion == VERSION_1 )
        {
            return modifyHatestV1( id, nom, prenom, adresse, datenaissance );
        }
        _logger.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) )
                .build( );
    }
    
    /**
     * Modify Hatest V1
     * @param id the id
     * @param nom the nom
     * @param prenom the prenom
     * @param adresse the adresse
     * @param datenaissance the datenaissance
     * @return the Hatest if modified for the version 1
     */
    private Response modifyHatestV1( Integer id, String nom, String prenom, String adresse, String datenaissance )
    {
        if ( StringUtils.isEmpty( nom ) || StringUtils.isEmpty( prenom ) || StringUtils.isEmpty( adresse ) || StringUtils.isEmpty( datenaissance ) )
        {
            _logger.error( Constants.ERROR_BAD_REQUEST_EMPTY_PARAMETER );
            return Response.status( Response.Status.BAD_REQUEST )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.BAD_REQUEST.name( ), Constants.ERROR_BAD_REQUEST_EMPTY_PARAMETER ) ) )
                    .build( );
        }
        
        Hatest hatest = HatestHome.findByPrimaryKey( id );
        if ( hatest == null )
        {
            _logger.error( Constants.ERROR_NOT_FOUND_RESOURCE );
            return Response.status( Response.Status.NOT_FOUND )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_RESOURCE ) ) )
                    .build( );
        }
        
    	hatest.setNom( nom );
    	hatest.setPrenom( prenom );
    	hatest.setAdresse( adresse );
	    hatest.setDatenaissance( Date.valueOf( datenaissance ) );
        HatestHome.update( hatest );
        
        return Response.status( Response.Status.OK )
                .entity( JsonUtil.buildJsonResponse( new JsonResponse( hatest ) ) )
                .build( );
    }
    
    /**
     * Delete Hatest
     * @param nVersion the API version
     * @param id the id
     * @return the Hatest List if deleted
     */
    @DELETE
    @Path( Constants.ID_PATH )
    @Produces( MediaType.APPLICATION_JSON )
    public Response deleteHatest(
    @PathParam( Constants.VERSION ) Integer nVersion,
    @PathParam( Constants.ID ) Integer id )
    {
        if ( nVersion == VERSION_1 )
        {
            return deleteHatestV1( id );
        }
        _logger.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) )
                .build( );
    }
    
    /**
     * Delete Hatest V1
     * @param id the id
     * @return the Hatest List if deleted for the version 1
     */
    private Response deleteHatestV1( Integer id )
    {
        Hatest hatest = HatestHome.findByPrimaryKey( id );
        if ( hatest == null )
        {
            _logger.error( Constants.ERROR_NOT_FOUND_RESOURCE );
            return Response.status( Response.Status.NOT_FOUND )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_RESOURCE ) ) )
                    .build( );
        }
        
        HatestHome.remove( id );
        
        return Response.status( Response.Status.OK )
                .entity( JsonUtil.buildJsonResponse( new JsonResponse( Constants.EMPTY_OBJECT ) ) )
                .build( );
    }
    
    /**
     * Get Hatest
     * @param nVersion the API version
     * @param id the id
     * @return the Hatest
     */
    @GET
    @Path( Constants.ID_PATH )
    @Produces( MediaType.APPLICATION_JSON )
    public Response getHatest(
    @PathParam( Constants.VERSION ) Integer nVersion,
    @PathParam( Constants.ID ) Integer id )
    {
        if ( nVersion == VERSION_1 )
        {
            return getHatestV1( id );
        }
        _logger.error( Constants.ERROR_NOT_FOUND_VERSION );
        return Response.status( Response.Status.NOT_FOUND )
                .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_VERSION ) ) )
                .build( );
    }
    
    /**
     * Get Hatest V1
     * @param id the id
     * @return the Hatest for the version 1
     */
    private Response getHatestV1( Integer id )
    {
        Hatest hatest = HatestHome.findByPrimaryKey( id );
        if ( hatest == null )
        {
            _logger.error( Constants.ERROR_NOT_FOUND_RESOURCE );
            return Response.status( Response.Status.NOT_FOUND )
                    .entity( JsonUtil.buildJsonResponse( new ErrorJsonResponse( Response.Status.NOT_FOUND.name( ), Constants.ERROR_NOT_FOUND_RESOURCE ) ) )
                    .build( );
        }
        
        return Response.status( Response.Status.OK )
                .entity( JsonUtil.buildJsonResponse( new JsonResponse( hatest ) ) )
                .build( );
    }
}