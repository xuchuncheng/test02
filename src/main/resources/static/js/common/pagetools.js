var columns=[[
    {field:'id',title:'唯一标识符',hidden:true},
    {field:'name',title:'名称',width:columnsWidth160,sortable:true},
    {field:'type',title:'类型',width:columnsWidth160,sortable:true},
    {field:'createUser',title:'创建人',width:columnsWidth160,sortable:true},
    {field:'description',title:'描述',width:columnsWidth523-20,sortable:true},
    {field:'createTime',title:'创建时间',width:columnsWidth160+10,sortable:true},
    {field:'lastTime',title:'最近修改时间',width:columnsWidth160+10,sortable:true},
    {field:'updateTimes',title:'更新次数',width:columnsWidth160,sortable:true,align:'right'},
    {
        title : '操作',
        field : '_opt',//不对应数据库或json字段，取的名字
        width : columnsWidth160,
        formatter:function(value,row,index){   //格式化函数添加一个操作列
            var btn = '<div id="'+row.id+'" style="display:none">' +
                '<input onClick="searchXmlGridDeleteRecord_Record(\''+row.id+'\',\''+row.type+'\')" type="button" class="removeBtn"/>' +
                '<input type="button" onClick="updateXml_Record(\''+row.id+'\',\''+row.type+'\')" class="editBtn"/>' +
                '</div>';
            return btn;
        }
    }
]];

$('#searchXmlGrid').datagrid({
    //出现行数
    rownumbers:true,
    //只能选择一行，若为false，则可以选择多行
    singleSelect:true,
    //出现分页功能
    pagination:true,
    url:'php/send.php',
    method:'post',
    pageSize:20,
    columns:columns,
    queryParams: {
        options_php:'searchXmlGrid_init',
        filter:filter
    },
    loadMsg:"加载中...",
    emptyMsg:"没有改配置类型的数据",
    //如果没有这个，分页就没有用了，到第二页的时候，数据就为空了
    remoteFilter:true,
    //出现行斑纹
    striped:true,
    remoteSort:false,
    multiSort:true,
    onLoadSuccess:function(){
        $(".datagrid-row").mousemove(function(e){
            var id = $(this).children("td").eq(0).text().trim();
            $("#"+id).css('display','block');
        });
        $(".datagrid-row").mouseout(function(e){
            var id = $(this).children("td").eq(0).text().trim();
            $("#"+id).css('display','none');
        });
    }
});

//这里是用来分页的---------------------------------------------
function pagerFilter(data){
    if ($.isArray(data)){	// is array
        data = {
            total: data.length,
            rows: data
        }
    }
    var target = this;
    var dg = $(target);
    var state = dg.data('datagrid');
    var opts = dg.datagrid('options');
    if (!state.allRows){
        state.allRows = (data.rows);
    }
    if (!opts.remoteSort && opts.sortName){
        var names = opts.sortName.split(',');
        var orders = opts.sortOrder.split(',');
        state.allRows.sort(function(r1,r2){
            var r = 0;
            for(var i=0; i<names.length; i++){
                var sn = names[i];
                var so = orders[i];
                var col = $(target).datagrid('getColumnOption', sn);
                var sortFunc = col.sorter || function(a,b){
                    return a==b ? 0 : (a>b?1:-1);
                };
                r = sortFunc(r1[sn], r2[sn]) * (so=='asc'?1:-1);
                if (r != 0){
                    return r;
                }
            }
            return r;
        });
    }
    var start = (opts.pageNumber-1)*parseInt(opts.pageSize);
    var end = start + parseInt(opts.pageSize);
    data.rows = state.allRows.slice(start, end);
    return data;
}
var loadDataMethod = $.fn.datagrid.methods.loadData;
var deleteRowMethod = $.fn.datagrid.methods.deleteRow;
$.extend($.fn.datagrid.methods, {
    clientPaging: function(jq){
        return jq.each(function(){
            var dg = $(this);
            var state = dg.data('datagrid');
            var opts = state.options;
            opts.loadFilter = pagerFilter;
            var onBeforeLoad = opts.onBeforeLoad;
            opts.onBeforeLoad = function(param){
                state.allRows = null;
                return onBeforeLoad.call(this, param);
            }
            var pager = dg.datagrid('getPager');
            pager.pagination({
                onSelectPage:function(pageNum, pageSize){
                    opts.pageNumber = pageNum;
                    opts.pageSize = pageSize;
                    pager.pagination('refresh',{
                        pageNumber:pageNum,
                        pageSize:pageSize
                    });
                    dg.datagrid('loadData',state.allRows);
                }
            });
            $(this).datagrid('loadData', state.data);
            if (opts.url){
                $(this).datagrid('reload');
            }
        });
    },
    loadData: function(jq, data){
        jq.each(function(){
            $(this).data('datagrid').allRows = null;
        });
        return loadDataMethod.call($.fn.datagrid.methods, jq, data);
    },
    deleteRow: function(jq, index){
        return jq.each(function(){
            var row = $(this).datagrid('getRows')[index];
            deleteRowMethod.call($.fn.datagrid.methods, $(this), index);
            var state = $(this).data('datagrid');
            if (state.options.loadFilter == pagerFilter){
                for(var i=0; i<state.allRows.length; i++){
                    if (state.allRows[i] == row){
                        state.allRows.splice(i,1);
                        break;
                    }
                }
                $(this).datagrid('loadData', state.allRows);
            }
        });
    },
    getAllRows: function(jq){
        return jq.data('datagrid').allRows;
    }
});

