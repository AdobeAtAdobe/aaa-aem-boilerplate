/*******
 * CEC item finder
 * Special thanks to Mark Ellis for being a BOSS and helping me figure this out
 *******/
(function ($, author, moment, handlebars) {

    function AssetGroup(type,searchPath) {
        var typeObject = type;
        var searchPath = searchPath;

        /**
         * simple "template" function to render an image in the assetfinder
         * @param  {Object} page
         * @return {String} markup for the page
         */
        function pageTemplate(pageData) {
            var lastModified = moment(pageData["jcr:content"]["cq:lastModified"]);
            //var day = moment("1995-12-25");
            var aOutput = [];
            if(pageData["jcr:content"].thingPropertyName == "thing:pull-quote") {
                aOutput.push('<article class="card-asset pull-quote cq-draggable" draggable="true" data-path="' + pageData["jcr:path"] + '" data-asset-group="thing" data-type="' + typeObject.name + '">');
            }else{
                aOutput.push('<article class="card-asset cq-draggable" draggable="true" data-path="' + pageData["jcr:path"] + '" data-asset-group="thing" data-type="' + typeObject.name + '">');
            }
            aOutput.push('<i class="select"></i>');
            aOutput.push('<i class="move"></i>');
            aOutput.push('<div class="label">');
            aOutput.push('<div class="main">');
            aOutput.push('<h4 class="foundation-collection-item-title" itemprop="title">' + pageData["jcr:content"]["jcr:title"] + '</h4>');
            if(pageData["jcr:content"].thingPropertyName == "thing:pull-quote") {
                aOutput.push('<p style="white-space: pre-line;">' + pageData["jcr:content"].content.quote.quote + '</p>');
            }
            aOutput.push('</div>');
            aOutput.push('<div class="info">');
            aOutput.push('<p class="meta-info"></p>');
            aOutput.push('<p class="modified" title="Modified">');
            aOutput.push('<i class="coral-Icon coral-Icon--edit"></i><span class="date" itemprop="lastmodified" data-timestamp="'+lastModified.unix()+'">'+lastModified.fromNow()+'</span>');
            aOutput.push('<span class="user" itemprop="lastmodifiedby">'+pageData["jcr:content"]["cq:lastModifiedBy"]+'</span>');
            aOutput.push('</p>');
            aOutput.push('</div>');
            aOutput.push('</div>');
            aOutput.push('</article>');
            //TODO: convert from a big old js string to a handlebars template that is compiled


            return aOutput.join("");
        };

        /**
         Pre asset type switch hook
         */
        this.setUp = function () {
            console.log("in setup " + type);
        };

        /**
         * Load assets from the public flickr stream. Any search options are ignored.
         *
         * @param query {String} search query
         * @param lowerLimit {Number} lower bound for paging
         * @param upperLimit {Number} upper bound for paging
         * @returns {jQuery.Promise}
         */
        this.loadAssets = function (query, lowerLimit, upperLimit) {
            var def = $.Deferred();

            $.getJSON(queryBuilderAPI, {
                "type":"cq:Page",
                "path":searchPath,
                "orderby":"@jcr:content/cq:lastModified",
                "orderby.sort":"desc",
                "property.value":"thing:"+typeObject.name,
                "property":"jcr:content/thingPropertyName",
                "p.hits":"full",
                "p.nodedepth":"5"
            }).done(function (data) {
                var output = '';
                for (var i=0; i < data.hits.length; i++) {
                    output += pageTemplate(data.hits[i]);
                }

                def.resolve(output);
            });

            return def.promise();
        };
    };

    var queryBuilderAPI = '/bin/querybuilder.json?';
    var searchPath = "/content/cec/things";
    //var typeMapping = {};
    //var assetTypeValueSelected,assetTypeTextSelected;

    /*
    function onAssetTypeChange(e){
        var assetTypeSelected = $(this).find("option:selected");
        assetTypeValueSelected = assetTypeSelected.val();
        assetTypeTextSelected = assetTypeSelected.text();
    };

    function getSelectedType(){
        var selectedType = typeMapping[assetTypeValueSelected];
        return selectedType;
    };
    */

    function registerFinderTypes(){
        $.getJSON(queryBuilderAPI, {
            type: "cq:Tag",
            path: "/etc/tags/thing"
        }).done(function (data) {
            for (var i=0; i < data.hits.length; i++) {
                // register as a asset tab
                author.ui.assetFinder.register(data.hits[i]["name"], new AssetGroup(data.hits[i],searchPath));
                //typeMapping[data.hits[i]["name"]] = data.hits[i]; //store for lookup later
            }
        });
    };

    function setup(){
        registerFinderTypes();
        //author.ui.assetFinder.$el.find(".assetfilter.type").on("selected",onAssetTypeChange);
    };

    setup();

}(jQuery, Granite.author, moment, Handlebars));