# coding=utf-8
import os
import shutil
from sys import exit
import tkFileDialog  
import sys
import re
reload(sys)
sys.setdefaultencoding('utf-8')

#Change String into Int by considering 'G', M' or 'K' or ',' 
def StringToInt(input_string):
    output_int = 0
    if "," in input_string:
        input_string = input_string.replace(",","");   #Remove comma if any
    if "K" in input_string:
        output_int = float(input_string.strip("K")) * 1024
    elif "M" in input_string:
        output_int = float(input_string.strip("M")) * 1024 * 1024
    elif "G" in input_string:
        output_int = float(input_string.strip("G")) * 1024 * 1024 * 1024
    else:
        output_int = float(input_string) 
    return output_int

    
def isnumber(num):
    regex = re.compile(r"^(-?\d+)(\.\d*)?$")
    if re.match(regex,num):
        return True
    else:
            return False
        
        
#Used to copy javascript libraries into the generated report directory  
def copyFiles(sourceDir,  targetDir): 
    if sourceDir.find(".svn") > 0: 
        return 
    for file in os.listdir(sourceDir): 
        sourceFile = os.path.join(sourceDir,  file) 
        targetFile = os.path.join(targetDir,  file) 
        if os.path.isfile(sourceFile): 
            if not os.path.exists(targetDir):  
                os.makedirs(targetDir)  
            if not os.path.exists(targetFile) or(os.path.exists(targetFile) and (os.path.getsize(targetFile) != os.path.getsize(sourceFile))):  
                    open(targetFile, "wb").write(open(sourceFile, "rb").read()) 
        if os.path.isdir(sourceFile): 
            First_Directory = False 
            copyFiles(sourceFile, targetFile)

# 文件夹弹框
InitialDir = os.getcwd()
# 读入第一个文件夹
directoryName = tkFileDialog.askdirectory(title="please select first RawData directory",initialdir = InitialDir)
if(directoryName == ""):
    print("first directory is empty")
    exit(0)
else:
    fileList = os.listdir(directoryName)
if (directoryName.split("/")[-1] == "RawData"):
    print("first directory open success!")
elif (directoryName.split("/")[-1].encode("utf-8").startswith("ReportFile")):
    print("first directory open success!")
    directoryName = directoryName+"/RawData"
    fileList = os.listdir(directoryName)
else:
    print("selected directory is not ReportFile or RawData")
    exit(0)
# 读入第二个文件夹
directoryName2 = tkFileDialog.askdirectory(title="please select second RawData directory",initialdir = InitialDir)
if(directoryName2 == ""):
    print("second directory is empty")
else:
    fileList2 = os.listdir(directoryName2)
if (directoryName2.split("/")[-1] == "RawData"):
    print("second directory open success!")
elif (directoryName2.split("/")[-1].encode("utf-8").startswith("ReportFile")): 
    print("second directory open success!")
    directoryName2 = directoryName2+"/RawData"
    fileList2 = os.listdir(directoryName2)
else:
    print("selected directory is not RawData")
    exit(0)
if(directoryName == directoryName2):
    print("the two directory is same!")
    exit()
# 文件夹内文件不一致
if(fileList != fileList2):
    print("the number of file in directory is different, please check and try again")
    exit()