//	function getData(){
//		var rows = [];
//
//		//最终发现是这个耗时太长了
//
////		for(var i=1; i<=800; i++){
////			var amount = Math.floor(Math.random()*1000);
////			var price = Math.floor(Math.random()*1000);
////			rows.push({
////				inv: 'Inv No '+i,
////				date: $.fn.datebox.defaults.formatter(new Date()),
////				name: 'Name '+i,
////				amount: amount,
////				price: price,
////				cost: amount*price,
////				note: 'Note '+i
////			});
////		}
//		return rows;
//	}

$('#searchXmlGrid').datagrid('clientPaging');

/*
 * 是它拖慢了整个页面的生成的节奏，它一个人耗掉了95%以上的时间。
 *  $('#searchXmlGrid').datagrid({data:getData()}).datagrid('clientPaging');
 */
// $('#searchXmlGrid').datagrid({data:getData()}).datagrid('clientPaging');
//这里面的.datagrid({data:getData()})要不要一样，不要它的话，速度快乐10倍多。
//     $('#searchXmlGrid').datagrid({data:getData()}).datagrid('clientPaging');
//--------------------------------------------------------------------------

var pager = $('#searchXmlGrid').datagrid().datagrid('getPager');    // get the pager of datagrid
pager.pagination({
    buttons:[{
        iconCls:'icon-edit',
        handler:function(){
            var record=$('#searchXmlGrid').datagrid("getSelected");
            if(record==null){
                $.messager.alert('警告','请选择一行数据!','warning');
                //设置这个对话框的位置。
                var winTop=(document.documentElement.clientHeight/2-77)+'px',winLeft=(document.documentElement.clientWidth/2-145)+'px';
                $('.window.messager-window').css({'position':'fixed','top':winTop,'left':winLeft});
                $('.window-shadow').css({'position':'fixed','top':winTop,'left':winLeft});
            }else{
                updateXml(record.id,record.type);
            }
        }
    },{
        iconCls:'icon-remove',
        handler:function(){
            var record=$('#searchXmlGrid').datagrid("getSelected");
            if(record==null){
                alert("请选择一行数据");
            }else{
                searchXmlGridDeleteRecord(record.id,record.type);
            }
        }
    },{
        iconCls:'icon-add',
        handler:function(){
            var flag=false;
            $.ajax({
                type:"post",
                async:false,
                url:"php/send.php",
                data:{
                    "options_php":"DTDType_select"
                },
                dataType:"json",
                success:function(data){
                    if(data["success"]==true){
                        var ary=data["data"];
                        for(var key in ary){
                            if(ary[key]==filter["type"]){
                                flag=true;
                            }
                        }
                    }
                    else{
                        $.messager.alert('提示','连接服务器失败!','error');
                        //设置这个对话框的位置。
                        var winTop=(document.documentElement.clientHeight/2-77)+'px',winLeft=(document.documentElement.clientWidth/2-145)+'px';
                        $('.window.messager-window').css({'position':'fixed','top':winTop,'left':winLeft});
                        $('.window-shadow').css({'position':'fixed','top':winTop,'left':winLeft});
                    }
                },
                error:function(e){
                    $.messager.alert('提示','连接服务器失败!','error');
                    //设置这个对话框的位置。
                    var winTop=(document.documentElement.clientHeight/2-77)+'px',winLeft=(document.documentElement.clientWidth/2-145)+'px';
                    $('.window.messager-window').css({'position':'fixed','top':winTop,'left':winLeft});
                    $('.window-shadow').css({'position':'fixed','top':winTop,'left':winLeft});
                }
            });


            if(flag){
                addNewXml(filter["type"]);
            }
            else{
                addNewXml("房晓菲");
            }

        }
    }]
});

$('#searchXmlGrid').datagrid('enableFilter');

//将筛选框中进行初始化赋值
$(".datagrid-editable-input.datagrid-filter")[1].value=filter["name"];
$(".datagrid-editable-input.datagrid-filter")[2].value=filter["type"];
$(".datagrid-editable-input.datagrid-filter")[3].value=filter["createUser"];
$(".datagrid-editable-input.datagrid-filter")[4].value=filter["description"];
$(".datagrid-editable-input.datagrid-filter")[5].value=filter["createTime"];
$(".datagrid-editable-input.datagrid-filter")[6].value=filter["lastTime"];
$(".datagrid-editable-input.datagrid-filter")[7].value=filter["updateTimes"];


$(".datagrid-editable-input.datagrid-filter").on("change",function(){
    var filter={};
    var textboxAry=$(".datagrid-editable-input.datagrid-filter");
    for(var i=0;i<textboxAry.length;i++){
        filter[textboxAry[i].name]=textboxAry[i].value;
    }
    filter["type"]=filter["type"];
    $("#tabsPanel").tabs('close','配置信息列表');
    hyperlinkInit(filter);
});
//     if($("#searchXml .datagrid .datagrid-view")[0].scrollHeight<maxHeight-160){
//    	 var searchXmlHeight=(maxHeight-160)+'px';
//         $("#searchXml .datagrid .datagrid-view").css('height',searchXmlHeight);
//
//         $(".datagrid .datagrid-pager").css({'top':searchXmlHeight-40,'position':'absolute'});
//     }
//     else{
////    	 var searchXmlHeight=$("#searchXml .datagrid")[0].scrollHeight;
////    	 $(".datagrid .datagrid-pager").css('top',searchXmlHeight);
//    	 var searchXmlHeight=$("#searchXml .datagrid .datagrid-view")[0].scrollHeight-40;
//    	 $(".datagrid .datagrid-pager").css({'top':searchXmlHeight,'position':'absolute'});
//     }
