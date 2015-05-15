(function ($, ns, window, undefined) {

    window.cec = window.cec || {};

    var AdminPropertiesDialog = function (subBasePath) {
        this._src = "/apps/aaa/cec/components/thing/base/_cq_dialog-admin.html"+subBasePath;
    };

    ns.util.inherits(AdminPropertiesDialog, ns.ui.Dialog);

    AdminPropertiesDialog.prototype.getConfig = function () {
        return {
            src: this._src,
            loadingMode: 'auto',
            layout: 'auto'
        };
    };

    AdminPropertiesDialog.prototype.onSuccess = function () {
        window.location.reload();
    };

    window.cec.AdminPropertiesDialog = AdminPropertiesDialog;

}(jQuery, Granite.author, this));

(function ($, ns, window, undefined) {

    window.cec = window.cec || {};

    var AdminPagePropertiesDialog = function (subBasePath) {
        this._src = "/apps/aaa/cec/components/thing_type_selector/_cq_dialog-admin.html"+subBasePath;
    };

    ns.util.inherits(AdminPagePropertiesDialog, ns.ui.Dialog);

    AdminPagePropertiesDialog.prototype.getConfig = function () {
        return {
            src: this._src,
            loadingMode: 'auto',
            layout: 'auto'
        };
    };

    AdminPagePropertiesDialog.prototype.onSuccess = function () {
        window.location.reload();
    };

    window.cec.AdminPagePropertiesDialog = AdminPagePropertiesDialog;

}(jQuery, Granite.author, this));