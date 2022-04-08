var echartsProxyList=[];
var echarts={
        index:-1,
        list:[],
        init:function(ele){
            this.list.push({ele:ele});
            this.index++;
            return this;
        },
        setOption:function(config){
            this.list[this.index].config=config;
            var chart=this.list[this.index];
            echartsProxyList.push(function(echarts){
                echarts.init(chart.ele).setOption(chart.config);
            });
        }
}
function echartsProxy(echarts){
    if(echartsProxyList){
        for(var i=0;i<echartsProxyList.length;i++){
            echartsProxyList[i](echarts);
        }
    }
}