# 生成目标json
print("start processing......")
data = [];
for fileName in fileList:
    LineList = open(directoryName+"/"+fileName,"r").readlines()
    LineList2 = open(directoryName2+"/"+fileName,"r").readlines()
    indexList = LineList[0].split(",");
    indexList2 = LineList2[0].split(",");
    index = 1
    while (index < len(LineList)):
        line = LineList[index].split(",")
        line2 = LineList2[index].split(",")
        # 读入数据长度不一致
        if(len(line) != len(line2)):
            print("the length of data in two directory is different. scene1:"+lineList[index]+" scene2:"+lineList2[index])
            exit()
        # 读入数据与列名长度不一致
        if(len(indexList) != len(line)):
            print("the length data and type is different index:"+bytes(len(indexList))+" line["+bytes(index)+"]:"+bytes(len(line)))
            exit()
        # 遍历数据
        for i in range(len(line)):
            xIndex = line[0];
            if(indexList[i] == "Applid" and indexList[i] != indexList2[i]):
                print("Applid is different")
                exit()
            indexList[i] = indexList[i].replace("%","").strip();
            line[i] = line[i].replace("\n","").strip();
            line2[i] = line2[i].replace("\n","").strip();
            if(isnumber(line[i])):
                line[i] = float(line[i])
                line2[i] = float(line2[i])
            else:
                num = line[i]
                if((num.isalnum() and  num[1:len(num)-1].isdigit() and (num[-1] == "K" or num[-1] == "G" or num[-1] == "M"))):
                    line[i] = StringToInt(line[i]);
                else:
                    continue;
                    
                num2 = line2[i]
                if((num2.isalnum() and  num2[1:len(num)-1].isdigit() and (num2[-1] == "K" or num2[-1] == "G" or num2[-1] == "M"))):
                    line2[i] = StringToInt(line2[i]);
                else:
                    continue;
                    
            containsFlag = False
            for item in data:
                if(item["type"] == indexList[i]):
                    item["scene1"].append(line[i]);
                    item["scene2"].append(line2[i]);
                    item["xAxis"].append(xIndex)
                    containsFlag = True;
                    break;
            if(not containsFlag):
                temp = {"type":indexList[i],"scene1":[],"scene2":[],"xAxis":[],"dis":[],"base":[],"radio":[]};
                temp["scene1"].append(line[i]);
                temp["scene2"].append(line2[i]);
                temp["xAxis"].append(xIndex)
                data.append(temp)
        index = index + 1
for item in data:
    max1 = max(item["scene1"]);
    max2 = max(item["scene2"]);
    if(max1>max2):
        temp["max"] = max1
    else:
        temp["max"] = max2;
    min1 = min(item["scene1"]);
    min2 = min(item["scene2"]);
    if(min1<min2):
        temp["min"] = min1;
    else:
        temp["min"] = min2;
    for i in range(len(item["scene1"])):
        value1 = float(item["scene1"][i]);
        value2 = float(item["scene2"][i]);
        if(value1>value2):
            item["base"].append(value2)
            dis = value1 - value2;
            item["dis"].append({"value":dis,"itemStyle":{"normal":{"color":"green"}}})
        else:
            item["base"].append(value1)
            dis = value2 - value1
            item["dis"].append({"value":dis,"itemStyle":{"normal":{"color":"red"}}})
            
        if(abs(value2) < 1e-6):
            item["radio"].append(0)
        else:
            item["radio"].append(dis/value2)
for item in data:
    item["avgRadio"] = sum(item["radio"])/len(item["radio"]);
# http://blog.csdn.net/emaste_r/article/details/47373011
new_data = sorted(data,key = lambda e:e.__getitem__('avgRadio'),reverse=True)  
            
reportDirName="ReportFile_compare"
if(os.path.exists(reportDirName)):
    shutil.rmtree(reportDirName)
os.mkdir(reportDirName)
copyFiles("output",reportDirName)
input_peak_template = "output/ReportFile_compare_TEMPLATE.html"
fo_input = open(input_peak_template,"r")
LineList = fo_input.readlines()
HTMLFile = list()
fo_input.close()

for index in range(len(LineList)):
    htmlEntry=LineList[index]
    HTMLFile.append(htmlEntry)
          
if len(HTMLFile) > 1:
    ReportName = "ReportFile_compare.html"
    fo_rep = open(reportDirName+"//"+ReportName, 'w')
    output = ''
    for htmlEntry in HTMLFile:
        if("DATA" in htmlEntry):
            htmlEntry=htmlEntry.replace('DATA',str(data))
        output = output + htmlEntry
    fo_rep.write(output)
    
    ReportName = "ReportFile_compare_sorted.html"
    fo_rep = open(reportDirName+"//"+ReportName, 'w')
    output = ''
    for htmlEntry in HTMLFile:
        if("DATA" in htmlEntry):
            htmlEntry=htmlEntry.replace('DATA',str(new_data))
        output = output + htmlEntry
    fo_rep.write(output)
    fo_rep.close()   
print("Compare Analysis completed!")
print("The Report has been generated in " + reportDirName)
exit()