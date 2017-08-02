/*******************************************************************************
 * 功能描述：货比达2.0，js验证 创建人：尚军杰 创建日期：2014-09-09
 ******************************************************************************/
var Putil = {};
/** *********************************************************************字符串工具类开始********************************************************************************** */
/**
 * 将空元素转成空串指定的字符串 参数： fromStr，将要转换的字符串 不能为空 toStr，需要将空替换成的字符串，可为空，为空，则默认替换成空串
 */
Putil.nvl = function(fromStr, toStr)
{
    var tstr = "";
    if (fromStr == undefined || fromStr == null || fromStr == "")
    {
        if (toStr == undefined || toStr == null)
        {
            tstr = "";
        } else
        {
            tstr = toStr;
        }
    } else
    {
        tstr = fromStr;
    }
    return tstr;
};

/**
 * 把null的转化为空字符
 */
Putil.toNull = function(fromStr)
{
    if (fromStr == undefined || fromStr == null )
    {
    	return "";
    } else
    {
    	return fromStr;
    }
};
/**
 * replaceAll 用法：str.replaceAll('-',',');将-替换成，
 * 
 * @param s1
 * @param s2
 * @returns
 * 
 */
String.prototype.replaceAll = function(s1, s2)
{
    return this.replace(new RegExp(s1, "gm"), s2);
}

/**
 * function:cTrim(sInputString,iType) description:字符串去空格的函数 parameters:iType：
 * 1=去掉字符串左边的空格 2=去掉字符串左边的空格 0=去掉字符串左边和右边的空格 return value:去掉空格的字符串
 */
Putil.trim = function(sInputString, iType)
{
    var sTmpStr = ' ';
    var i = -1;

    if (iType == 0 || iType == 1)
    {
        while (sTmpStr == ' ')
        {
            ++i;
            sTmpStr = sInputString.substr(i, 1);
        }
        sInputString = sInputString.substring(i);
    }
    if (iType == 0 || iType == 2)
    {
        sTmpStr = ' ';
        i = sInputString.length;
        while (sTmpStr == ' ')
        {
            --i;
            sTmpStr = sInputString.substr(i, 1);
        }
        sInputString = sInputString.substring(0, i + 1);
    }
    return sInputString;
}
/** *********************************************************************字符串工具类结束********************************************************************************** */

/** *********************************************************************数字金额工具类开始********************************************************************************** */
/**
 * 将传入值转换成整数
 */
Putil.parseInteger = function(v)
{
    if (typeof v == 'number')
    {
        return v;
    } else if (typeof v == 'string')
    {
        var ret = parseInt(v);
        if (isNaN(ret) || !isFinite(ret))
        {
            return 0;
        }
        return ret;
    } else
    {
        return 0;
    }
}
/**
 * 将传入值转换成小数,传入值可以是以逗号(,)分隔的数字，此方法将会过滤掉(,)
 */
Putil.parseDotFloat = function(v)
{
    if (typeof v == 'number')
    {
        return v;
    } else if (typeof v == 'string')
    {
        v = v.replace(/[^\d|.]/g, '');
        v = parseFloat(v);

        if (isNan(v) || !isFinite(v))
        {
            return 0
        }
        return ret;
    } else
    {
        return 0;
    }
}
/**
 * js四舍五入保留小数位数
 * 保留的位数可以多为，可以为整数
 * create by：shangjunjie
 */
Putil.roundNumber =function (numberRound, roundDigit) // 四舍五入，保留位数为roundDigit
{
    if (numberRound >= 0)
    {
        var tempNumber = parseInt((numberRound * Math.pow(10, roundDigit) + 0.5)) / Math.pow(10, roundDigit);
        return tempNumber;
    } else
    {
        numberRound1 = -numberRound
        var tempNumber = parseInt((numberRound1 * Math.pow(10, roundDigit) + 0.5)) / Math.pow(10, roundDigit);
        return -tempNumber;
    }
}
/**
 * 格式化小数点后面位数，数字四舍五入，不足位补0
 */
