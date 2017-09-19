import matplotlib.pyplot as plt
data = [106.5,107.3,118.8,118.0,103.1,103.4,106.4,114.7,124.1,117.1,108.3,102.8,99.2,98.6,100.4]
newData = [];
plt.plot(data)
sum=0;
disSum=0;
step=3;
for i in range(len(data)):
	if i-step<0:
		for j in 0..i:
		    sum += data[i];
		newData.append(sum/(i+1));

    
    disSum += abs(newData[i]-data[i]);
    print("newData:"+str(newData[i])+"data:"+str(data[i]))
avgDis = disSum/len(data)
print("avgDis:"+str(avgDis))
plt.plot(newData)
plt.show();
