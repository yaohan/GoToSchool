取HealthCheckAssistant分析CGB_CoreBanking_51的结果

分析了TrandReport中APP15POA1,APP15POA3,APP16POA2,APP16POA4这4个Region在14:00与16:00两个采集时间(不同配置)的TotlTran,QRDispRatio,ASCPUUtilRadio三个指标的对比值

点击可以查看详细分析，包括different折线图，before柱状图和after柱状图。
详细分析还没有进行数据更新，使用的是multiRegion的图。


横轴 纵轴 去掉

收数据

下一步工作：
1. 将缩略图与详细分析数据对接，更新详细分析的数据。
2. 将不同配置的数据以表格的形式展示出来
3. 根据传入的数据，获取不同region和不同的配置信息(当前版本为不同的采集时间)，由用户选择比较哪些region在哪两个配置下的指标变化。
4. 根据传入的数据，自动获取不同region和不同配置的数据，动态生成比较图。