Putil.formatDecimalLen = function(decimalVar, len)
{
    var f_x = parseFloat(decimalVar);
    if (isNaN(f_x))
    {
        console.log('function:formatDecimalLen->parameter error');
        return false;
    }
    var f_x = Math.round(f_x * 100) / 100;
    var s_x = f_x.toString();
    var pos_decimal = s_x.indexOf('.');
    if (pos_decimal < 0)
    {
        pos_decimal = s_x.length;
        s_x += '.';
    }
    while (s_x.length <= pos_decimal + len)
    {
        s_x += '0';
    }
    return s_x;
};
/**
 * 格式化小数点后面位数，数字四舍五入，不足位补0,默认格式长度为2位
 */
Putil.formatDecimalDefaultLen = function(decimalVar)
{
    return Putil.formatDecimalLen(decimalVar, 2);
};

/** *********************************************************************数字金额工具类结束********************************************************************************** */

/** *********************************************************************日期工具类开始*************************************************************************************** */
/**
 * 判断闰年
 * 
 * @param date
 *            Date日期对象
 * @return boolean true 或false
 */
Putil.isLeapYear = function(date)
{
    return (0 == date.getYear() % 4 && ((date.getYear() % 100 != 0) || (date.getYear() % 400 == 0)));
}
/**
 * 日期对象转换为指定格式的字符串
 * 
 * @param f
 *            日期格式,格式定义如下 yyyy-MM-dd HH:mm:ss
 * @param date
 *            Date日期对象, 如果缺省，则为当前时间
 * 
 * YYYY/yyyy/YY/yy 表示年份 MM/M 月份 W/w 星期 dd/DD/d/D 日期 hh/HH/h/H 时间 mm/m 分钟
 * ss/SS/s/S 秒
 * @return string 指定格式的时间字符串
 */
Putil.dateToStr = function(formatStr, date)
{
    formatStr = arguments[0] || "yyyy-MM-dd HH:mm:ss";
    date = arguments[1] || new Date();
    var str = formatStr;
    var Week = [ '日', '一', '二', '三', '四', '五', '六' ];
    str = str.replace(/yyyy|YYYY/, date.getFullYear());
    str = str.replace(/yy|YY/, (date.getYear() % 100) > 9 ? (date.getYear() % 100).toString() : '0' + (date.getYear() % 100));
    str = str.replace(/MM/, date.getMonth() + 1 > 9 ? (date.getMonth() + 1) : '0' + (date.getMonth() + 1));
    str = str.replace(/M/g, date.getMonth());
    str = str.replace(/w|W/g, Week[date.getDay()]);
    str = str.replace(/dd|DD/, date.getDate() > 9 ? date.getDate().toString() : '0' + date.getDate());
    str = str.replace(/d|D/g, date.getDate());
    str = str.replace(/hh|HH/, date.getHours() > 9 ? date.getHours().toString() : '0' + date.getHours());
    str = str.replace(/h|H/g, date.getHours());
    str = str.replace(/mm/, date.getMinutes() > 9 ? date.getMinutes().toString() : '0' + date.getMinutes());
    str = str.replace(/m/g, date.getMinutes());
    str = str.replace(/ss|SS/, date.getSeconds() > 9 ? date.getSeconds().toString() : '0' + date.getSeconds());
    str = str.replace(/s|S/g, date.getSeconds());
    return str;
}
/**
 * 日期计算
 * 
 * @param strInterval
 *            string 可选值 y 年 m月 d日 w星期 ww周 h时 n分 s秒
 * @param num
 *            int 为负数表示相减
 * @param date
 *            Date 日期对象
 * @return Date 返回日期对象
 */
Putil.dateAdd = function(strInterval, num, date)
{
    date = arguments[2] || new Date();
    switch (strInterval)
    {
    case 's':
        return new Date(date.getTime() + (1000 * num));
    case 'n':
        return new Date(date.getTime() + (60000 * num));
    case 'h':
        return new Date(date.getTime() + (3600000 * num));
    case 'd':
        return new Date(date.getTime() + (86400000 * num));
    case 'w':
        return new Date(date.getTime() + ((86400000 * 7) * num));
    case 'm':
        return new Date(date.getFullYear(), (date.getMonth()) + num, date.getDate(), date.getHours(), date.getMinutes(), date.getSeconds());
    case 'y':
        return new Date((date.getFullYear() + num), date.getMonth(), date.getDate(), date.getHours(), date.getMinutes(), date.getSeconds());
    }
}

