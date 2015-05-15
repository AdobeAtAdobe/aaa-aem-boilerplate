/*
 * Drag and drop handler with multi value support
 * Special thanks to Mark Ellis for being a BOSS and helping me figure this out
 *
 */
(function ($, author) {
    function MultiImageDragAndDrop() {
        this.constructor.super_.constructor.apply(this, arguments);
    };

    //Extend the base AssetDragAndDrop handler if there is not an existing Image handler.  if it exists extend existing
    var existinghandler = Granite.author.ui.dropController.get("Images");
    if(existinghandler){
        MultiImageDragAndDrop.prototype = existinghandler;
    }else{
        author.util.inherits(MultiImageDragAndDrop,author.ui.assetFinder.AssetDragAndDrop);
    }

    /**
     * handles the situation after a item is dropped on a valid drop zone
     *
     * @param  {Event} event
     */
    MultiImageDragAndDrop.prototype.handleDrop = function (event) {
        var editable = event.currentDropTarget.targetEditable;
        var properties = {};

        for (var i = 0; i < editable.dropTargets.length; i++) {
            var dropTarget = editable.dropTargets[i];
            var updateProperty = dropTarget.name;
            var params = dropTarget.params;
            var j;
            var prefix = "";

            //Multi value check, see if we need to append
            if (params["./multival"] !== undefined) {
                prefix = "+";
                properties[updateProperty + "@Patch"] = "true";
                properties[updateProperty + "@TypeHint"] = "String[]";
            }
            ;

            properties[updateProperty] = prefix + event.path;

            for (j in params) {
                if (params.hasOwnProperty(j)) {
                    properties[j] = params[j];
                }
            }

            for (j in event.param) {
                if (event.param.hasOwnProperty(j)) {
                    properties[j] = event.param[j];
                }
            }
        }

        var updatePromise = author.edit.actions.doUpdate(editable, properties).done(function () {
            console.log("done");
            //    author.edit.actions.doRefresh(editable);
            //    author.selection.select(editable);
        });
        var t = 9;
    };

    function setup(){
        //update the registry to map to our new handler
        author.dropController.register("Images", new MultiImageDragAndDrop());
    };

    setup();
}(jQuery, Granite.author));