var cmbc = cmbc || {};

(function(cmbc) {
    var _this = cmbc;
    /*数据加密解密*/
    var key = 'MiN2SHeNg0DiAnS1S6Tb';
    function encryptByDES(message, key) {
        var keyHex = CryptoJS.enc.Utf8.parse(key);
        var encrypted = CryptoJS.DES.encrypt(message, keyHex, {
            mode: CryptoJS.mode.ECB,
            padding: CryptoJS.pad.Pkcs7
        });
        return encrypted.toString();
    }

    function decryptByDES(ciphertext, key) {
        var keyHex = CryptoJS.enc.Utf8.parse(key);
        var decrypted = CryptoJS.DES.decrypt({
            ciphertext: CryptoJS.enc.Base64.parse(ciphertext)
        }, keyHex, {
            mode: CryptoJS.mode.ECB,
            padding: CryptoJS.pad.Pkcs7
        });
        return decrypted.toString(CryptoJS.enc.Utf8);
    }

    _this.initCmbcInfo = function(params) {
        params = params.replace(/\r\n+/g, '');
        params = decodeURIComponent(params);
        params = params.replace(/%2B/g, '+');

        var value = decryptByDES(params, key);
        if (value) {
            value = JSON.parse(value);
            if (value && value.Package && value.Package.Header && value.Package.Header.UUID) {
                var cmbcUUID = value.Package.Header.UUID;
                xigou.setSessionStorage("cmbcuuid", cmbcUUID);
                xigou.setSessionStorage("cmbctuuid", value.Package.Header.TUUID);
            }
        }
    };

    // 获取ChannelCode 如果是
    _this.getChannelCode = function(bSettle) {
        var cmbcUUID = xigou.getSessionStorage("cmbcuuid");
        var value = "";
        if (cmbcUUID) {
            value =  "cmbc";
            if (bSettle) {
                value += cmbcUUID;
            }
        }
        return value;
    };

    // 获取tuuid
    _this.getTuuid = function() {
        var cmbcUUID = xigou.getSessionStorage("cmbcuuid");
        var value = "";
        if (cmbcUUID) {
            value = xigou.getSessionStorage("cmbctuuid");
        }
        return value;
    };

    _this.getUnionType = function() {
        if (_this.getChannelCode(false) == "cmbc") {
            return "4";
        }
        return "";
    };

    _this.getUnionVal = function() {
        if (_this.getChannelCode(false) == "cmbc") {
            return xigou.getSessionStorage("cmbcuuid");
        }
        return "";
    };

    _this.initInfo = function() {
        var param = {"Package":{"Header":{"PurposeRemark":"01","RegSource":"2","ComId":"020160001","TUUID":"2,7","UUID":"4","SendTime":"2016-05-12 10:34:39","SalesChannel":"02","RequestType":"C02","ComSerial":"472"}}}
        var givem = JSON.stringify(param);
        givem = encryptByDES(givem, key).replace(/\+/g,'%2B');
        givem = encodeURIComponent(givem);
        console.log(givem);
    }
})(cmbc);