/**
 * 比较日期差 dtEnd 格式为日期型或者有效日期格式字符串
 * 
 * @param strInterval
 *            string 可选值 y 年 m月 d日 w星期 ww周 h时 n分 s秒
 * @param dtStart
 *            Date 可选值 y 年 m月 d日 w星期 ww周 h时 n分 s秒
 * @param dtEnd
 *            Date 可选值 y 年 m月 d日 w星期 ww周 h时 n分 s秒
 */
Putil.dateDiff = function(strInterval, dtStart, dtEnd)
{
    switch (strInterval)
    {
    case 's':
        return parseInt((dtEnd - dtStart) / 1000);
    case 'n':
        return parseInt((dtEnd - dtStart) / 60000);
    case 'h':
        return parseInt((dtEnd - dtStart) / 3600000);
    case 'd':
        return parseInt((dtEnd - dtStart) / 86400000);
    case 'w':
        return parseInt((dtEnd - dtStart) / (86400000 * 7));
    case 'm':
        return (dtEnd.getMonth() + 1) + ((dtEnd.getFullYear() - dtStart.getFullYear()) * 12) - (dtStart.getMonth() + 1);
    case 'y':
        return dtEnd.getFullYear() - dtStart.getFullYear();
    }
}

/**
 * 字符串转换为日期对象
 * 
 * @param date
 *            Date 格式为yyyy-MM-dd HH:mm:ss，必须按年月日时分秒的顺序，中间分隔符不限制
 */
Putil.strToDate = function(dateStr)
{
    var data = dateStr;
    var reCat = /(\d{1,4})/gm;
    var t = data.match(reCat);
    t[1] = t[1] - 1;
    eval('var d = new Date(' + t.join(',') + ');');
    return d;
}

/**
 * 把指定格式的字符串转换为日期对象yyyy-MM-dd HH:mm:ss
 * 
 */
Putil.strFormatToDate = function(formatStr, dateStr)
{
    var year = 0;
    var start = -1;
    var len = dateStr.length;
    if ((start = formatStr.indexOf('yyyy')) > -1 && start < len)
    {
        year = dateStr.substr(start, 4);
    }
    var month = 0;
    if ((start = formatStr.indexOf('MM')) > -1 && start < len)
    {
        month = parseInt(dateStr.substr(start, 2)) - 1;
    }
    var day = 0;
    if ((start = formatStr.indexOf('dd')) > -1 && start < len)
    {
        day = parseInt(dateStr.substr(start, 2));
    }
    var hour = 0;
    if (((start = formatStr.indexOf('HH')) > -1 || (start = formatStr.indexOf('hh')) > 1) && start < len)
    {
        hour = parseInt(dateStr.substr(start, 2));
    }
    var minute = 0;
    if ((start = formatStr.indexOf('mm')) > -1 && start < len)
    {
        minute = dateStr.substr(start, 2);
    }
    var second = 0;
    if ((start = formatStr.indexOf('ss')) > -1 && start < len)
    {
        second = dateStr.substr(start, 2);
    }
    return new Date(year, month, day, hour, minute, second);
}
/**
 * 日期对象转换为毫秒数
 */
Putil.dateToMillisecond = function(date)
{
    return date.getTime();
}

/**
 * 毫秒转换为日期对象
 * 
 * @param dateVal
 *            number 日期的毫秒数
 */
Putil.millisecondToDate = function(dateVal)
{
    return new Date(dateVal);
}

/**
 * 判断字符串是否为日期格式
 * 
 * @param str
 *            string 字符串
 * @param formatStr
 *            string 日期格式， 如下 yyyy-MM-dd
 */
