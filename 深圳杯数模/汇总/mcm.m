%% 提取原始数据
% 运行时间较长，提取之后可以注释掉
% [num1,raw1,text1]=xlsread('B题附件数据（4.24附件）/1月.xls');
% [num2,raw2,text2]=xlsread('B题附件数据（4.24附件）/2月.xls');
% [num3,raw3,text3]=xlsread('B题附件数据（4.24附件）/3月.xls');
% [num4,raw4,text4]=xlsread('B题附件数据（4.24附件）/4月.xls');

%% 全局变量
day = 91;
minute = 1440;

%% 查找缺失数据
% time = zeros(1,day*minute);
% start1 = 1; end1 = length(num1);
% start2 = end1+1; end2 = end1+length(num2);
% start3 = end2+1; end3 = end2+length(num3);
% start4 = end3+1; end4 = end3+length(num4);
% time(start1:end1) = str2num(cell2mat(text1(2:length(text1),2)));
% time(start2:end2) = str2num(cell2mat(text2(2:length(text2),2)));
% time(start3:end3) = str2num(cell2mat(text3(2:length(text3),2)));
% time(start4:end4) = str2num(cell2mat(text4(2:length(text4),2)));
% 
% dis = time(2:length(time))-time(1:length(time)-1);
% result = find(dis>1 & dis<41);
% 
% source = cell(length(result),5);
% for i=1:length(result)
%     month_1_offset = length(num1);
%     month_2_offset = length(num2) + length(num1);
%     month_3_offset = length(num3)+length(num2) + length(num1);
%     if(result(i)<=month_1_offset)
%         index = result(i)+1;
%         source(i,:) = text1(index,:);
%     else if(result(i)<= month_2_offset)
%             index = result(i)-month_1_offset+1;
%             source(i,:) = text2(index,:);
%         else if(result(i)<=month_3_offset)
%                 index = result(i)-month_2_offset+1;
%                 source(i,:) = text3(index,:);
%             else
%                     index = result(i)-month_3_offset+1;
%                     source(i,:) = text4(index,:);
%             end
%         end
%     end
% end
%% 统计原始数据
%
% DATA = zeros(day*minute,3);
% start1 = 1;     end1 = length(num1);
% start2 = end1+1;end2 = end1+length(num2);
% start3 = end2+1;end3 = end2+length(num3);
% start4 = end3+1;end4 = end3+length(num4);
% DATA(start1:end1,:) = num1;
% DATA(start2:end2,:) = num2;
% DATA(start3:end3,:) = num3;
% DATA(start4:end4,:) = num4;

%% 分类统计原始数据
% LV = zeros(minute,day); %Trading Volume
% SR = zeros(minute,day); %Success Rate
% RT = zeros(minute,day); %Response Time
% 
% for i=1:length(DATA)
%     d = floor(i/minute)+1;
%     m = mod(i,minute);
%     if (m==0)
%         m = minute;
%         d = d-1;
%     end
%     LV(m,d) = DATA(i,1);
%     SR(m,d) = DATA(i,2);
%     RT(m,d) = DATA(i,3);
% end


%% 分析原始数据
% plot(mean(LV));
% plot(var(LV));
% plot(mean(SR));
% plot(var(SR));
% plot(mean(RT));
% plot(var(RT));


