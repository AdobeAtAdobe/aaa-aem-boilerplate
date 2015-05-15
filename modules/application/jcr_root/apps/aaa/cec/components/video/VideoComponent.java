package apps.aaa.cec.components.video;

import com.adobe.cq.sightly.WCMUse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.resource.ResourceUtil;

public class VideoComponent extends WCMUse {
    private Resource _referencedResourceContent = null;
    private Resource _metaDataResource = null;
    private ValueMap _metaData = null;
    private String _thumbnailPath = "";
    private String _videoPath = "";

    @Override
    public void activate() throws Exception {
        this._videoPath = this.getProperties().get("videoPath",String.class); //TODO: add support for you tube or vimeo
        if(this._videoPath != null) {
            this._referencedResourceContent = this.getResourceResolver().resolve(this._videoPath).getChild("jcr:content");
            this._metaDataResource = this._referencedResourceContent.getChild("metadata");
            if(this._metaDataResource != null) {
                this._metaData = ResourceUtil.getValueMap(this._metaDataResource);
            }
        };
    }

    public ValueMap getVideoFormat() {
        return this._metaData.get("dc:format",String.class);
    }

    public String getVideoBasePath() {
        if(this._videoPath != null){
            return this._videoPath;
        }else{
            return "";
        }
    }
}