Putil.isDate = function(str, formatStr)
{
    if (formatStr == null)
    {
        formatStr = "yyyyMMdd";
    }
    var yIndex = formatStr.indexOf("yyyy");
    if (yIndex == -1)
    {
        return false;
    }
    var year = str.substring(yIndex, yIndex + 4);
    var mIndex = formatStr.indexOf("MM");
    if (mIndex == -1)
    {
        return false;
    }
    var month = str.substring(mIndex, mIndex + 2);
    var dIndex = formatStr.indexOf("dd");
    if (dIndex == -1)
    {
        return false;
    }
    var day = str.substring(dIndex, dIndex + 2);
    if (!Putil.isNumber(year) || year > "2100" || year < "1900")
    {
        return false;
    }
    if (!Putil.isNumber(month) || month > "12" || month < "01")
    {
        return false;
    }
    if (day > getMaxDay(year, month) || day < "01")
    {
        return false;
    }
    return true;
}
/**
 * 取得给定年、月的最大天数
 */
Putil.getMaxDay = function(year, month)
{
    if (month == 4 || month == 6 || month == 9 || month == 11)
        return "30";
    if (month == 2)
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0)
            return "29";
        else
            return "28";
    return "31";
}

/**
 * 把日期分割成数组 [年、月、日、时、分、秒]
 */
Putil.dateToArray = function(myDate)
{
    myDate = arguments[0] || new Date();
    var myArray = Array();
    myArray[0] = myDate.getFullYear();
    myArray[1] = myDate.getMonth();
    myArray[2] = myDate.getDate();
    myArray[3] = myDate.getHours();
    myArray[4] = myDate.getMinutes();
    myArray[5] = myDate.getSeconds();
    return myArray;
}

/**
 * 取得日期数据信息 参数 interval 表示数据类型 y 年 M月 d日 w星期 ww周 h时 n分 s秒
 */
Putil.datePart = function(interval, myDate)
{
    myDate = arguments[1] || new Date();
    var partStr = '';
    var Week = [ '日', '一', '二', '三', '四', '五', '六' ];
    switch (interval)
    {
    case 'y':
        partStr = myDate.getFullYear();
        break;
    case 'M':
        partStr = myDate.getMonth() + 1;
        break;
    case 'd':
        partStr = myDate.getDate();
        break;
    case 'w':
        partStr = Week[myDate.getDay()];
        break;
    case 'ww':
        partStr = myDate.WeekNumOfYear();
        break;
    case 'h':
        partStr = myDate.getHours();
        break;
    case 'm':
        partStr = myDate.getMinutes();
        break;
    case 's':
        partStr = myDate.getSeconds();
        break;
    }
    return partStr;
}

/**
 * 取得当前日期所在月的最大天数
 */
this.maxDayOfDate = function(date)
{
    date = arguments[0] || new Date();
    date.setDate(1);
    date.setMonth(date.getMonth() + 1);
    var time = date.getTime() - 24 * 60 * 60 * 1000;
    var newDate = new Date(time);
    return newDate.getDate();
}

/** *********************************************************************日期工具类结束*************************************************************************************** */

/** *********************************************************************格式判断校验开始*************************************************************************************** */
/**
 * 判断字符串是否为空
 */
Putil.isEmpty = function(str)
{
    return str == null || !str || typeof str == undefined || str == '';
}
/**
 * 用途：字符1是否以字符串2开始 输入：str1：字符串；str2：被包含的字符串 返回：如果通过验证返回true,否则返回false
 */
Putil.isFirstMatch = function(str1, str2)
{
    var index = str1.indexOf(str2);
    if (index == 0)
        return true;
    return false;
}
/**
 * 用途：字符1是否以字符串2结束 输入：str1：字符串；str2：被包含的字符串 返回：如果通过验证返回true,否则返回false
 */
Putil.isLastMatch = function(str1, str2)
{
    var index = str1.lastIndexOf(str2);
    if (str1.length == index + str2.length)
        return true;
    return false;
}
/**
 * 用途：字符1是包含字符串2 输入：str1：字符串；str2：被包含的字符串 返回：如果通过验证返回true,否则返回false
 */
Putil.isMatch = function(str1, str2)
{
    var index = str1.indexOf(str2);
    if (index == -1)
        return false;
    return true;
}
/**
 * 用途：检查输入对象的值是否符合整数格式 输入：str 输入的字符串 返回：如果通过验证返回true,否则返回false
 */
