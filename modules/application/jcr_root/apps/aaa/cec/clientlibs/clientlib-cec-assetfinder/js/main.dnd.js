/*
 * Drag and drop handler with multi value support
 * Special thanks to Mark Ellis for being a BOSS and helping me figure this out
 *
 */
(function ($, author) {
    //Our DND Handler
    function ThingDragAndDrop() {
        this.constructor.super_.constructor.apply(this, arguments);
    };
    //Extend the base AssetDragAndDrop handler
    author.util.inherits(ThingDragAndDrop,author.ui.assetFinder.AssetDragAndDrop);

    /**
     * handles the situation after a item is dropped on a valid drop zone
     *
     * @param  {Event} event
     */
    ThingDragAndDrop.prototype.handleDrop = function (event) {
        console.log("handle drop");

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
            };

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

        author.edit.actions.doUpdate(editable, properties).done(function () {
            author.selection.select(editable);
            author.edit.actions.doRefresh(editable);
        });
    };

    var queryBuilderAPI = '/bin/querybuilder.json?';

    function registerDropControllers(){
        $.getJSON(queryBuilderAPI, {
            type: "cq:Tag",
            path: "/etc/tags/thing"
        }).done(function (data) {
            for (var i=0; i < data.hits.length; i++) {
                // register as a asset tab
                author.dropController.register(data.hits[i]["name"], new ThingDragAndDrop());
            }
        });
    };

    function setup(){
        registerDropControllers();
    };

    setup();
}(jQuery, Granite.author));