/*************************************************************************
 *
 * ADOBE CONFIDENTIAL
 * __________________
 *
 *  Copyright 2013 Adobe Systems Incorporated
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Adobe Systems Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Adobe Systems Incorporated and its
 * suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Adobe Systems Incorporated.
 * @author dbenge
 **************************************************************************/
package com.adobe.aaa.cec.thing.api;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.json.JSONObject;

/**
 * ThingJsonBuilder - service takes a resource and serializes it
 */
public interface ThingJsonBuilder {

    /**
     * Returns a JSONObject that holds all the thing data we need. This method will not resolve linked items
     *
     * @param  resource  Resource to start serializing from
     *
     * @return      JSONObject of the thing
     */
    public JSONObject serializeToJson(Resource resource);

    /**
     * Returns a JSONObject that holds all the thing data we need.
     *
     * @param  resource  Resource to start serializing from
     * @param  serializeAssociatedItems  option to resolve linked items. if true we will resolve and include them
     *
     * @return      JSONObject of the thing
     */
    public JSONObject serializeToJson(Resource resource,Boolean serializeAssociatedItems);

    public int getVersion();
}