Putil.isInteger = function(str)
{
    var regu = /^[-]{0,1}[0-9]{1,}$/;
    return regu.test(str);
}
/**
 * 用途：检查输入字符串是否符合正整数格式 输入： s：字符串 返回： 如果通过验证返回true,否则返回false
 */
Putil.isNumber = function(str)
{
    var reg = new RegExp("^[0-9]*$");
    if (!reg.test(str))
    {
        return false
    }
    return true;
}
/**
 * 用途：检查输入字符串是否是带小数的数字格式,可以是负数 输入： s：字符串 返回： 如果通过验证返回true,否则返回false
 */
Putil.isDecimal = function(str)
{
    if (Putil.isInteger(str))
        return true;
    var re = /^[-]{0,1}(\d+)[\.]+(\d+)$/;
    if (re.test(str))
    {
        if (RegExp.$1 == 0 && RegExp.$2 == 0)
            return false;
        return true;
    } else
    {
        return false;
    }
}
/**
 * 用途：检查输入字符串是否符合金额格式 格式定义为带小数的正数，小数点后最多三位 输入： s：字符串 返回： 如果通过验证返回true,否则返回false
 */
Putil.isMoney = function(s)
{
    var regu = "^[0-9]+[\.][0-9]{0,3}$";
    var re = new RegExp(regu);
    if (re.test(s))
    {
        return true;
    } else
    {
        return false;
    }
}
/**
 * 用途：检查输入字符串是否只由汉字、字母、数字组成 输入： value：字符串 返回： 如果通过验证返回true,否则返回false
 */
Putil.isChinaOrNumbOrLett = function(s)
{// 判断是否是汉字、字母、数字组成
    var regu = "^[0-9a-zA-Z\u4e00-\u9fa5]+$";
    var re = new RegExp(regu);
    if (re.test(s))
    {
        return true;
    } else
    {
        return false;
    }
}
/**
 * 用途：检查输入字符串是否只由英文字母和数字和下划线组成 输入： s：字符串 返回： 如果通过验证返回true,否则返回false
 */
Putil.isNumberOr_Letter = function(s)
{// 判断是否是数字、字母或下划线
    var regu = "^[0-9a-zA-Z\_]+$";
    var re = new RegExp(regu);
    if (re.test(s))
    {
        return true;
    } else
    {
        return false;
    }
}
/**
 * 用途：检查输入字符串是否只由英文字母和数字组成 输入： s：字符串 返回： 如果通过验证返回true,否则返回false
 */
Putil.isNumberOrLetter = function(s)
{// 判断是否是数字或字母
    var regu = "^[0-9a-zA-Z]+$";
    var re = new RegExp(regu);
    if (re.test(s))
    {
        return true;
    } else
    {
        return false;
    }
}
/**
 * 用途：检查输入手机号码是否正确 输入： s：字符串 返回： 如果通过验证返回true,否则返回false
 */
Putil.isMobileNo = function(s)
{
    var regu = /^[1][3][0-9]{9}$/;
    var re = new RegExp(regu);
    if (re.test(s))
    {
        return true;
    } else
    {
        return false;
    }
}
/**
 * 用途：检查输入对象的值是否符合E-Mail格式 输入：str 输入的字符串 返回：如果通过验证返回true,否则返回false
 */
Putil.isEmail = function(str)
{
    var myReg = /^[-_A-Za-z0-9]+@([_A-Za-z0-9]+\.)+[A-Za-z0-9]{2,3}$/;
    if (myReg.test(str))
        return true;
    return false;
}
/**
 * 用途：校验ip地址的格式 输入：strIP：ip地址 返回：如果通过验证返回true,否则返回false；
 */
Putil.isIP = function(strIP)
{
    if (Putil.isNull(strIP))
        return false;
    var re = /^(\d+)\.(\d+)\.(\d+)\.(\d+)$/g // 匹配IP地址的正则表达式
    if (re.test(strIP))
    {
        if (RegExp.$1 < 256 && RegExp.$2 < 256 && RegExp.$3 < 256 && RegExp.$4 < 256)
            return true;
    }
    return false;
}
/**
 * 用途：检查输入对象的值是否符合端口号格式 输入：str 输入的字符串 返回：如果通过验证返回true,否则返回false
 */
