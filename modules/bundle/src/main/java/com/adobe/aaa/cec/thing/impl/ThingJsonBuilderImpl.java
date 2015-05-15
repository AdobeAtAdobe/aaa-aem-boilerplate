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
package com.adobe.aaa.cec.thing.impl;

import com.adobe.aaa.cec.thing.ThingConstants;
import com.adobe.aaa.cec.thing.api.ThingJsonBuilder;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.day.cq.commons.jcr.JcrConstants;
import org.apache.sling.jcr.resource.JcrResourceConstants;
import com.day.cq.wcm.api.NameConstants;
import org.apache.commons.lang.time.DateFormatUtils;

import java.util.GregorianCalendar;
import java.util.Iterator;

public class ThingJsonBuilderImpl implements ThingJsonBuilder{
    private static final Logger log = LoggerFactory.getLogger(ThingJsonBuilderImpl.class);
    private ResourceResolver resourceResolver;
    private static final int maxDepth = 10;

    /**
     * Returns a JSONObject that holds all the thing data we need.
     *
     * @param  resource  Resource to start serializing from
     * @param  serializeAssociatedItems  option to resolve linked items. if true we will resolve and include them
     *
     * @return      JSONObject of the thing
     */
    @Override
    public JSONObject serializeToJson(Resource resource,Boolean serializeAssociatedItems) {
        JSONObject jsonObject = null;//empty result

        //is it a page?
        if(resource.isResourceType(NameConstants.NT_PAGE)){
            try {
                //ok so now that we know its a page, lets look at its jcr content
                Resource contentResource = resource.getChild(NameConstants.NN_CONTENT);
                ValueMap resourceVm = contentResource.adaptTo(ValueMap.class);

                dumbResourcePropertiesToDebug(contentResource);

                //if its not a thing we cant serialize it or we wont
                if( (resourceVm.containsKey(JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY)) && (resourceVm.get(JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY).equals(ThingConstants.THING_RESOURCE_TYPE)) || (resourceVm.get(JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY).equals(ThingConstants.THING_RESOURCE_SHORT_TYPE))){
                    log.debug("Doing build json");
                    jsonObject = buildJson(contentResource, jsonObject,serializeAssociatedItems,0,true);
                }else{
                    log.debug("resource passed is not a thing : "+ resource.getResourceType());
                }
            }catch (Exception exc){
                log.error(exc.getLocalizedMessage(),exc.getStackTrace());
            }
        }else {
            log.debug("Resource passed is not a cq:Page its " + resource.getResourceType() + " so are whimping out on serializing it");
        }

        if(jsonObject == null){
            return new JSONObject();//empty object
        }else{
            return jsonObject;
        }
    }

    /**
     * Returns a JSONObject that holds all the thing data we need. This method will not resolve linked items
     *
     * @param  resource  Resource to start serializing from
     *
     * @return      JSONObject of the thing
     */
    @Override
    public JSONObject serializeToJson(Resource resource) {
        return serializeToJson(resource,false);
    }

    @Override
    public int getVersion() {
        return 1;
    }

    /**
     * Constructor
     *
     * @param resourceResolver
     */
    public ThingJsonBuilderImpl(ResourceResolver resourceResolver) {
        this.resourceResolver = resourceResolver;
    }

    private JSONObject buildJson(Resource resource, JSONObject jsonObject,Boolean serializeAssociatedItems,int depth,boolean checkThingPropertyExists) throws Exception{
        depth = depth+1;
        if(depth > this.maxDepth){
            JSONObject maxJson = new JSONObject();
            maxJson.put("error","recursion depth of "+this.maxDepth+" exceeded");
            return maxJson;
        }
        //current resource have a propertyName output it
        ValueMap resourceVm = resource.adaptTo(ValueMap.class);
        String thingPropertyName = resourceVm.get("thingPropertyName",String.class);

        if(((checkThingPropertyExists == true) && (thingPropertyName != null)) || (checkThingPropertyExists == false)){
            JSONObject resourceJson = new JSONObject();

            for (String pName : resourceVm.keySet()) {
                Object keyValue = resourceVm.get((String)pName);

                //string array
                if(keyValue instanceof String[]){
                    //check for Associated Items
                    if(serializeAssociatedItems && (pName.equalsIgnoreCase(ThingConstants.PROPERTY_NAME_THING_ASSOCIATED_ITEMS))){
                        JSONArray jsonArray = new JSONArray();
                        for(String val : (String[])keyValue){
                            String slingResourceType = (String)resourceVm.get("sling:resourceType");

                            //get the resource
                            Resource associatedItemResource = this.resourceResolver.resolve(val); //todo: this only handles things, we need to add in images and stuff

                            JSONObject associatedItemJson = new JSONObject();
                            if(slingResourceType.equalsIgnoreCase(ThingConstants.THING_ASSOCIATED_ITEMS_SLING_RESOURCETYPE)){
                                //serialize that object
                                associatedItemJson = buildJson(associatedItemResource,null,serializeAssociatedItems,depth,true);
                            }else if(slingResourceType.equalsIgnoreCase(ThingConstants.IMAGE_ASSOCIATED_ITEMS_SLING_RESOURCETYPE)){
                                associatedItemJson = buildJson(associatedItemResource, null, serializeAssociatedItems, depth,false);
                            }else if(slingResourceType.equalsIgnoreCase(ThingConstants.VIDEO_ASSOCIATED_ITEMS_SLING_RESOURCETYPE)){
                                associatedItemJson = buildJson(associatedItemResource,null,serializeAssociatedItems,depth,false);
                            }

                            jsonArray.put(associatedItemJson);
                        }
                        resourceJson.put((String)pName,jsonArray);
                    }else{
                        JSONArray jsonArray = new JSONArray();
                        for(String val : (String[])keyValue){
                            jsonArray.put(val);
                        }
                        resourceJson.put((String)pName,jsonArray);
                    }

                    //Calendar
                }else if(keyValue instanceof GregorianCalendar) {
                    GregorianCalendar calendar = (GregorianCalendar)resourceVm.get((String)pName);
                    resourceJson.put((String)pName,DateFormatUtils.format(calendar,"yyyy-MM-dd'T'HH:mm:ssZZ"));

                    //String
                }else{
                    resourceJson.put((String)pName,resourceVm.get((String)pName));
                }
            }

            //check if its the first json object
            if(jsonObject == null){
                jsonObject = resourceJson;
            }else{
                if(checkThingPropertyExists == true){
                    jsonObject.put(thingPropertyName,resourceJson);
                }else {
                    jsonObject.put(resource.getName(),resourceJson);
                }
            }
        }

        if(resource.hasChildren()){
            Iterator<Resource> iChildResource = resource.getChildren().iterator();
            while(iChildResource.hasNext()){
                Resource childResource = iChildResource.next();
                jsonObject = buildJson(childResource,jsonObject,serializeAssociatedItems,depth,checkThingPropertyExists);
            }
        }

        return jsonObject;
    }

    private void dumbResourcePropertiesToDebug(Resource resource){
        ValueMap resourceVm = resource.adaptTo(ValueMap.class);

        //debug
        log.debug("dumping resource " + resource.getPath());
        for(String key : resourceVm.keySet()){
            log.debug("key : " + key);
        }
    }
}