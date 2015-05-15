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
package com.adobe.aaa.cec.thing;

/**
 * Created by dbenge on 4/28/15.
 */
public final class ThingConstants {
    // RESPONSE CONSTANTS
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String ENCODING = "utf-8";

    //Thing component
    public static final String THING_RESOURCE_TYPE = "/apps/aaa/cec/pages/thing";
    public static final String THING_RESOURCE_SHORT_TYPE = "aaa/cec/pages/thing";
    public static final String PROPERTY_NAME_THING_ASSOCIATED_ITEMS = "associatedItems";
    public static final String THING_ASSOCIATED_ITEMS_SLING_RESOURCETYPE = "aaa/cec/components/thing/associated_items";
    public static final String IMAGE_ASSOCIATED_ITEMS_SLING_RESOURCETYPE = "aaa/cec/components/thing/associated_image_items";
    public static final String VIDEO_ASSOCIATED_ITEMS_SLING_RESOURCETYPE = "aaa/cec/components/thing/associated_video_items";
}
