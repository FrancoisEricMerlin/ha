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
 *"
 * License 1.0
 */

package fr.paris.lutece.plugins.hatest.business;

import fr.paris.lutece.test.LuteceTestCase;

import java.sql.Date;

/**
 * This is the business class test for the object Hatest
 */
public class HatestBusinessTest extends LuteceTestCase
{
    private static final String NOM1 = "Nom1";
    private static final String NOM2 = "Nom2";
    private static final String PRENOM1 = "Prenom1";
    private static final String PRENOM2 = "Prenom2";
    private static final String ADRESSE1 = "Adresse1";
    private static final String ADRESSE2 = "Adresse2";
	private static final Date DATENAISSANCE1 = new Date( 1000000l );
    private static final Date DATENAISSANCE2 = new Date( 2000000l );

	/**
	* test Hatest
	*/
    public void testBusiness(  )
    {
        // Initialize an object
        Hatest hatest = new Hatest();
        hatest.setNom( NOM1 );
        hatest.setPrenom( PRENOM1 );
        hatest.setAdresse( ADRESSE1 );
        hatest.setDatenaissance( DATENAISSANCE1 );

        // Create test
        HatestHome.create( hatest );
        Hatest hatestStored = HatestHome.findByPrimaryKey( hatest.getId( ) );
        assertEquals( hatestStored.getNom() , hatest.getNom( ) );
        assertEquals( hatestStored.getPrenom() , hatest.getPrenom( ) );
        assertEquals( hatestStored.getAdresse() , hatest.getAdresse( ) );
        assertEquals( hatestStored.getDatenaissance().toString() , hatest.getDatenaissance( ).toString() );

        // Update test
        hatest.setNom( NOM2 );
        hatest.setPrenom( PRENOM2 );
        hatest.setAdresse( ADRESSE2 );
        hatest.setDatenaissance( DATENAISSANCE2 );
        HatestHome.update( hatest );
        hatestStored = HatestHome.findByPrimaryKey( hatest.getId( ) );
        assertEquals( hatestStored.getNom() , hatest.getNom( ) );
        assertEquals( hatestStored.getPrenom() , hatest.getPrenom( ) );
        assertEquals( hatestStored.getAdresse() , hatest.getAdresse( ) );
        assertEquals( hatestStored.getDatenaissance().toString() , hatest.getDatenaissance( ).toString() );

        // List test
        HatestHome.getHatestsList( );

        // Delete test
        HatestHome.remove( hatest.getId( ) );
        hatestStored = HatestHome.findByPrimaryKey( hatest.getId( ) );
        assertNull( hatestStored );
        
    }
    
    
     

}