% plot(mean(LV'));
% plot(var(LV'));
% plot(mean(SR'));
% plot(var(SR'));
% plot(mean(RT'));
% plot(var(RT'));

%% LV距离平均值的距离
% LV = importdata('LV.mat');
% start1 = 12;end1=91;
% LV_dis = zeros(minute*day,1);
% meanMinute = zeros(minute,1);
% for i=1:minute
%     meanMinute(i,1) = mean(LV(i,start1:end1));
% end
% for j=1:day
%     for i=1:minute;
%         index = (j-1)*minute+i;
%         LV_dis(index,1) = LV(i,j) - meanMinute(i,1);
%     end
% end


% [b x]=hist(LV_dis,100);
% num=numel(LV_dis);
% figure;
% plot(x,b/num);   %概率密度
% c=cumsum(b/num);        %累积分布
% figure; 
% plot(x,c);

% [pdft1,x1] = ksdensity(LV_dis);
% plot(x1,pdft1);

% hold on;
% plot(sort(LV_dis));
% x = ones(1:minute*day,1)*minute*day*0.01;
% y = -1000:2000;
% plot(x,y,'r-');
% plot(mean(LV))
%% SR 
minute_work = 60*12;

%% 

% DATA_work = zeros(minute_work*day,3);
% DATA_rest = zeros(minute_work*day,3);
% count_work = 1;
% count_rest = 1;
% for i=1:minute*day
%     d = floor(i/minute);
%     m = mod(i,minute);
% %     if(m == 0)
% %         m = minute;
% %         d = d-1;
% %     end
%     hour = floor(m/60);
%     if(hour>=8 && hour<20)
%         DATA_work(count_work,:) = DATA(i,:);
%         count_work = count_work + 1;
%     else
%         DATA_rest(count_rest,:) = DATA(i,:);
%         count_rest = count_rest + 1;
%     end
% end



%%  work rest
% 
% SR_work = zeros(minute_work*day,1);
% SR_rest = zeros(minute_work*day,1);
% RT_work = zeros(minute_work*day,1);
% RT_rest = zeros(minute_work*day,1);
% 
% % DATA_work(start1:end1,1:3) = DATA()
% 
% for i=1:91
%     start1 = (i-1)*720+1;
%     end1 = i*720;
%     SR_work(start1:end1,1) = SR(481:1200,i);
%     RT_work(start1:end1,1) = RT(481:1200,i);
% end
% for i=1:91
%     start1 = (i-1)*720+1;
%     end1 = (i-1)*720+480;
%     SR_rest(start1:end1,1) = SR(1:480,i);
%     RT_rest(start1:end1,1) = RT(1:480,i);
%     start2 = (i-1)*720+481;
%     end2 = i*720;
%     SR_rest(start2:end2,1) = SR(1201:1440,i);
%     RT_rest(start2:end2,1) = RT(1201:1440,i);
% end

%% SR
% [b x]=hist(DATA(:,2),100);
% num=numel(DATA(:,2));
% figure;plot(x,b/num);   %概率密度
% c=cumsum(b/num);        %累积分布
% figure; plot(x,c);
% 
% 
% 
% [b x]=hist(SR_work,100);
% num=numel(SR_work);
% figure;plot(x,b/num);   %概率密度
% c=cumsum(b/num);        %累积分布
% figure; plot(x,c);
% % 
% % 
% [b x]=hist(SR_rest,100);
% num=numel(SR_rest);
% figure;plot(x,b/num);   %概率密度
% c=cumsum(b/num);        %累积分布
% figure; plot(x,c);


%% RT
RT_process = log(DATA(:,3)+1)/log(100);
[b x]=hist(RT_process,100);
num=numel(RT_process);
figure;plot(x,b/num);   %概率密度
c=cumsum(b/num);        %累积分布
figure; plot(x,c);
% 
% 
% 
% [b x]=hist(RT_work,100);
% num=numel(RT_work);
% figure;plot(x,b/num);   %概率密度
% c=cumsum(b/num);        %累积分布
% figure; plot(x,c);
% % 
% % 
% [b x]=hist(RT_rest,100);
% num=numel(RT_rest);
% figure;plot(x,b/num);   %概率密度
% c=cumsum(b/num);        %累积分布
% figure; plot(x,c);


% plot(sort(SR_work),'r')
% plot(sort(SR_rest),'b')


%% 标记
% LV_sort = sort(LV_dis);
% SR_work_sort = sort(SR_work);
% SR_rest_sort = sort(SR_rest);
% % RT_sort = sort(DATA(:,3));
% RT_work_sort = sort(RT_work);
% RT_rest_sort = sort(RT_rest);
% confidence = 0.995;
% work_confidence = 0.999;
% threshold_LV_dis = LV_sort(floor(length(LV_dis)*(1-0.95)))
% threshold_LV_dis_max = LV_sort(floor(length(LV_dis)*(0.95)))
% threshold_SR_work = SR_work_sort(floor(length(SR_work_sort)*(1-work_confidence)))
% threshold_SR_rest = SR_rest_sort(floor(length(SR_rest_sort)*(1-confidence)))
% % threshold_RT = RT_sort(floor(length(DATA(:,3))*confidence))
% threshold_RT_work = RT_work_sort(floor(length(RT_work_sort)*work_confidence))
% threshold_RT_rest = RT_rest_sort(floor(length(RT_rest_sort)*confidence))
% DATA_flag = zeros(minute*day,7);
% DATA_flag(:,1:3) = DATA;
% for i=1:length(LV_dis)
%     if(LV_dis(i)<threshold_LV_dis)
%         DATA_flag(i,4) = 1;
%     end
% %     if(LV_dis(i)>threshold_LV_dis_max)
% %         DATA_flag(i,7) = 1;
% %     end
% end
% 
% for i=1:length(DATA(:,2))
% %     if(DATA(i,3)>threshold_RT)
% %        DATA_flag(i,6) = 1;
% %     end
%     hour = floor(mod(i,minute)/60);
%     if(hour>=8 && hour<20)
%         if(DATA(i,2)<threshold_SR_work)
%             DATA_flag(i,5) = 1;
%         end
%         if(DATA(i,3)>threshold_RT_work)
%             DATA_flag(i,6) = 1;
%         end
%     else
%         if(DATA(i,2)<threshold_SR_rest)
%             DATA_flag(i,5) = 1;
%         end
%         if(DATA(i,3)>threshold_RT_rest)
%             DATA_flag(i,6) = 1;
%         end
%     end
% end

%% 二维结果显示
% [i,j,v]=find(RT>threshold);
% size(i);
% result = cell(length(i),5);
% count = 1;
% for in = 1:length(i)
% %     if(SR(i(in),j(in))<0.8)
%         day = j(in)-1;
%         minute = i(in)+1;
%         if(day<9)
%             result(count,:) = text1(day*1440+minute,:);
%         else if(day<37)
%             result(count,:) = text2((day-9)*1440+minute,:);
%             else if(day <68)
%                 result(count,:) = text3((day-37)*1440+minute,:);
%                 else
%                     result(count,:) = text4((day-68)*1440+minute,:);
%                 end
%             end
%         end
%         count = count+1;
% %     end
% end

%% 一维结果显示
% result = find(sum(DATA_flag(:,5:6)') >0);
% length(result)
% source = cell(length(result),8);
% for i=1:length(result)
%     month_1_offset = length(num1);
%     month_2_offset = length(num2) + length(num1);
%     month_3_offset = length(num3)+length(num2) + length(num1);
%     if(result(i)<=month_1_offset)
%         index = result(i)+1;
%         source(i,1:5) = text1(index,:);
%     else if(result(i)<= month_2_offset)
%             index = result(i)-month_1_offset+1;
%             source(i,1:5) = text2(index,:);
%         else if(result(i)<=month_3_offset)
%                 index = result(i)-month_2_offset+1;
%                 source(i,1:5) = text3(index,:);
%             else
%                     index = result(i)-month_3_offset+1;
%                     source(i,1:5) = text4(index,:);
%             end
%         end
%     end
%     source(i,6:8) = num2cell(DATA_flag(result(i),4:6));
% end
% 
% time = str2num(cell2mat(source(:,2)));
% time_dis = time(2:6551)-time(1:6550);
%% 显示总结过
% % % DATA_flag_grid = zeros(day,minute);
% hold on;
% for i=1:length(DATA_flag(:,4))
% % for i=1440*33:1440*40
%     i
%         d = floor(i/minute)+1;
%         m = mod(i,minute);
% %         if(mod(m,60)==0)
% %             x = ones(1,91)*m;
% %             y = 1:91;
% %             plot(x,y,'r-');
% %         end
%         if(m == 0)
%             m = 1440;
%             d = d-1;
%         end
%     if(DATA_flag(i,4)>0)
%         plot(m,d,'bo')
%     end
%     
%     if(DATA_flag(i,5)>0)
%         plot(m,d,'ro')
%     end
%     
%     if(DATA_flag(i,6)>0)
%         plot(m,d,'go')
%     end
%     
% %         if(sum(DATA_flag(i,4)>0))
% %             plot(m,d,'ro')
% %         end
%     
% end


% [X,Y] = meshgrid(1:1440,1:91);
% mesh(X,Y,DATA_flag_grid)

%% 3D显示dis
% LV_dis_grid = zeros(day,minute);
% for i=1:day*minute
%     d = floor(i/minute)+1;
%     m = mod(i,minute);
%     if(m == 0)
%         m = minute;
%         d = d - 1;
%     end
%     LV_dis_grid(d,m) = LV_dis(i,1);
% end
% [X,Y] = meshgrid(1:minute,1:day);
% mesh(X,Y,LV_dis_grid)

        

%%
% LV = importdata('LV.mat');
% SR = importdata('SR.mat');
% RT = importdata('RT.mat');
% 
% DATA = importdata('DATA.mat');
% plot(sort(DATA(:,2)))