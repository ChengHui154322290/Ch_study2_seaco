//     Zepto.js
//     (c) 2010-2015 Thomas Fuchs
//     Zepto.js may be freely distributed under the MIT license.
//
//     Some code (c) 2005, 2013 jQuery Foundation, Inc. and other contributors

;(function($){
  var slice = Array.prototype.slice

  function Deferred(func) {

    //元组：描述状态、状态切换方法名、对应状态执行方法名、回调列表的关系
      //tuple引自C++/python，和list的区别是，它不可改变 ，用来存储常量集
    var tuples = [
          // action, add listener, listener list, final state
          [ "resolve", "done", $.Callbacks({once:1, memory:1}), "resolved" ],
          [ "reject", "fail", $.Callbacks({once:1, memory:1}), "rejected" ],
          [ "notify", "progress", $.Callbacks({memory:1}) ]
        ],
        state = "pending", //Promise初始状态

        //promise对象，promise和deferred的区别是:
        /*promise只包含执行阶段的方法always(),then(),done(),fail(),progress()及辅助方法state()、promise()等。
          deferred则在继承promise的基础上，增加切换状态的方法，resolve()/resolveWith(),reject()/rejectWith(),notify()/notifyWith()*/
        //所以称promise是deferred的只读副本
        promise = {
            /**
             * 返回状态
             * @returns {string}
             */
          state: function() {
            return state
          },
            /**
             * 成功/失败状态的 回调调用
             * @returns {*}
             */
          always: function() {
            deferred.done(arguments).fail(arguments)
            return this
          },
            /**
             *
             * @returns promise对象
             */
          then: function(/* fnDone [, fnFailed [, fnProgress]] */) {
            var fns = arguments

            //注意，这无论如何都会返回一个新的Deferred只读副本，
            //所以正常为一个deferred添加成功，失败，千万不要用then，用done，fail
            return Deferred(function(defer){
              $.each(tuples, function(i, tuple){
                //i==0: done   i==1: fail  i==2 progress
                var fn = $.isFunction(fns[i]) && fns[i]

                //执行新deferred done/fail/progress
                deferred[tuple[1]](function(){
                    //直接执行新添加的回调 fnDone fnFailed fnProgress
                  var returned = fn && fn.apply(this, arguments)

                    //返回结果是promise对象
                  if (returned && $.isFunction(returned.promise)) {
                     //转向fnDone fnFailed fnProgress返回的promise对象
                     //注意，这里是两个promise对象的数据交流
                      //新deferrred对象切换为对应的成功/失败/通知状态，传递的参数为 returned.promise() 给予的参数值
                    returned.promise()
                      .done(defer.resolve)
                      .fail(defer.reject)
                      .progress(defer.notify)
                  } else {
                    var context = this === promise ? defer.promise() : this,
                        values = fn ? [returned] : arguments
                    defer[tuple[0] + "With"](context, values)//新deferrred对象切换为对应的成功/失败/通知状态
                  }
                })
              })
              fns = null
            }).promise()
          },

            /**
             * 返回obj的promise对象
             * @param obj
             * @returns {*}
             */
          promise: function(obj) {
            return obj != null ? $.extend( obj, promise ) : promise
          }
        },

        //内部封装deferred对象
        deferred = {}

    //给deferred添加切换状态方法
    $.each(tuples, function(i, tuple){
      var list = tuple[2],//$.Callback
          stateString = tuple[3]//   状态 如 resolved

        //扩展promise的done、fail、progress为Callback的add方法，使其成为回调列表
        //简单写法：  promise['done'] = jQuery.Callbacks( "once memory" ).add
        //         promise['fail'] = jQuery.Callbacks( "once memory" ).add  promise['progress'] = jQuery.Callbacks( "memory" ).add
      promise[tuple[1]] = list.add

        //切换的状态是resolve成功/reject失败
        //添加首组方法做预处理，修改state的值，使成功或失败互斥，锁定progress回调列表，
      if (stateString) {
        list.add(function(){
          state = stateString

            //i^1  ^异或运算符  0^1=1 1^1=0，成功或失败回调互斥，调用一方，禁用另一方
        }, tuples[i^1][2].disable, tuples[2][2].lock)
      }

        //添加切换状态方法 resolve()/resolveWith(),reject()/rejectWith(),notify()/notifyWith()
      deferred[tuple[0]] = function(){
        deferred[tuple[0] + "With"](this === deferred ? promise : this, arguments)
        return this
      }
      deferred[tuple[0] + "With"] = list.fireWith
    })

    //deferred继承promise的执行方法
    promise.promise(deferred)

    //传递了参数func，执行
    if (func) func.call(deferred, deferred)

    //返回deferred对象
    return deferred
  }

    /**
     *
     * 主要用于多异步队列处理。
       多异步队列都成功，执行成功方法，一个失败，执行失败方法
       也可以传非异步队列对象

     * @param sub
     * @returns {*}
     */
  $.when = function(sub) {
    var resolveValues = slice.call(arguments), //队列数组 ，未传参数是[]
        len = resolveValues.length,//队列个数
        i = 0,
        remain = len !== 1 || (sub && $.isFunction(sub.promise)) ? len : 0, //子def计数
        deferred = remain === 1 ? sub : Deferred(),//主def,如果是1个fn，直接以它为主def，否则建立新的Def
        progressValues, progressContexts, resolveContexts,
        updateFn = function(i, ctx, val){
          return function(value){
            ctx[i] = this    //this
            val[i] = arguments.length > 1 ? slice.call(arguments) : value   // val 调用成功函数列表的参数
            if (val === progressValues) {
              deferred.notifyWith(ctx, val)  // 如果是通知，调用主函数的通知，通知可以调用多次
            } else if (!(--remain)) {          //如果是成功，则需等成功计数为0，即所有子def都成功执行了，remain变为0，
              deferred.resolveWith(ctx, val)      //调用主函数的成功
            }
          }
        }

      //长度大于1，
    if (len > 1) {
      progressValues = new Array(len) //
      progressContexts = new Array(len)
      resolveContexts = new Array(len)

      //遍历每个对象
      for ( ; i < len; ++i ) {
         //如果是def，
        if (resolveValues[i] && $.isFunction(resolveValues[i].promise)) {
          resolveValues[i].promise()
            .done(updateFn(i, resolveContexts, resolveValues)) //每一个成功
            .fail(deferred.reject)//直接挂入主def的失败通知函数,当某个子def失败时，调用主def的切换失败状态方法，执行主def的失败函数列表
            .progress(updateFn(i, progressContexts, progressValues))
        } else {
          --remain   //非def，直接标记成功，减1
        }
      }
    }

    //都为非def，比如无参数，或者所有子队列全为非def，直接通知成功，进入成功函数列表
    if (!remain) deferred.resolveWith(resolveContexts, resolveValues)
    return deferred.promise()
  }

  $.Deferred = Deferred
})(Zepto)