Putil.isPort = function(str)
{
    return (Putil.isNumber(str) && str < 65536);
}
/** *********************************************************************格式判断校验结束*************************************************************************************** */
/**
 * 在用户输入时将输入的值强制格式化为两位小数
 * 入参：多个元素的class中间逗号分隔，如['input.dev_heavyCargoUnitPrice','input.dev_lightCargoUnitPrice']
 */
Putil.setInputToDecimal = function(chkEleArry)
{
    var chkEleArry = chkEleArry.join(",");
    $(chkEleArry).unbind().bind("keyup", function()
    {// 触发KEYUP时校验
        var unitprice = parseFloat($(this).val());
        if (isNaN($(this).val()))
        {
            $(this).val($(this).data("oldval"));
        } else
        {
            if ($.trim($(this).val()) == '')
            {
                $(this).val('');
            } else if (parseFloat($(this).val()) < 0)
            {
                $(this).val($(this).data("oldval"));
            } else
            {
                $(this).data("oldval", $(this).val());
            }
            $(this).val($.trim($(this).val()));
        }
    }).bind("blur", function()
    {// 失去焦点是校验
        if ($.trim($(this).val()) == '')
        {
            // $(this).val($(this).data("oldval"));
        }
        if (!Putil.isEmpty($.trim($(this).val())))
        {
            $(this).val($.trim($(this).val()));
            if ($(this).val().indexOf('.') == $(this).val().length - 1)
            {
                $(this).val($(this).val().replace('.', ''));
            }
            $(this).val(Putil.formatDecimalDefaultLen($(this).val()));
        }
    }).bind("focus", function()
    {// 获取焦点是保存原值
        var value = $(this).val();
        if (Putil.isEmpty(value))
        {
            value = '';
        }
        $(this).data("oldval", value);
    });
};

/**
 * 在用户输入时将输入的值强制格式化为两位小数
 * 入参：多个元素的class中间逗号分隔，如['input.dev_heavyCargoUnitPrice','input.dev_lightCargoUnitPrice']
 */
Putil.setInputToNumber = function(chkEleArry)
{
    var chkEleArry = chkEleArry.join(",");
    $(chkEleArry).bind("keyup", function()
    {// 触发KEYUP时校验
        this.value = this.value.replace(/\D/g, '');
    })
};
/**
 * 设置输入框可输入字符长度 strLen 可输入长度
 */
Putil.setInputStrLen = function(strLen)
{
    $(this).unbind().bind("keyup", function()
    {
        var str = $(this).val();
        var len = str.length;
        if (len <= strLen)
        {
            $(this).data("oldval", str);
        } else
        {
            $(this).val($(this).data("oldval"));
        }
    });
}

/**
 * 字符串求长度(全角占2位，半角1位)
 */
Putil.getLength = function(str)
{
    var len = str.length;
    var reLen = 0;
    for ( var i = 0; i < len; i++)
    {
        if (str.charCodeAt(i) < 27 || str.charCodeAt(i) > 126)
        {
            // 全角
            reLen += 2;
        } else
        {
            reLen++;
        }
    }
    return reLen;
}

/**
 * 将数值四舍五入后格式化成金额形式 可以控制小数点位数
 * 
 * @param num
 *            数值(Number或者String)
 * @param n
 *            数值小数点位数
 * @return 金额格式的字符串,如'1,234,567.45'
 * @type String
 */
Putil.formatMoney = function(s, n)
{
    if (Putil.isEmpty(s))
    {
        return '';
    }
    if (!Putil.isDecimal(s))
    {
        return '';
    }
    n = n > 0 && n <= 20 ? n : 2;
    s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";
    var l = s.split(".")[0].split("").reverse(), r = s.split(".")[1];
    t = "";
    for (i = 0; i < l.length; i++)
    {
        t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
    }
    return t.split("").reverse().join("") + "." + r;
}
/**
 * 还原金额格式的数值为原数值
 * 
 * @param s
 *            被还原的金额格式
 * @returns 格式化成金额前的字符，如“123.5”
 */
Putil.reFormatMoney = function(s)
{
    return parseFloat(s.replace(/[^\d\.-]/g, ""));
}
