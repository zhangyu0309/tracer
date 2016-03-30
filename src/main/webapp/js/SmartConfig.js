var TraceConfig = TraceConfig || {};
//动态刷新时间
TraceConfig.Time = 60000;
//分钟间隔  1小时
TraceConfig.MinuteType = 1;
//小时间隔  一天
TraceConfig.HourType = 24;


//获取时间的字符串
TraceConfig.getTimeString = function(preHour){
	var now = new Date();
	var date = new Date(now.getTime() - preHour * 3600 * 1000);
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var day = date.getDate();
	var hour = date.getHours();
	var minute = date.getMinutes();
	var second = date.getSeconds();
	var dateTimeString = year + '-' + month + '-' + day  + ' ' + hour + ':' + minute + ':' + second;
	return dateTimeString;
};

//单位初始化
TraceConfig.initUnits = function(units){
    //  console.info(units.trim()=="bps");
      if(units.trim()=="bps"){
        return "MBps";
        //console.info(units.trim()=="bps");
      }
      if(units.trim()=="Bps"){
        return "MBps";
      }
      if(units.trim()=="KBps"){
        return "MBps";
      }
      if(units.trim()=="b"){
        return "MB";
        //console.info(units.trim()=="bps");
      }
      if(units.trim()=="B"){
        return "MB";
      }
      if(units.trim()=="KB"){
        return "MB";
      }
      return units;
   };
 //单位初始化
   TraceConfig.initUnitsYS = function(units){
	    var jz = 1;
       //  console.info(units.trim()=="bps");
         if(units.trim()=="bps"){
           jz = 1024*1024*8;
           return jz;
           //console.info(units.trim()=="bps");
         }
         if(units.trim()=="Bps"){
           jz = 1024*1024;
           return jz;
         }
         if(units.trim()=="KBps"){
           jz = 1024;
           return jz;
         }
         if(units.trim()=="b"){
            jz = 1024*1024*8;
           return jz;
           //console.info(units.trim()=="bps");
         }
         if(units.trim()=="B"){
            jz = 1024*1024;
           return jz;
         }
         if(units.trim()=="KB"){
           jz = 1024;
           return jz;
         }
      };
 //初始化柱形图或者折线图的数据
