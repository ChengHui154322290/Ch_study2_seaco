!function(e){function n(r){var t=[["resolve","done",e.Callbacks({once:1,memory:1}),"resolved"],["reject","fail",e.Callbacks({once:1,memory:1}),"rejected"],["notify","progress",e.Callbacks({memory:1})]],i="pending",o={state:function(){return i},always:function(){return s.done(arguments).fail(arguments),this},then:function(){var r=arguments;return n(function(n){e.each(t,function(t,i){var a=e.isFunction(r[t])&&r[t];s[i[1]](function(){var r=a&&a.apply(this,arguments);if(r&&e.isFunction(r.promise))r.promise().done(n.resolve).fail(n.reject).progress(n.notify);else{var t=this===o?n.promise():this,s=a?[r]:arguments;n[i[0]+"With"](t,s)}})}),r=null}).promise()},promise:function(n){return null!=n?e.extend(n,o):o}},s={};return e.each(t,function(e,n){var r=n[2],a=n[3];o[n[1]]=r.add,a&&r.add(function(){i=a},t[1^e][2].disable,t[2][2].lock),s[n[0]]=function(){return s[n[0]+"With"](this===s?o:this,arguments),this},s[n[0]+"With"]=r.fireWith}),o.promise(s),r&&r.call(s,s),s}var r=Array.prototype.slice;e.when=function(t){var i,o,s,a=r.call(arguments),u=a.length,c=0,l=1!==u||t&&e.isFunction(t.promise)?u:0,f=1===l?t:n(),m=function(e,n,t){return function(o){n[e]=this,t[e]=arguments.length>1?r.call(arguments):o,t===i?f.notifyWith(n,t):--l||f.resolveWith(n,t)}};if(u>1)for(i=new Array(u),o=new Array(u),s=new Array(u);u>c;++c)a[c]&&e.isFunction(a[c].promise)?a[c].promise().done(m(c,s,a)).fail(f.reject).progress(m(c,o,i)):--l;return l||f.resolveWith(s,a),f.promise()},e.Deferred=n}(Zepto);