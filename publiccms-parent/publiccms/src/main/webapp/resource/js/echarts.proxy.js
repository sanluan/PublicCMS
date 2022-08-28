var echartsProxyList=[];
function ChartProxy(ele) {
    this.ele = ele;
    this.setOption = function(config) {
        this.config=config;
        var chart=this;
        if( chart.echart) {
            chart.echart.setOption(chart.config)
            return chart.echart;
        } else {
            echartsProxyList.push(function(echarts) {
                chart.echart=echarts.init(chart.ele);
                chart.echart.setOption(chart.config)
            });
            return chart;
        }
    };
    this.resize = function(){
        if(this.echart) {
            this.echart.resize();
        }
    }
}
var echarts={
    init:function(ele) {
        return new ChartProxy(ele);
    }
}
function echartsProxy(echarts) {
    if(echartsProxyList) {
        for(var i=0;i<echartsProxyList.length;i++){
            echartsProxyList[i](echarts);
        }
    }
}