TraceConfig.initLineOrBarDataDetail = function(dataDetail,isDJ){
	    var dataSeries = [];
	    for(var i=0;i<dataDetail.length;i++){
	       var tempBean ;
	       if(isDJ){
	         tempBean = {
				name : dataDetail[i].name,
				type : 'line',
				itemStyle: {normal: {areaStyle: {type: 'default'}}},
				data : dataDetail[i].dataList
			 };
	       }else{
	          tempBean = {
				name : dataDetail[i].name,
				type : 'line',
				data : dataDetail[i].dataList
			 };
	       }
			dataSeries.push(tempBean);
	    }
	  //  console.info(dataSeries);
	    return dataSeries;
};
	//初始化Legend数
	TraceConfig.initLegendData = function(dataDetail){
	    var legendData = [];
	    for(var i=0;i<dataDetail.length;i++){
	       legendData.push(dataDetail[i].name);
	    }
	    return legendData;
	};
	//初始化单位
	TraceConfig.initUnit = function(dataDetail){
         for(var i=0;i<dataDetail.length;i++){
	       return dataDetail[i].units;
	     }
	};
	//初始化X轴的信息
	var initX_DATA = function(itemListT){
	   $.each(itemListT,function(i,itemBean){
			if(itemBean.dataList.length>0){
			   return itemBean.x_data;
			}
	   });
	};
	//获取单个监控的数据
	TraceConfig.getDataForItem = function(timeType,tempUrl,graphObj,sortOrder,sortField,limit,startTime,endTime,tempJz,hasFooter,footerId){
		var footerHtml ="<table style=\"font-size: 12px\"><tr><td></td><td>最大值</td><td>最小值</td><td>平均值</td></tr>";
	     $.each(graphObj.items,function(i,m){
		    $.ajax({
			type : "post", //请求方式
			async:false,
			url : sy.contextPath + tempUrl, //发送请求地址
			data : {
				"timeType":timeType,
				"ids" : m.itemid,
				"history" :m.value_type,
				"sortOrder":sortOrder,
				"sortField":sortField,
				"limit":limit,
				"startDate":startTime,
				"endDate":endTime,
				"graphType":graphObj.graphtype,
				"isHistory":1
			},
			//请求成功后的回调函数有两个参数
			success : function(r) {
			    m.dataList = [];
				m.x_data =[];
				if (r.flag) {
				   $.each(r.resultData,function(j,tempDataBean){
				     m.dataList.push((tempDataBean.value/tempJz).toFixed(2));
				     m.x_data.push(tempDataBean.dateString); 
//				     console.info("原来的值:"+tempDataBean.value);
//				     console.info("兑换单位:"+tempJz);
//				     console.info("进制运算完后的值:"+tempDataBean.value/tempJz);
				   });
				   var bean = r.lastData;
				   
				   footerHtml+="<tr><td>"+m.name+"</td><td>"+(bean.value_max/tempJz).toFixed(2)+"</td><td>"+(bean.value_min/tempJz).toFixed(2)+"</td><td>"+(bean.value_avg/tempJz).toFixed(2)+"</td></tr>";
				} 
			}

		    });
//		    TraceConfig.initLineOrBarFooter(graphObj.items);
		 });
	     footerHtml+="</table>";
	     if(hasFooter){
	    	 $("#"+footerId).html(footerHtml); 
	     }
	     
	};
	//初始化X轴
	TraceConfig.initXDATA_ARRAY = function(n){
		var x_dataArr = [];
		//初始化Y轴Data问题
		$.each(n.items,function(i,temp){
		    if(temp.x_data.length>0){
		      x_dataArr = temp.x_data;
		    }
		});
		return x_dataArr;
	};
	//初始化柱形图或者折线图的数据
	TraceConfig.initLineOrBarDataDetail = function(dataDetail,isDJ){
	    var dataSeries = [];
	    for(var i=0;i<dataDetail.length;i++){
	       var tempBean ;
	       if(isDJ==1){
	         tempBean = {
				name : dataDetail[i].name,
				type : 'line',
				itemStyle: {normal: {areaStyle: {type: 'default'}}},
				data : dataDetail[i].dataList
			 };
	       }else{
	          tempBean = {
				name : dataDetail[i].name,
				type : 'line',
				data : dataDetail[i].dataList
			 };
	       }
			dataSeries.push(tempBean);
	    }
	  //  console.info(dataSeries);
	    return dataSeries;
	};
	//初始化列表
	TraceConfig.initLineOrBarTB = function(tbId,timeType,graphBean,startTime,endTime,hasFooter,footerId){
		require([ 'echarts', 'echarts/chart/line','echarts/chart/bar' ], function(ec) {
			var myChart = ec.init(document.getElementById(tbId));
			myChart.showLoading({
				text : '正在努力的读取数据中...',
			});
			//初始化Legend
			
			var dataLegend = TraceConfig.initLegendData(graphBean.items);
			//alert(graphBean.items);
			//获取数据单位
			var tempJzDW = TraceConfig.initUnit(graphBean.items);
			var unit = TraceConfig.initUnits(tempJzDW);
			//获取数据进制
			var jz =  TraceConfig.initUnitsYS(tempJzDW);
			if(jz==undefined){
				jz = 1;
			}
			var tempStartDate = startTime.datetimebox('getValue');
			var tempEndDate = endTime.datetimebox('getValue');
			
			//初始化数据信息
			TraceConfig.getDataForItem(timeType,"getTrendHistoryData.do",graphBean,"ASC","clock",null,tempStartDate,tempEndDate,jz,hasFooter,footerId);
			//console.info(graphBean);
			//初始化X轴信息
			var x_dataArr = TraceConfig.initXDATA_ARRAY(graphBean);
			
			var dataSeries=TraceConfig.initLineOrBarDataDetail(graphBean.items,graphBean.graphtype);
			//console.info(dataSeries);
			option = {
				title : {
					text : graphBean.name,
					x : 'center',
					y : 'top'
				},
				tooltip : {
					trigger : 'axis'
				},
				legend : {
					data : dataLegend,
					y : 'bottom',
					x : 'center'
				},
				calculable : false,
				
				xAxis : [ {
					type : 'category',
					boundaryGap : false,
					data : x_dataArr
				} ],
				yAxis : [ {
					type : 'value',
					axisLabel : {
						formatter : '{value} '+unit
					}
				} ],
				series : dataSeries
			};
			
			// 为echarts对象加载数据 
			myChart.setOption(option);
			myChart.hideLoading();//取消loading
			return graphBean;
		 });
	};
	//初始化表格预览
	TraceConfig.initGridDataTB = function(domId,graphId){
		alert("开始熏染");
		$("#"+domId).html("");
		$.ajax({
			type:"post",
			async:false,
			url : sy.contextPath + "/previewGridGraphs.do?id="+graphId, //发送请求地址
			sucess:function(r){
				$("#"+domId).html(r);
			}
		});
	};
	
	