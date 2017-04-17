'''                                                         
    (C) COPYRIGHT International Business Machines Corp.   
    2015, 2017                                            
    All Rights Reserved                                   
    Licensed Materials - Property of IBM                  
    5724-B44                                              
                                                          
    US Government Users Restricted Rights -               
    Use, duplication or disclosure restricted by          
    GSA ADP Schedule Contract with IBM Corp.              
                                                          
                                                                        
             NOTICE TO USERS OF THE SOURCE CODE EXAMPLES                
                                                                        
  INTERNATIONAL BUSINESS MACHINES CORPORATION PROVIDES THE SOURCE CODE  
  EXAMPLES, BOTH INDIVIDUALLY AND AS ONE OR MORE GROUPS, AS IS          
  WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED OR IMPLIED,            
  INCLUDING, BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF               
  MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.  THE ENTIRE     
  RISK AS TO THE QUALITY AND PERFORMANCE OF THE SOURCE CODE EXAMPLES,   
  BOTH INDIVIDUALLY AND AS ONE OR MORE GROUPS, IS WITH YOU.  SHOULD     
  ANY PART OF THE SOURCE CODE EXAMPLES PROVE DEFECTIVE, YOU (AND NOT    
  IBM) ASSUME THE ENTIRE COST OF ALL NECESSARY SERVICING, REPAIR OR     
  CORRECTION.  THIS PROGRAM MAY BE USED, EXECUTED, COPIED, MODIFIED     
  AND DISTRIBUTED WITHOUT ROYALTY FOR THE PURPOSE OF DEVELOPING,        
  MARKETING, OR DISTRIBUTING.

@version: Updated on Feb 17th, 2016

@author: Evan Zhou, Xue Liang Zhao

''' 
#######################################################################################################################################
import os
import shutil
from sys import exit
import sys
import json
import platform
import math
import tkFileDialog  
import sys
import re
reload(sys)
sys.setdefaultencoding('utf-8')

#######################################################################################################################################
#Change String into Int by considering 'G', M' or 'K' or ',' 
def StringToInt(input_string):
    output_int = 0
    if "," in input_string:
        input_string = input_string.replace(",","");   #Remove comma if any
    if "K" in input_string:
        output_int = int(input_string.strip("K")) * 1024
    elif "M" in input_string:
        output_int = int(input_string.strip("M")) * 1024 * 1024
    elif "G" in input_string:
        output_int = int(input_string.strip("G")) * 1024 * 1024 * 1024
    else:
        output_int = int(input_string) 
    return output_int
#######################################################################################################################################
#Convert number string into the string with 'G', M' or 'K'
def ConvertGMK(input_string):
    output_string = ''
    value = int(input_string)

    if value < 1024:
        output_string = input_string
    elif value < 1024 * 1024:
        output_string = str('%.2f'%(value/1024)) + "K"
    elif value < 1024 * 1024 * 1024:
        output_string = str('%.2f'%(value/1024/1024)) + "M"
    else:
        output_string = str('%.2f'%(value/1024/1024/1024)) + "G"
    return output_string

#######################################################################################################################################
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
#######################################################################################################################################
#Filer Applid by ApplidPattern 
def ApplidMatch(ApplidPattern, currentApplid): 
    if re.compile(ApplidPattern).match(currentApplid):
        return 1
    return 0
#######################################################################################################################################
class TClassItem:
    __CSVString = '' #CSV string
    Applid = ''      #CICS Application ID
    Date = ''        #Data collection data
    ResTime = ''     #Last rest time
    ColTime = ''     #Data collection time
    TClassName = ''  #Transaction Class name
    TClassDef = 0    #Transactions in this class
    MaxAct = 0       # maximum number of transactions
    PrgThrs = 0      #Purge threshold
    Attaches = 0     #attach requests
    AcptImm = 0      #transactions which are accepted immediately
    PrgImm = 0       #transactions which are purged immediately 
    Queued = 0       #transactions which are queued
    PrgQd = 0        #Purged while queuing
    QuedTime =''     #Queued time
    PckAct = 0       #Peak Active
    PckQued = 0      #Peak Queued
    MaxActTimes = 0  #MXT Active times
    PrgThrsTimes = 0 #times at purge threshold

    def TClassItemCSV(self):  #CSV format
        self.__CSVString = self.Applid + ',' + \
                    self.Date + ',' + \
                    self.ResTime + ',' + \
                    self.ColTime + ',' + \
                    self.TClassName + ',' + \
                    str(self.TClassDef) + ',' + \
                    str(self.MaxAct) + ',' + \
                    str(self.PrgThrs) + ',' + \
                    str(self.Attaches) + ',' + \
                    str(self.AcptImm) + ',' + \
                    str(self.PrgImm) + ',' + \
                    str(self.Queued) + ',' + \
                    str(self.PrgQd) + ',' + \
                    self.QuedTime + ',' + \
                    str(self.PckAct) + ',' + \
                    str(self.PckQued) + ',' + \
                    str(self.MaxActTimes) + ',' + \
                    str(self.PrgThrsTimes)
        return self.__CSVString

#######################################################################################################################################
class TransactionManagerItem:
    __CSVString = '' #CSV string
    Applid = ''      #CICS Application ID
    Date = ''        #Data collection data
    ResTime = ''     #Last rest time
    ColTime = ''     #Data collection time
    TotTran = 0      #Total transaction numer (User + System)
    MXT = 0          #MXT setting
    MXTTimeRch = 0   #MXT reach time
    PkQued = 0       #Peak queued task number
    PkActv = 0       #Peak active task number
    TotlActUsr = 0   #Total active user transaction number
    TotlDly = 0      #Total Delayed transactin number
    MXTRatio = 0.0   #Peak active/MXT
    IntervalValue = 0.0 #Interval in seconds
    TClassItemList = list() #TClass list
    global TCLASSNAME

    def __init__(self):
        self.TClassItemList = list()

    def AppendTClassItem(self,TCItem):
        TCItem.Applid = self.Applid
        TCItem.Date = self.Date
        TCItem.ResTime = self.ResTime
        TCItem.ColTime = self.ColTime
        self.TClassItemList.append(TCItem)

    def PickedTClassItem(self):
        TCItem = TClassItem() 
        for item in self.TClassItemList:
            if item.TClassName == TCLASSNAME:
                TCItem = item
        return TCItem       

    def TMItemCSV(self):
        self.__CSVString = self.Applid + ',' + \
                    self.Date + ',' + \
                    self.ResTime + ',' + \
                    self.ColTime + ',' + \
                    str(self.TotTran) + ',' + \
                    str(self.MXT) + ',' + \
                    str(self.MXTTimeRch) + ',' + \
                    str(self.PkQued) + ',' + \
                    str(self.PkActv) + ',' + \
                    str(self.TotlActUsr) + ',' + \
                    str(self.TotlDly) + ',' + \
                    str(self.MXTRatio)               
        return self.__CSVString


class TransactionManagerReport:
    __Title_TM = "Applid,Date,ResTime,ColTime,TotTran,MXT,MXTTimeRch,PkQued,PkActv,TotlActUsr,TotlDly,MXTRatio%"
    __Title_TC = "Applid,Date,ResTime,ColTime,TClassName,Trandfs,NaxAct,PrgThrs,Attaches,AcptImm,PrgImm,Queued,PrgQd,QuedTime,PckAct,PckQued,MaxActTimes,PrgThrsTimes"

    TMItemList = list()  #Transaction item list

    def __init__(self):
        self.TMItemList = list()
    
    def AppendItem(self, TMItem):         #Append new item
        self.TMItemList.append(TMItem)

    def PrintInCSV(self):                 #Print in CSV format
        if len(self.TMItemList) > 0:
            print (self.__Title)
            for item in self.TMItemList:
                print (item.TMItemCSV())
        else:
            print ("No Data is available.")

    def DumpToFile(self):                 #Dump to a CSV file
        if len(self.TMItemList) > 0:
            fo_raw_tm = open(reportDirName+'//RawData//TransactionManager.csv', 'w')
            fo_raw_tc = open(reportDirName+'//RawData//TClass.csv', 'w')
            raw_content_tm =  self.__Title_TM + "\n"
            raw_content_tc =  self.__Title_TC + "\n"
            for item in self.TMItemList:
                raw_content_tm = raw_content_tm + item.TMItemCSV() +"\n"
                for item2 in item.TClassItemList:
                    raw_content_tc = raw_content_tc + item2.TClassItemCSV() +"\n"
            fo_raw_tm.write(raw_content_tm)
            fo_raw_tc.write(raw_content_tc)
            fo_raw_tm.close()
            fo_raw_tc.close()
        else:
            print ("No Data is available.")    

#######################################################################################################################################
class DispatcherItem:
    __CSVString = ''     #CSV string   
    Applid = ''          #CICS Application ID
    Date = ''            #Data collection data
    ResTime = ''         #Last rest time
    ColTime = ''         #Data collection time
    ASCPU = ''           #Address space CPU time
    ASSRB = ''           #Address space SRB time
    ICV = 0              #ICV setting
    ICVR = 0             #ICVR setting
    
    QRDsp = '00:00:00'    #QR dispatch time
    QRCPU = '00:00:00'    #QR CPU time
    QRCPUUtilRatio = 0.0  #QR CPU/Interval
    QRDispUtilRatio = 0.0 #QR Disp/Interval
    QRDispRatio = 0.0     #QR CPU/dispatch
    
    L8Dsp = '00:00:00'    #L8 dispatch time
    L8CPU = '00:00:00'    #L8 CPU time
    L8CPUUtilRatio = 0.0  #L8 CPU/Interval
    L8DispUtilRatio = 0.0 #L8 Disp/Interval
    L8DispRatio = 0.0     #L8 CPU/dispatch

    ASCPUUtilRatio = 0.0  #Address space TCB CPU/Interval
    SRBCPUUtilRatio = 0.0    #Address space SRB CPU/Interval
    CPUPerUsrTran = 0.0  #CPU time (TCB+SRB) /User transaction
    PkOpTCB = 0          #Peak open TCB usage
    MAXOpTCB = 0         #Max open TCB usage
    OpTCBRatio = 0       #Peak Open TCB/MAXOPENTCB
    ASCPUV = 0.0         #Address space CPU in seconds
    ASSRB = 0.0          #Address space SRC in seconds
    QRDspQueuePk = 0     #Peak QR dispatchable queue length
    QRDspQueueAvg = 0.00 #Average QR dispatchable queue length


    def DispatcherItemCSV(self):
        global CICSVERSION
        self.__CSVString = self.Applid + ',' + \
                    self.Date + ',' + \
                    self.ResTime + ',' + \
                    self.ColTime + ',' + \
                    self.ASCPU + ',' + \
                    self.ASSRB + ',' + \
                    str(self.ICV) + ',' + \
                    str(self.ICVR) + ',' + \
                    self.QRCPU + ',' + \
                    self.QRDsp + ',' + \
                    str(self.QRCPUUtilRatio) + ',' + \
                    str(self.QRDispUtilRatio) + ',' + \
                    str(self.QRDispRatio) + ',' + \
                    self.L8CPU + ',' + \
                    self.L8Dsp + ',' + \
                    str(self.L8CPUUtilRatio) + ',' + \
                    str(self.L8DispUtilRatio) + ',' + \
                    str(self.L8DispRatio) + ',' + \
                    str(self.ASCPUUtilRatio) + ',' + \
                    str(self.SRBCPUUtilRatio) + ',' + \
                    str(self.CPUPerUsrTran) + ',' + \
                    str(self.PkOpTCB) + ',' + \
                    str(self.MAXOpTCB) + ',' + \
                    str(self.OpTCBRatio)
        if CICSVERSION == "6.9.0":
            self.__CSVString = self.__CSVString + ',' + \
                               str(self.QRDspQueuePk) + ',' + \
                               str(self.QRDspQueueAvg)
        return self.__CSVString        

class DispatcherReport:
    __Title = "Applid,Date,ResTime,ColTime,ASCPU,ASSRB,ICV,ICVR,QRCPU,QRDsp,QRCPUUtilRatio%,QRDispUtilRatio%,QRDispRatio%,"+ \
            "L8CPU,L8Dsp,L8CPUUtilRatio%,L8DispUtilRatio%,L8DispRatio%,ASTCBUtilRatio%,ASSRBCPUUtilRatio%,CPUPerUsrTran,PkOpTCB,MAXOpTCB,OpTCBRatio%"
    global CICSVERSION


    DSItemList = list() #Dispatcher item list

    def __init__(self):
        self.DSItemList = list()
    
    def AppendItem(self, DSItem): #Append an item
        self.DSItemList.append(DSItem)

    def PrintInCSV(self): #Print in CSV format
        if len(self.DSItemList) > 0:
            print (self.__Title)
            for item in self.DSItemList:
                print (item.DispatcherItemCSV())
        else:
            print ("No Data is available.")

    def DumpToFile(self): #Dump to CSV file
        if len(self.DSItemList) > 0:
            fo_raw = open(reportDirName+'//RawData//Dispatcher.csv', 'w')
            if CICSVERSION == "6.9.0":
                self.__Title = self.__Title + ",PkQRDspQueue,AvgQRDspQueue"
            raw_content =  self.__Title + "\n"
            for item in self.DSItemList:
                raw_content = raw_content + item.DispatcherItemCSV() +"\n"
            fo_raw.write(raw_content)
            fo_raw.close()    


#######################################################################################################################################
class EnqueueItem:
    __CSVString = ''
    Applid = ''      #CICS Application ID
    Date = ''        #Data collection data
    ResTime = ''     #Last rest time
    ColTime = ''     #Data collection time
    ENQPoolName = '' #Selected ENQ pool ID
    ENQIssd = 0      #ENQs Issued
    ENQWtd = 0       #ENQs Waited
    ENQWTT = ''      #Enqueue Waiting time
    ENQSysWtd = 0    #Sysplex Waited
    ENQSysWtT = ''   #Sysplex Waiting time
    ENQRtnd = 0      #ENQs Retained
    ENQRtnT = ''     #Enqueue retention time
    ImmRejEqBsy = 0  #Immediately rejected ENQBUSY
    ImmRejEqRtn = 0  #Immediately rejected due to RETAINED
    WtRejRtn = 0     #Waiting requests that were rejected due to the enqueue moving into a retained state
    WtRejOp = 0      #Waiting requests that were rejected due to the operator purging the waiting transaction
    WtRejTo = 0      #Waiting requests that were rejected due to the timeout value being exceeded
    ENQBusyRatio = 0.0   #ENQ Busy/ENQIssd
    ENQWttRatio= 0.00    #ENQ Waited/ENQIssd
    ENQRetRatio= 0.00    #ENQ Retained/ENQIssd
    ENQImmRetRatio = 0.00 #ENQ Imme RETAINED/ENQIssd
    ENQImmSuccessRatio = 0.00 #Endqueue Immediate success

    def EnqueueItemCSV(self):
        self.__CSVString = self.Applid + ',' + \
                    self.Date + ',' + \
                    self.ResTime + ',' + \
                    self.ColTime + ',' + \
                    self.ENQPoolName + ',' + \
                    str(self.ENQIssd) + ',' + \
                    str(self.ENQWtd) + ',' + \
                    self.ENQWTT + ',' + \
                    str(self.ENQSysWtd) + ',' + \
                    str(self.ENQSysWtT) + ',' + \
                    str(self.ENQRtnd) + ',' + \
                    self.ENQRtnT + ',' + \
                    str(self.ImmRejEqBsy) + ',' + \
                    str(self.ImmRejEqRtn) + ',' + \
                    str(self.WtRejRtn) + ',' + \
                    str(self.WtRejOp) + ',' + \
                    str(self.WtRejTo) + ',' + \
                    str(self.ENQBusyRatio) + ',' + \
                    str(self.ENQWttRatio) + ',' + \
                    str(self.ENQRetRatio) + ',' + \
                    str(self.ENQImmRetRatio) + ',' + \
                    str(self.ENQImmSuccessRatio)                    
        return self.__CSVString        

class EnqueueReport:
    __Title = "Applid,Date,ResTime,ColTime,ENQPoolName,ENQIssd,ENQWtd,ENQWTT,ENQSysWtd,ENQSysWtT,ENQRtnd,ENQRtnT,ImmRejEqBsy,ImmRejEqRtn,WtRejRtn,WtRejOp,WtRejTo," + \
              "ENQBusyRatio%,ENQWttRatio%,ENQRetRatio%,ENQImmRetRatio%,ENQImmSuccessRatio%"
    NQItemList = list() 

    def __init__(self):
        self.NQItemList = list() 

    def AppendItem(self, NQItem): #Append an item
        self.NQItemList.append(NQItem)

    def PrintInCSV(self): #Print in CSV format
        if len(self.NQItemList) > 0:
            print (self.__Title)
            for item in self.NQItemList:
                print (item.EnqueueItemCSV())
        else:
            print ("No Data is available.")

    def DumpToFile(self): #Dump to a CSV file
        if len(self.NQItemList) > 0:
            fo_raw = open(reportDirName+'//RawData//EnqueueManager.csv', 'w')
            raw_content =  self.__Title + "\n"
            for item in self.NQItemList:
                raw_content = raw_content + item.EnqueueItemCSV() +"\n"
            fo_raw.write(raw_content)
            fo_raw.close()   

#######################################################################################################################################
class StorageManagerItem:
    __CSVString = ''
    Applid = ''      #CICS Application ID
    Date = ''        #Data collection data
    ResTime = ''     #Last rest time
    ColTime = ''     #Data collection time
    STGPROT = ''     #Storage Protection
    TRANISO = ''     #Transaction Isolation
    REENTPG = ''     #Write protection for reentrant programs
    DSALIM = 0       #Current DSA Limit
    PeakDSA = 0      #Peak DSA usage
    EDSALIM = 0      #Current EDSA Limit
    PeakEDSA = 0     #Peak EDSA usage
    MEMLIMIT = ''    #z/OS MEMLIMIT
    CDSASize = 0     #Peak CDSA usage
    UDSASize = 0     #Peak UDSA usage
    SDSASize = 0     #Peak SDSA usage
    RDSASize = 0     #Peak RDSA usage
    ECDSASize = 0    #Peak ECDSA usage
    EUDSASize = 0    #Peak EUDSA usage
    ESDSASize = 0    #Peak ESDSA usage
    ERDSASize = 0    #Peak ERDSA usage
    ETDSASize = 0    #Peak ETDSA usage

    CDSAHWM = 0      #Peak - Lowest Free
    UDSAHWM = 0      #Peak - Lowest Free
    SDSAHWM = 0      #Peak - Lowest Free
    RDSAHWM = 0      #Peak - Lowest Free
    DSAHWMTotal = 0  #Total DSA HWM

    DSASOSTms = 0    #SOS times
    DSASTGVIOLTms = 0 #Storage Violation times

    ECDSAHWM = 0     #Peak - Lowest Free
    EUDSAHWM = 0     #Peak - Lowest Free 
    ESDSAHWM = 0     #Peak - Lowest Free
    ERDSAHWM = 0     #Peak - Lowest Free
    ETDSAHWM = 0     #Peak - Lowest Free
    EDSAHWMTotal = 0  #Total EDSA HWM
    EDSASOSTms = 0   #SOS times
    EDSASTGVIOLTms = 0 #Storage Violation times

    DSARatio = 0.0   #Peak DSA/DSALIM
    EDSARatio = 0.0  #Peak EDSA/EDSALIM
    CDSARatio = 0.0  #Percentage of CDSA
    UDSARatio = 0.0  #Percentage of UDSA
    SDSARatio = 0.0  #Percentage of SDSA
    RDSARatio = 0.0  #Percentage of RDSA
    ECDSARatio = 0.0 #Percentage of ECDSA
    EUDSARatio = 0.0 #Percentage of EUDSA
    ESDSARatio = 0.0 #Percentage of ESDSA
    ERDSARatio = 0.0 #Percentage of ERDSA
    ETDSARatio = 0.0 #Percentage of ETDSA
    DSAHWMRatio = 0.0  #DSA HWM/DSALIM
    EDSAHWMRatio = 0.0 #EDSA HWM/EDSALIM

    CDSAFSTG = 0     #CDSA Free storage
    UDSAFSTG = 0     #UDSA Free storage
    SDSAFSTG = 0     #SDSA Free storage
    RDSAFSTG = 0     #RDSA Free storage

    ECDSAFSTG = 0    #ECDSA Free storage
    EUDSAFSTG = 0    #EUDSA Free storage
    ESDSAFSTG = 0    #ESDSA Free storage
    ERDSAFSTG = 0    #ERDSA Free storage

    CDSALFA = 0      #CDSA Largest Free Area
    UDSALFA = 0      #USDA Largest Free Area    
    SDSALFA = 0      #SDSA Largest Free Area
    RDSALFA = 0      #RDSA Largest Free Area    
    
    ECDSALFA = 0     #ECDSA Largest Free Area
    EUDSALFA = 0     #EUDSA Largest Free Area
    ESDSALFA = 0     #ESDSA Largest Free Area
    ERDSALFA = 0     #ERDSA Largest Free Area

    CDSAFragRatio = 0.0 #CDSALFA/CDSAFSTG
    UDSAFragRatio = 0.0 #UDSALFA/UDSAFSTG
    SDSAFragRatio = 0.0 #SDSALFA/SDSAFSTG
    RDSAFragRatio = 0.0 #RDSALFA/RDSAFSTG

    ECDSAFragRatio = 0.0 #ECDSALFA/ECDSAFSTG
    EUDSAFragRatio = 0.0 #EUDSALFA/EUDSAFSTG
    ESDSAFragRatio = 0.0 #ESDSALFA/ESDSAFSTG
    ERDSAFragRatio = 0.0 #ERDSALFA/ERDSAFSTG   

    GDSASOSTms = 0     #GDSA SOS
    GDSASTGVIOLTms = 0 #GDSA Storage Violation

    def StorageManagerItemCSV(self):
        self.__CSVString = self.Applid + ',' + \
                self.Date + ',' + \
                self.ResTime + ',' + \
                self.ColTime + ',' + \
                self.STGPROT + ',' + \
                self.TRANISO + ',' + \
                self.REENTPG + ',' + \
                str(self.DSALIM) + ',' + \
                str(self.PeakDSA) + ',' + \
                str(self.EDSALIM) + ',' + \
                str(self.PeakEDSA) + ',' + \
                self.MEMLIMIT + ',' + \
                str(self.CDSASize) + ',' + \
                str(self.UDSASize) + ',' + \
                str(self.SDSASize) + ',' + \
                str(self.RDSASize) + ',' + \
                str(self.ECDSASize) + ',' + \
                str(self.EUDSASize) + ',' + \
                str(self.ESDSASize) + ',' + \
                str(self.ERDSASize) + ',' + \
                str(self.ETDSASize) + ',' + \
                str(self.DSARatio) + ',' + \
                str(self.EDSARatio) + ',' + \
                str(self.CDSAHWM) + ',' + \
                str(self.UDSAHWM) + ',' + \
                str(self.SDSAHWM) + ',' + \
                str(self.RDSAHWM) + ',' + \
                str(self.DSASOSTms) + ',' + \
                str(self.DSASTGVIOLTms) + ',' + \
                str(self.ECDSAHWM) + ',' + \
                str(self.EUDSAHWM) + ',' + \
                str(self.ESDSAHWM) + ',' + \
                str(self.ERDSAHWM) + ',' + \
                str(self.ETDSAHWM) + ',' + \
                str(self.EDSASOSTms) + ',' + \
                str(self.EDSASTGVIOLTms) + ',' + \
                str(self.CDSAFragRatio) + ',' + \
                str(self.UDSAFragRatio) + ',' + \
                str(self.SDSAFragRatio) + ',' + \
                str(self.RDSAFragRatio) + ',' + \
                str(self.ECDSAFragRatio) + ',' + \
                str(self.EUDSAFragRatio) + ',' + \
                str(self.ESDSAFragRatio) + ',' + \
                str(self.ERDSAFragRatio) + ',' + \
                str(self.DSAHWMRatio) + ',' + \
                str(self.EDSAHWMRatio) + ',' + \
                str(self.GDSASOSTms) + ',' + \
                str(self.GDSASTGVIOLTms)
        return self.__CSVString

class StorageManagerReport:
    __Title = "Applid,Date,ResTime,ColTime,STGPROT,TRANISO,REENTPG,DSALIM,PeakDSA,EDSALIM,PeakEDSA,MEMLIMIT," + \
            "CDSASize,UDSASize,SDSASize,RDSASize,ECDSASize,EUDSASize,ESDSASize,ERDSASize,ETDSASize,DSARatio%,EDSARatio%," + \
            "CDSAHWM,UDSAHWM,SDSAHWM,RDSAHWM,DSASOSTms,DSASTGVIOLTms,ECDSAHWM,EUDSAHWM,ESDSAHWM,ERDSAHWM,ETDSAHWM,EDSASOSTms,EDSASTGVIOLTms," + \
            "CDSAFragRatio%, UDSAFragRatio%, SDSAFragRatio%, RDSAFragRatio%, ECDSAFragRatio%, EUDSAFragRatio%, ESDSAFragRatio%, ERDSAFragRatio%, DSAHWMRatio%, EDSAHWMRatio%," + \
            "GDSASOSTms, GDSASTGVIOLTms"
    SMItemList = list()

    def __init__(self):
        self.SMItemList = list() 

    def AppendItem(self, SMItem):
        self.SMItemList.append(SMItem)

    def PrintInCSV(self):
        if len(self.SMItemList) > 0:
            print (self.__Title)
            for item in self.SMItemList:
                print (item.StorageManagerItemCSV())
        else:
            print ("No Data is available.")

    def DumpToFile(self):
        if len(self.SMItemList) > 0:
            fo_raw = open(reportDirName+'//RawData//StorageManager.csv', 'w')
            raw_content =  self.__Title + "\n"
            for item in self.SMItemList:
                raw_content = raw_content + item.StorageManagerItemCSV() +"\n"
            fo_raw.write(raw_content)
            fo_raw.close()   

#######################################################################################################################################
class LogManagerItem:
    __CSVString = ''
    Applid = ''      #CICS Application ID
    Date = ''        #Data collection data
    ResTime = ''     #Last rest time
    ColTime = ''     #Data collection time
    DFHLOG = ''      #Name of DFHLOG
    DASDOnly = ''    #DSAD Only?
    AKPFREQ = 0      #AKPFREQ setting
    AKPTaken = 0     #AKP number
    LGDFINT = 0      #LGDFINT setting
    WriteReq = 0     #Write request number
    ByteWritten = 0  #Bytes written
    BufAppend = 0    #Buffer append times
    WaitFullBuf = 0  #Times to wait due to buffer full
    PkForceWts = 0   #The peak number of tasks suspended while requesting a flush of the logstream buffer currently in use 
    RetryError = 0   #MVS system logger retryable errors occurred when a block of data was being written to the logstream 
    ShuntWriteRequest = 0 #Write request against DFHSHUNT


    def LogManagerItemCSV(self):
        self.__CSVString = self.Applid + ',' + \
                self.Date + ',' + \
                self.ResTime + ',' + \
                self.ColTime + ',' + \
                self.DFHLOG + ',' + \
                self.DASDOnly + ',' + \
                str(self.AKPFREQ) + ',' + \
                str(self.AKPTaken) + ',' + \
                str(self.LGDFINT) + ',' + \
                str(self.WriteReq) + ',' + \
                str(self.ByteWritten) + ',' + \
                str(self.BufAppend) + ',' + \
                str(self.WaitFullBuf) + ',' + \
                str(self.PkForceWts) + ',' + \
                str(self.RetryError) + ',' + \
                str(self.ShuntWriteRequest)
        return self.__CSVString

class LogManagerReport:
    __Title = "Applid,Date,ResTime,ColTime,DFHLOG,DASDOnly,AKPFREQ,AKPTaken,LGDFINT,WriteReq,ByteWritten,BufAppend,WaitFullBuf,PkForceWts,RetryError,ShuntWriteRequest"
    LGItemList = list()

    def __init__(self):
        self.LGItemList = list() 

    def AppendItem(self, LGItem):  #Append an item
        self.LGItemList.append(LGItem) 

    def PrintInCSV(self):          #Print in CSV format
        if len(self.LGItemList) > 0:
            print (self.__Title)
            for item in self.LGItemList:
                print (item.LogManagerItemCSV())
        else:
            print ("No Data is available.")

    def DumpToFile(self):          #Dump to a CSV file
        if len(self.LGItemList) > 0:
            fo_raw = open(reportDirName+'//RawData//LogManager.csv', 'w')
            raw_content =  self.__Title + "\n"
            for item in self.LGItemList:
                raw_content = raw_content + item.LogManagerItemCSV() +"\n"
            fo_raw.write(raw_content)
            fo_raw.close()  

#######################################################################################################################################
class DB2ENTRYItem:
    __CSVString = ''
    Applid = ''         #CICS Application ID
    Date = ''           #Data collection data
    ResTime = ''        #Last rest time
    ColTime = ''        #Data collection time
    DB2EntryName = ''   #DBEntry name
    AccountRec = ''     #Accounting record
    ThreadWait = ''     #Thread wait
    ThreadPrty = ''     #Thread priority
    CallCount = 0       #Numbers of SQL call
    SignonCount = 0     #Numbers of Signon
    PartialSignon = 0   #Numbers of Partial singons
    TwoPCommitCount = 0 #2PC count
    AbortCount = 0      #Abort count
    OnePCommitCount = 0 #1PC count
    ThreadCreate = 0    #Number of create thread requests
    ThreadReuse = 0     #Times to reuse a thread
    ThreadTerms = 0     #Number of terminate thread requests
    ThreadWtOrOF = 0    #Times all threads were busy and a transaction had to wait for a thread to become available, or overflow to the pool

    ThreadLimit = 0     #Thread Limit for this DB2ENTRY
    ThreadHWM = 0       #Peak active threads
    PthreadLimit = 0    #Protected thread limit
    PthreadHWM = 0      #Peak active protected threads
    Readyq = 0          #Peak number of CICS tasks that waited for a thread to become available on this DB2ENTRY
    Reuselm = 0         #Times the reuselimit has been reached by a thread for this DB2ENTRY
    CommitRatio = 0.0   #Percentage of success commit
    ThreadRatio = 0.0   #Percentage of thread limit usage
    PthreadRatio = 0.0  #Percentage of pthread usage

    def DB2ENTRYItemCSV(self):
        self.__CSVString = self.Applid + ',' + \
                self.Date + ',' + \
                self.ResTime + ',' + \
                self.ColTime + ',' + \
                self.DB2EntryName + ',' + \
                self.AccountRec + ',' + \
                self.ThreadWait + ',' + \
                self.ThreadPrty + ',' + \
                str(self.CallCount) + ',' + \
                str(self.TwoPCommitCount) + ',' + \
                str(self.AbortCount) + ',' + \
                str(self.OnePCommitCount) + ',' + \
                str(self.ThreadCreate) + ',' + \
                str(self.ThreadReuse) + ',' + \
                str(self.ThreadTerms) + ',' + \
                str(self.ThreadLimit) + ',' + \
                str(self.ThreadHWM) + ',' + \
                str(self.PthreadLimit) + ',' + \
                str(self.PthreadHWM) + ',' + \
                str(self.Readyq) + ',' + \
                str(self.Reuselm) + ',' + \
                str(self.CommitRatio) + ',' + \
                str(self.ThreadRatio) + ',' + \
                str(self.PthreadRatio)
        return self.__CSVString

class DB2CONNItem:
    __CSVString = ''
    Applid = ''      #CICS Application ID
    Date = ''        #Data collection data
    ResTime = ''     #Last rest time
    ColTime = ''     #Data collection time
    DB2Conn = ''     #DB2CONN name
    TCBLimit = 0     #TCBLIMIT
    PkConnWithTCB = 0 #Peak Connection
    PkConnReadyq = 0  #Peak number of tasks on the Conn Readyq
    PoolPriority = '' #Priority of the pool thread
    PkPoolTasks = 0   #Peak number of Pool Threads in use
    PkPoolReadyq = 0  #Peak number of tasks on the Pool Readyq
    PkCmdThreads = 0  #Peak number of Command Threads
    DB2TCBRatio = 0.0 #Percentage of TCBLIMIT usage
    DB2ENTRYItemList = list()

    def __init__(self):
        self.DB2ENTRYItemList = list()

    def AppendDB2Entry(self,DB2ENTRYItem):
        DB2ENTRYItem.Applid = self.Applid
        DB2ENTRYItem.Date = self.Date
        DB2ENTRYItem.ResTime = self.ResTime
        DB2ENTRYItem.ColTime = self.ColTime       
        self.DB2ENTRYItemList.append(DB2ENTRYItem)

    def PeakDEItem(self):
        PkDEItem = DB2ENTRYItem() 
        for item in self.DB2ENTRYItemList:
            if item.CallCount > PkDEItem.CallCount:
                PkDEItem = item
        return PkDEItem

    def DB2CONNItemCSV(self):
        self.__CSVString = self.Applid + ',' + \
                self.Date + ',' + \
                self.ResTime + ',' + \
                self.ColTime + ',' + \
                self.DB2Conn + ',' + \
                str(self.TCBLimit) + ',' + \
                str(self.PkConnWithTCB) + ',' + \
                str(self.PkConnReadyq) + ',' + \
                self.PoolPriority + ',' + \
                str(self.PkPoolTasks) + ',' + \
                str(self.PkPoolReadyq) + ',' + \
                str(self.PkCmdThreads) + ',' + \
                str(self.DB2TCBRatio)
        return self.__CSVString

class DB2Report:
    __Title_DC = "Applid,Date,ResTime,ColTime,DB2Conn,TCBLimit,PkConnWithTCB,PkConnReadyq,PoolPriority,PkPoolTasks,PkPoolReadyq,PkCmdThreads,DB2TCBRatio%"
    __Title_DE = "Applid,Date,ResTime,ColTime,DB2EntryName,AccountRec,ThreadWait,ThreadPrty,CallCount,TwoPCommitCount,AbortCount,OnePCommitCount,ThreadCreate,ThreadReuse," + \
                      "ThreadTerms,ThreadLimit,ThreadHWM,PthreadLimit,PthreadHWM,Readyq,Reuselm,CommitRatio%,ThreadRatio%,PthreadRatio%"    
    DB2CONNItemList = list()

    def __init__(self):
        self.DB2CONNItemList = list() 

    def AppendItem(self, DB2CONNItem):        #Append an item
        self.DB2CONNItemList.append(DB2CONNItem)

    def PrintInCSV(self):                     #Print in CSV format
        if len(self.DB2CONNItemList) > 0:
            print (self.__Title)
            for item in self.DB2CONNItemList:
                print (item.DB2CONNItemCSV())
        else:
            print ("No Data is available.")

    def DumpToFile(self):                     #Dump to CSV file
        if len(self.DB2CONNItemList) > 0:
            fo_raw_con = open(reportDirName+'//RawData//DB2CONN.csv', 'w')
            fo_raw_ent = open(reportDirName+'//RawData//DB2ENTRY.csv', 'w')
            raw_content_con =  self.__Title_DC + "\n"
            raw_content_ent =  self.__Title_DE + "\n"
            for item in self.DB2CONNItemList:
                raw_content_con = raw_content_con + item.DB2CONNItemCSV() +"\n"
                for item2 in item.DB2ENTRYItemList:
                    raw_content_ent = raw_content_ent + item2.DB2ENTRYItemCSV() + "\n"
            fo_raw_con.write(raw_content_con)
            fo_raw_ent.write(raw_content_ent)
            fo_raw_con.close() 
            fo_raw_ent.close()

#######################################################################################################################################
class DumpItem:
    __CSVString = ''
    Applid = ''      #CICS Application ID
    Date = ''        #Data collection data
    ResTime = ''     #Last rest time
    ColTime = ''     #Data collection time
    SysDumpTaken = 0 #System dumps taken
    SysDumpSupp = 0  #System dumps suppressed
    TranDumpTaken = 0 #Transaction dump taken
    TranDumpSupp = 0 #Transaction dump suppressed

    def DumpItemCSV(self):
        self.__CSVString = self.Applid + ',' + \
                self.Date + ',' + \
                self.ResTime + ',' + \
                self.ColTime + ',' + \
                str(self.SysDumpTaken) + ',' + \
                str(self.SysDumpSupp) + ',' + \
                str(self.TranDumpTaken) + ',' + \
                str(self.TranDumpSupp)
        return self.__CSVString

class DumpReport:
    __Title = "Applid,Date,ResTime,ColTime,SysDumpTaken,SysDumpSupp,TranDumpTaken,TranDumpSupp"
    DumpItemList = list()

    def __init__(self):
        self.DumpItemList = list()

    def AppendItem(self, DumpItem):  #Append an item
        self.DumpItemList.append(DumpItem) 

    def PrintInCSV(self):          #Print in CSV format
        if len(self.DumpItemList) > 0:
            print (self.__Title)
            for item in self.DumpItemList:
                print (item.DumpItemCSV())
        else:
            print ("No Data is available.")

    def DumpToFile(self):          #Dump to a CSV file
        if len(self.DumpItemList) > 0:
            fo_raw = open(reportDirName+'//RawData//Dump.csv', 'w')
            raw_content =  self.__Title + "\n"
            for item in self.DumpItemList:
                raw_content = raw_content + item.DumpItemCSV() +"\n"
            fo_raw.write(raw_content)
            fo_raw.close()  

#######################################################################################################################################
class TSQItem:
    __CSVString = ''
    Applid = ''      #CICS Application ID
    Date = ''        #Data collection data
    ResTime = ''     #Last rest time
    ColTime = ''     #Data collection time
    PutqMainReqs = 0 #Put main TSQ requests
    GetqMainReqs = 0 #Get main TSQ requests
    TSMAINLIMIT = 0  #Main stroage limit for TSQ
    TSMAINLIMITTms = 0 #Times to reach TSQ storage limit
    PeakUsedTSMAIN = 0 #Storage used
    TSMAINLIMITRatio = 0.0  #Percentage of TSMAINLIMIT usaged
    AutoDelTSQs = 0  #Number of TS queues auto-deleted
    PutqAuxReqs = 0  #Put/Putq auxiliary storage requests
    GetqAuxReqs = 0  #Get/Getq auxiliary storage requests
    QueueCreTms = 0  #Times queues created
    CISize = 0       #Control Interval size
    WriteMTCI = 0    #Writes more than control interval
    LongestRec = 0   #Longest auxiliary temp storage record 
    AuxStgExh = 0    #Times aux. storage exhausted
    TSCompress = 0   #Number of temp storage compressions
    BufferWait = 0   #Buffer waits
    StringWait = 0   #String wait
    IOErrorTSQDS = 0 #IO error on TS dataset
    GetqShareReqs = 0   #Shared read requests
    PutqShareReqs = 0  #Shared write requests

    def TSQItemCSV(self):
        self.__CSVString = self.Applid + ',' + \
                self.Date + ',' + \
                self.ResTime + ',' + \
                self.ColTime + ',' + \
                str(self.PutqMainReqs) + ',' + \
                str(self.GetqMainReqs) + ',' + \
                str(self.TSMAINLIMIT) + ',' + \
                str(self.TSMAINLIMITTms) + ',' + \
                str(self.PeakUsedTSMAIN) + ',' + \
                str(self.TSMAINLIMITRatio)+ ',' + \
                str(self.AutoDelTSQs) + ',' + \
                str(self.PutqAuxReqs) + ',' + \
                str(self.GetqAuxReqs) + ',' + \
                str(self.QueueCreTms) + ',' + \
                str(self.CISize) + ',' + \
                str(self.WriteMTCI) + ',' + \
                str(self.LongestRec) + ',' + \
                str(self.AuxStgExh) + ',' + \
                str(self.TSCompress) + ',' + \
                str(self.BufferWait) + ',' + \
                str(self.StringWait) + ',' + \
                str(self.IOErrorTSQDS) + ',' + \
                str(self.GetqShareReqs) + ',' + \
                str(self.PutqShareReqs)
        return self.__CSVString

class TSQReport:
    __Title = "Applid,Date,ResTime,ColTime,PutqMainReqs,GetqMainReqs,TSMAINLIMIT,TSMAINLIMITTms,PeakUsedTSMAIN,TSMAINLIMITRatio%,AutoDelTSQs,PutqAuxReqs,GetqAuxReqs," + \
              "QueueCreTms,CISize,WriteMTCI,LongestRec,AuxStgExh,TSCompress,BufferWait,StringWait,IOErrorTSQDS,GetqShareReqs,PutqShareReqs"
    TSQItemList = list()

    def __init__(self):
        self.TSQItemList = list()

    def AppendItem(self, TSQItem):  #Append an item
        self.TSQItemList.append(TSQItem) 

    def PrintInCSV(self):          #Print in CSV format
        if len(self.TSQItemList) > 0:
            print (self.__Title)
            for item in self.TSQItemList:
                print (item.TSQItemCSV())
        else:
            print ("No Data is available.")

    def DumpToFile(self):          #Dump to a CSV file
        if len(self.TSQItemList) > 0:
            fo_raw = open(reportDirName+'//RawData//TSQueue.csv', 'w')
            raw_content =  self.__Title + "\n"
            for item in self.TSQItemList:
                raw_content = raw_content + item.TSQItemCSV() +"\n"
            fo_raw.write(raw_content)
            fo_raw.close()  

#######################################################################################################################################
class TDQItem:
    __CSVString = ''
    Applid = ''      #CICS Application ID
    Date = ''        #Data collection data
    ResTime = ''     #Last rest time
    ColTime = ''     #Data collection time
    PutqIntraReqs = 0   #Writes to intrapartition dataset
    GetqIntraReqs = 0    #Reads from intrapartition dataset
    IOErrors = 0     #I/O errors
    BufferWait = 0 #Intrapartition buffer waits 
    StringWait = 0    #String waits
    NOSPACETms = 0   #NOSPACE condition 
    IntraBuffers = 0 #Intra buffers
    PeakIntraBuffData = 0 #Peak valid
    StringNum = 0    #String number


    def TDQItemCSV(self):
        self.__CSVString = self.Applid + ',' + \
                self.Date + ',' + \
                self.ResTime + ',' + \
                self.ColTime + ',' + \
                str(self.NOSPACETms) + ',' + \
                str(self.PutqIntraReqs) + ',' + \
                str(self.GetqIntraReqs) + ',' + \
                str(self.IntraBuffers) + ',' + \
                str(self.PeakIntraBuffData) + ',' + \
                str(self.BufferWait) + ',' + \
                str(self.StringNum) + ',' + \
                str(self.StringWait)
        return self.__CSVString

class TDQReport:
    __Title = "Applid,Date,ResTime,ColTime,NOSPACETms,PutqIntraReqs,GetqIntraReqs,IntraBuffers,PeakIntraBuffData,BufferWait,StringNum,StringWait"
    TDQItemList = list()

    def __init__(self):
        self.TDQItemList = list()

    def AppendItem(self, TDQItem):  #Append an item
        self.TDQItemList.append(TDQItem) 

    def PrintInCSV(self):          #Print in CSV format
        if len(self.TDQItemList) > 0:
            print (self.__Title)
            for item in self.TDQItemList:
                print (item.TDQItemCSV())
        else:
            print ("No Data is available.")

    def DumpToFile(self):          #Dump to a CSV file
        if len(self.TDQItemList) > 0:
            fo_raw = open(reportDirName+'//RawData//TDQueue.csv', 'w')
            raw_content =  self.__Title + "\n"
            for item in self.TDQItemList:
                raw_content = raw_content + item.TDQItemCSV() +"\n"
            fo_raw.write(raw_content)
            fo_raw.close()  

#######################################################################################################################################
class ConnectionItem:
    Applid = ''      #CICS Application ID
    Date = ''        #Data collection data
    ResTime = ''     #Last rest time
    ColTime = ''     #Data collection time  
    TotalAlloc = 0   #TOTAL NUMBER OF ALLOCATES
    QueuedAlloc = 0  #QUEUED ALLOCATES
    FailedLinkAlloc = 0 #FAILED LINK ALLOCATES
    FailedAllocSessUsed = 0 #FAILED ALLOCATES DUE TO SESSIONS IN USE  

    def ConnectionCSV(self):
        self.__CSVString = self.Applid + ',' + \
                self.Date + ',' + \
                self.ResTime + ',' + \
                self.ColTime + ',' + \
                str(self.TotalAlloc) + ',' + \
                str(self.QueuedAlloc) + ',' + \
                str(self.FailedLinkAlloc) + ',' + \
                str(self.FailedAllocSessUsed)
        return self.__CSVString

class ConnectionReport:
    __Title = "Applid,Date,ResTime,ColTime,TotalAlloc,QueuedAlloc,FailedLinkAlloc,FailedAllocSessUsed"
    CONItemList = list()

    def __init__(self):
        self.CONItemList = list()

    def AppendItem(self, CONItem):  #Append an item
        self.CONItemList.append(CONItem) 

    def PrintInCSV(self):          #Print in CSV format
        if len(self.CONItemList) > 0:
            print (self.__Title)
            for item in self.CONItemList:
                print (item.ConnectionCSV())
        else:
            print ("No Data is available.")

    def DumpToFile(self):          #Dump to a CSV file
        if len(self.CONItemList) > 0:
            fo_raw = open(reportDirName+'//RawData//Connection.csv', 'w')
            raw_content =  self.__Title + "\n"
            for item in self.CONItemList:
                raw_content = raw_content + item.ConnectionCSV() +"\n"
            fo_raw.write(raw_content)
            fo_raw.close()      

#######################################################################################################################################
class IntervalItem:
    __CSVString = ''
    Applid = ''      #CICS Application ID
    Date = ''        #Data collection data
    ResTime = ''     #Last rest time
    ColTime = ''     #Data collection time
    TMItem = TransactionManagerItem()
    DSItem = DispatcherItem()
    NQItem = EnqueueItem()
    SMItem = StorageManagerItem()
    LGItem = LogManagerItem()
    DCItem = DB2CONNItem()
    DUItem = DumpItem()
    TSQItem = TSQItem()
    TDQItem = TDQItem()
    CONItem = ConnectionItem()

    def __init__(self):
        self.TMItem = TransactionManagerItem()
        self.DSItem = DispatcherItem()
        self.NQItem = EnqueueItem()
        self.SMItem = StorageManagerItem()
        self.LGItem = LogManagerItem()
        self.DCItem = DB2CONNItem()    
        self.DUItem = DumpItem()    
        self.TSQItem = TSQItem()
        self.TDQItem = TDQItem()
        self.CONItem = ConnectionItem()

 
    def UpdateIntervalInfo(self):    #Update the interval information in each item
        self.TMItem.Applid = self.Applid
        self.TMItem.Date = self.Date
        self.TMItem.ResTime = self.ResTime
        self.TMItem.ColTime = self.ColTime

        self.DSItem.Applid = self.Applid
        self.DSItem.Date = self.Date
        self.DSItem.ResTime = self.ResTime
        self.DSItem.ColTime = self.ColTime
        
        self.NQItem.Applid = self.Applid
        self.NQItem.Date = self.Date
        self.NQItem.ResTime = self.ResTime
        self.NQItem.ColTime = self.ColTime

        self.SMItem.Applid = self.Applid
        self.SMItem.Date = self.Date
        self.SMItem.ResTime = self.ResTime
        self.SMItem.ColTime = self.ColTime   

        self.LGItem.Applid = self.Applid
        self.LGItem.Date = self.Date
        self.LGItem.ResTime = self.ResTime
        self.LGItem.ColTime = self.ColTime   

        self.DCItem.Applid = self.Applid
        self.DCItem.Date = self.Date
        self.DCItem.ResTime = self.ResTime
        self.DCItem.ColTime = self.ColTime  

        self.DUItem.Applid = self.Applid
        self.DUItem.Date = self.Date
        self.DUItem.ResTime = self.ResTime
        self.DUItem.ColTime = self.ColTime          

        self.TSQItem.Applid = self.Applid
        self.TSQItem.Date = self.Date
        self.TSQItem.ResTime = self.ResTime
        self.TSQItem.ColTime = self.ColTime  

        self.TDQItem.Applid = self.Applid
        self.TDQItem.Date = self.Date
        self.TDQItem.ResTime = self.ResTime
        self.TDQItem.ColTime = self.ColTime  

        self.CONItem.Applid = self.Applid
        self.CONItem.Date = self.Date
        self.CONItem.ResTime = self.ResTime
        self.CONItem.ColTime = self.ColTime         


    def IntervalFullJSON(self):
        JSONInfo = "{\"APPID\":\"" + self.Applid + "\"" + \
        ",\"ColTime\":\"" + self.ColTime + "\"" + \
        ",\"TotlTran\":\"" + str(self.TMItem.TotTran) + "\"" + \
        ",\"TotlActUsr\":\"" + str(self.TMItem.TotlActUsr) + "\"" + \
        ",\"MXT\":\"" + str(self.TMItem.MXT) + "\"" + \
        ",\"PkActv\":\"" + str(self.TMItem.PkActv) + "\"" + \
        ",\"PeakMXTRatio\":\"" + str(self.TMItem.MXTRatio) + "\"" + \
        ",\"TClassMaxActTims\":\"" + str(self.TMItem.PickedTClassItem().MaxActTimes) + "\"" + \
        ",\"PrgThrsTimes\":\"" + str(self.TMItem.PickedTClassItem().PrgThrsTimes) + "\"" + \
        ",\"QRDispRatio\":\"" + str(self.DSItem.QRDispRatio) + "\"" + \
        ",\"SRBCPUUtilRatio\":\"" + str(self.DSItem.SRBCPUUtilRatio) + "\"" + \
        ",\"QRCPUUtilRatio\":\"" + str(self.DSItem.QRCPUUtilRatio) + "\"" + \
        ",\"QRDispUtilRatio\":\"" + str(self.DSItem.QRDispUtilRatio) + "\"" + \
        ",\"ASCPUUtilRatio\":\"" + str('%.2f'%(float(self.DSItem.ASCPUUtilRatio))) + "\"" + \
        ",\"L8DispRatio\":\"" + str(self.DSItem.L8DispRatio) + "\"" + \
        ",\"L8CPUUtilRatio\":\"" + str(self.DSItem.L8CPUUtilRatio) + "\"" + \
        ",\"L8DispUtilRatio\":\"" + str(self.DSItem.L8DispUtilRatio) + "\"" + \
        ",\"ENQIssued\":\"" + str(self.NQItem.ENQIssd) + "\"" + \
        ",\"ENQWtd\":\"" + str(self.NQItem.ENQWtd) + "\"" + \
        ",\"ENQRtnd\":\"" + str(self.NQItem.ENQRtnd) + "\"" + \
        ",\"ImmRejEqBsy\":\"" + str(self.NQItem.ImmRejEqBsy) + "\"" + \
        ",\"ImmRejEqRtn\":\"" + str(self.NQItem.ImmRejEqRtn) + "\"" + \
        ",\"ENQImmSuccessRatio\":\"" + str(self.NQItem.ENQImmSuccessRatio) + "\"" + \
        ",\"DSAPercent\":\"" + str(self.SMItem.DSARatio) + "\"" + \
        ",\"EDSAPercent\":\"" + str(self.SMItem.EDSARatio) + "\"" + \
        ",\"DSAHWMRatio\":\"" + str(self.SMItem.DSAHWMRatio) + "\"" + \
        ",\"EDSAHWMRatio\":\"" + str(self.SMItem.EDSAHWMRatio) + "\"" + \
        ",\"ECDSARatio\":\"" + str(self.SMItem.ECDSARatio) + "\"" + \
        ",\"EUDSARatio\":\"" + str(self.SMItem.EUDSARatio) + "\"" + \
        ",\"ESDSARatio\":\"" + str(self.SMItem.ESDSARatio) + "\"" + \
        ",\"ERDSARatio\":\"" + str(self.SMItem.ERDSARatio) + "\"" + \
        ",\"ETDSARatio\":\"" + str(self.SMItem.ETDSARatio) + "\"" + \
        ",\"CDSARatio\":\"" + str(self.SMItem.CDSARatio) + "\"" + \
        ",\"UDSARatio\":\"" + str(self.SMItem.UDSARatio) + "\"" + \
        ",\"SDSARatio\":\"" + str(self.SMItem.SDSARatio) + "\"" + \
        ",\"RDSARatio\":\"" + str(self.SMItem.RDSARatio) + "\"" + \
        ",\"CDSA\":\"" + str(self.SMItem.CDSASize) + "\"" + \
        ",\"UDSA\":\"" + str(self.SMItem.UDSASize) + "\"" + \
        ",\"SDSA\":\"" + str(self.SMItem.SDSASize) + "\"" + \
        ",\"RDSA\":\"" + str(self.SMItem.RDSASize) + "\"" + \
        ",\"ECDSA\":\"" + str(self.SMItem.ECDSASize) + "\"" + \
        ",\"EUDSA\":\"" + str(self.SMItem.EUDSASize) + "\"" + \
        ",\"ESDSA\":\"" + str(self.SMItem.ESDSASize) + "\"" + \
        ",\"ERDSA\":\"" + str(self.SMItem.ERDSASize) + "\"" + \
        ",\"ETDSA\":\"" + str(self.SMItem.ETDSASize) + "\"" + \
        ",\"AKPTaken\":\"" + str(self.LGItem.AKPTaken) + "\"" + \
        ",\"BytesWritten\":\"" + str(self.LGItem.ByteWritten) + "\"" + \
        ",\"WaitFullBuf\":\"" + str(self.LGItem.WaitFullBuf) + "\"" + \
        ",\"RetryError\":\"" + str(self.LGItem.RetryError) + "\"" + \
        ",\"ShuntWriteRequest\":\"" + str(self.LGItem.ShuntWriteRequest) + "\"" + \
        ",\"PkConnWithTCB\":\"" + str(self.DCItem.PkConnWithTCB) + "\"" + \
        ",\"PkDB2TCBRatio\":\"" + str(self.DCItem.DB2TCBRatio) + "\"" + \
        ",\"TCBLimit\":\"" + str(self.DCItem.TCBLimit) + "\"" + \
        ",\"CommitRatio\":\"" + str(self.DCItem.PeakDEItem().CommitRatio) + "\"" + \
        ",\"ThreadRatio\":\"" + str(self.DCItem.PeakDEItem().ThreadRatio) + "\"" + \
        ",\"PthreadRatio\":\"" + str(self.DCItem.PeakDEItem().PthreadRatio) + "\"" + \
        ",\"PutqMainReqs\":\"" + str(self.TSQItem.PutqMainReqs) + "\"" + \
        ",\"GetqMainReqs\":\"" + str(self.TSQItem.GetqMainReqs) + "\"" + \
        ",\"TSMAINLIMITRatio\":\"" + str(self.TSQItem.TSMAINLIMITRatio) + "\"" + \
        ",\"PutqAuxReqs\":\"" + str(self.TSQItem.PutqAuxReqs) + "\"" + \
        ",\"GetqAuxReqs\":\"" + str(self.TSQItem.GetqAuxReqs) + "\"" + \
        ",\"WriteMTCI\":\"" + str(self.TSQItem.WriteMTCI) + "\"" + \
        ",\"AuxStgExh\":\"" + str(self.TSQItem.AuxStgExh) + "\"" + \
        ",\"TSCompress\":\"" + str(self.TSQItem.TSCompress) + "\"" + \
        ",\"BufferWaitTSQ\":\"" + str(self.TSQItem.BufferWait) + "\"" + \
        ",\"StringWaitTSQ\":\"" + str(self.TSQItem.StringWait) + "\"" + \
        ",\"IOErrorTSQDS\":\"" + str(self.TSQItem.IOErrorTSQDS) + "\"" + \
        ",\"PutqShareReqs\":\"" + str(self.TSQItem.PutqShareReqs) + "\"" + \
        ",\"GetqShareReqs\":\"" + str(self.TSQItem.GetqShareReqs) + "\"" + \
        ",\"PutqIntraReqs\":\"" + str(self.TDQItem.PutqIntraReqs) + "\"" + \
        ",\"GetqIntraReqs\":\"" + str(self.TDQItem.GetqIntraReqs) + "\"" + \
        ",\"BufferWaitTDQ\":\"" + str(self.TDQItem.BufferWait) + "\"" + \
        ",\"StringWaitTDQ\":\"" + str(self.TDQItem.StringWait) + "\"" + \
        ",\"IOErrorsTDQ\":\"" + str(self.TDQItem.IOErrors) + "\"" + \
        ",\"NOSPACETms\":\"" + str(self.TDQItem.NOSPACETms) + "\"" + \
        ",\"QueuedAlloc\":\"" + str(self.CONItem.QueuedAlloc) + "\"" + \
        ",\"FailedLinkAlloc\":\"" + str(self.CONItem.FailedLinkAlloc) + "\"" + \
        ",\"FailedAllocSessUsed\":\"" + str(self.CONItem.FailedAllocSessUsed) + "\"" + \
        ",\"SysDumpTaken\":\"" + str(self.DUItem.SysDumpTaken) + "\"" + \
        ",\"SysDumpSupp\":\"" + str(self.DUItem.SysDumpSupp) + "\"" + \
        ",\"TranDumpTaken\":\"" + str(self.DUItem.TranDumpTaken) + "\"" + \
        ",\"TranDumpSupp\":\"" + str(self.DUItem.TranDumpSupp) + "\"" + \
        "}"
        return JSONInfo

    def IntervalBriefJSON(self):
        JSONInfo = "{\"APPID\":\"" + self.Applid + "\"" + \
        ",\"ColTime\":\"" + self.ColTime + "\"" + \
        ",\"TotlTran\":\"" + str(self.TMItem.TotTran) + "\"" + \
        ",\"MXTTimeRch\":\"" + str(self.TMItem.MXTTimeRch) + "\"" + \
        ",\"PeakMXTRatio\":\"" + str(self.TMItem.MXTRatio) + "\"" + \
        ",\"TClassMaxActTims\":\"" + str(self.TMItem.PickedTClassItem().MaxActTimes) + "\"" + \
        ",\"QRDispRatio\":\"" + str(self.DSItem.QRDispRatio) + "\"" + \
        ",\"ASCPUUtilRatio\":\"" + str('%.2f'%(float(self.DSItem.ASCPUUtilRatio) + float(self.DSItem.SRBCPUUtilRatio))) + "\"" + \
        ",\"ENQImmSuccessRatio\":\"" + str(self.NQItem.ENQImmSuccessRatio) + "\"" + \
        ",\"SMSOSTms\":\"" + str(self.SMItem.DSASOSTms+self.SMItem.EDSASOSTms+self.SMItem.GDSASOSTms) + "\"" + \
        ",\"SMSTGVIOLTms\":\"" + str(self.SMItem.DSASTGVIOLTms+self.SMItem.EDSASTGVIOLTms+self.SMItem.GDSASTGVIOLTms) + "\"" + \
        ",\"DSAPercent\":\"" + str(self.SMItem.DSARatio) + "\"" + \
        ",\"DSAHWMRatio\":\"" + str(self.SMItem.DSAHWMRatio) + "\"" + \
        ",\"EDSAPercent\":\"" + str(self.SMItem.EDSARatio) + "\"" + \
        ",\"EDSAHWMRatio\":\"" + str(self.SMItem.EDSAHWMRatio) + "\"" + \
        ",\"WriteReq\":\"" + str(self.LGItem.WriteReq) + "\"" + \
        ",\"ShuntWriteRequest\":\"" + str(self.LGItem.ShuntWriteRequest) + "\"" + \
        ",\"PkDB2TCBRatio\":\"" + str(self.DCItem.DB2TCBRatio) + "\"" + \
        ",\"ThreadRatio\":\"" + str(self.DCItem.PeakDEItem().ThreadRatio) + "\"" + \
        ",\"ThreadWtOrOF\":\"" + str(self.DCItem.PeakDEItem().ThreadWtOrOF) + "\"" + \
        ",\"BufferOrStringWaitTSQ\":\"" + str(self.TSQItem.BufferWait+self.TSQItem.StringWait) + "\"" + \
        ",\"BufferOrStringWaitTDQ\":\"" + str(self.TDQItem.BufferWait + self.TDQItem.StringWait) + "\"" + \
        ",\"QueuedAlloc\":\"" + str(self.CONItem.QueuedAlloc) + "\"" + \
        ",\"SysOrTranDumpTaken\":\"" + str(self.DUItem.SysDumpTaken+self.DUItem.TranDumpTaken) + "\"" + \
        "}"
        return JSONInfo

class IntervalItemListByInterval:   
    ResTimeMin = ''     #Last rest time in minutes, remove second because second may be different for applids
    ColTimeMin = ''     #Data collection time in minutes, remove second because second may be different for applids
    ItemList = list()
    TotTranNum = 0

    ScoreTransaction = 100
    RiskTransaction = ''
    
    ScoreDispatch = 100
    RiskDispatch = ''

    ScoreStorage = 100
    RiskStorage = ''
    
    ScoreData = 100
    RiskData = ''
    
    ScoreQueue = 100
    RiskQueue = ''

    ScoreOther = 100
    RiskOther = ''

    global DSARATIOTHRES
    global EDSARATIOTHRES
    global QRDISPRATIOTHRES
    global ENQIMMESUCCESSTHRESHOLD
    global TMFlag
    global TCFlag
    global DSFlag
    global LGFlag
    global DBFlag
    global NQFlag
    global TSQFlag
    global TDQFlag
    global SMFlag
    global CONFlag
    global DUFlag    

    def __init__(self):
        self.ResTimeMin = ''
        self.ColTimeMin = ''
        self.ItemList = list()
        self.TotTranNum = 0


    def AppendItem(self,IntervalItem):
        if self.ResTimeMin == '' and self.ColTimeMin == '':    #this is the first item, set ResTime and ColTime
            self.ResTimeMin = IntervalItem.ResTime.split(":")[0]+":"+IntervalItem.ResTime.split(":")[1]
            self.ColTimeMin = IntervalItem.ColTime.split(":")[0]+":"+IntervalItem.ColTime.split(":")[1]
            self.ItemList.append(IntervalItem)
        elif self.ResTimeMin in IntervalItem.ResTime and self.ColTimeMin in IntervalItem.ColTime: #make a quick check
            self.ItemList.append(IntervalItem)
        else:
            print (self.ResTimeMin + IntervalItem.ResTime)
            print (self.ColTimeMin + IntervalItem.ColTime)
            print ("Invalid Interval Item is added in the subgroup by interval infromation")         

    def InsightsTM(self):                                              #Insights into Transaction Manager Statistics
        if TMFlag == 0:
            return "No Transaction Manager Statistics was found"

        ApplidList = list()
        TotTranList =  list()
        PeakActList = list()
        MXTList = list()
        MXTTimeRchList = list()
        MXTRatioList = list()
        TotlDlyList = list()

        for item in self.ItemList:
            ApplidList.append(item.Applid)
            TotTranList.append(int(item.TMItem.TotTran))
            PeakActList.append(int(item.TMItem.PkActv))
            MXTList.append(int(item.TMItem.MXT))
            MXTRatioList.append(float(item.TMItem.MXTRatio))
            TotlDlyList.append(int(item.TMItem.TotlDly))
            MXTTimeRchList.append(int(item.TMItem.MXTTimeRch))

        InsightsTMTitle = "------Transaction Manager Insights------\n"       

        SumTotTran = sum(TotTranList)
        IntervalValue = self.ItemList[0].TMItem.IntervalValue
        SumTPS = '%.2f'% float(SumTotTran/IntervalValue)
        InsightsTMWorkload = "Sum of Total Transaction (System+User) = " + str(SumTotTran) + "\n"
        InsightsTMTPS = "Sum of Transaction Per Second (TPS) = " + str(SumTPS) + "\n"

        MaxTotTran = max(TotTranList)
        MaxTPS = '%.2f'% float(MaxTotTran/IntervalValue)
        MaxIndex = TotTranList.index(MaxTotTran)

        MinTotTran = min(TotTranList)
        MinTPS = '%.2f'% float(MinTotTran/IntervalValue)
        MinIndex = TotTranList.index(MinTotTran)

        InsightsTotTran = "Maximum Total Transaction = " + str(MaxTotTran) + " (" + ApplidList[MaxIndex] + ")\n"
        InsightsTotTran = InsightsTotTran + "Maximum TPS = " + str(MaxTPS) + " (" + ApplidList[MaxIndex] + ")\n"

        InsightsTotTran = InsightsTotTran + "Minmum Total Transaction = " + str(MinTotTran) + " (" + ApplidList[MinIndex] + ")\n"
        InsightsTotTran = InsightsTotTran + "Maximum TPS = " + str(MinTPS) + " (" + ApplidList[MinIndex] + ")\n"

        AvgTotTran = SumTotTran/len(TotTranList)
        dev = [x-AvgTotTran for x in TotTranList]
        dev2 = [x*x for x in dev]
        StandDev = math.sqrt(sum(dev2)/len(TotTranList))
        CoeffVar = '%.2f'% float(StandDev/AvgTotTran)
        InsightsTotTran = InsightsTotTran + "Coefficient of Variance for Total Transaction = " + str(CoeffVar) + "\n"


        MaxPeakAct = max(PeakActList)
        NaxIndex = PeakActList.index(MaxPeakAct)
        InsightsPeakAct = "Maximum Peak Active Transaction = " + str(MaxPeakAct) + " (" + ApplidList[MaxIndex] + ", MXT=" + str(MXTList[MaxIndex]) + ")\n"
 
        MaxMXTRatio = max(MXTRatioList)
        NaxIndex = MXTRatioList.index(MaxMXTRatio)       
        InsightsPkMXT = "Maximum System Utilization (Peak Act/MXT) ratio = " + str(MaxMXTRatio) + "% (" + ApplidList[MaxIndex] + ", MXT=" + str(MXTList[MaxIndex]) + ")\n"

        if max(MXTTimeRchList) == 0:
            InsightsMXTTimes = "\nNo region reached MAXTASK(MXT) during the interval\n"
        else:
            self.ScoreTransaction = self.ScoreTransaction - 10      #deduct transaction score
            self.RiskTransaction = self.RiskTransaction + "MAXTASK has been reached. Please see Transaction Manager report.\n"
            InsightsMXTTimes = "The following region(s) reached MAXTASK(MXT) during the interval\n"
            for i in range(len(MXTTimeRchList)):
                if MXTTimeRchList[i] > 0:
                    InsightsMXTTimes =  InsightsMXTTimes + "- " + ApplidList[i] + " reached MXT " + str(MXTTimeRchList[i]) + \
                                        " time(s), queued transaction number is " + str(TotlDlyList[i]) + "\n"


        InsightsTMString = InsightsTMTitle + InsightsTMWorkload + InsightsTMTPS + InsightsTotTran + InsightsPeakAct + InsightsPkMXT + InsightsMXTTimes

        return InsightsTMString

    def InsightsTC(self):                                              #Insights into Transaction Class Statistics
        if TCFlag == 0:
            return "No Transaction Class Statistics was found"

        ApplidList = list()
        TClassList = list()

        for item in self.ItemList:
            ApplidList.append(item.Applid)
            TClassList.append(item.TMItem.TClassItemList)

        InsightsTCTitle = "------Transaction Class Insights------\n"
        InsightsTCMaxAct = ''

        for i in range(len(TClassList)):
            for ii in range(len(TClassList[i])):
                if TClassList[i][ii].TClassName == "*TOTALS*" and TClassList[i][ii].MaxActTimes > 0:
                    InsightsTCMaxAct = InsightsTCMaxAct + ApplidList[i] + " Max Active times(" + str(TClassList[i][ii].MaxActTimes) + \
                                       ") Purge Threshold Reached times(" + str(TClassList[i][ii].PrgThrsTimes) + ")\n"
                    for iii in range(ii):
                        if TClassList[i][iii].MaxActTimes > 0:
                            InsightsTCMaxAct = InsightsTCMaxAct + "  - " + TClassList[i][iii].TClassName + " MaxAct(" + str(TClassList[i][iii].MaxAct) + ")" + \
                                               " PurgeThresh(" + str(TClassList[i][iii].PrgThrs) + ")" + \
                                               " PeakAct(" + str(TClassList[i][iii].PckAct) + ")" + \
                                               " PckQued(" + str(TClassList[i][iii].PckQued) + ")" + \
                                               " TimesMaxAct(" + str(TClassList[i][iii].MaxActTimes) + ")" + \
                                               " TimesPrgThr("  + str(TClassList[i][iii].PrgThrsTimes) + ")\n"
                    InsightsTCMaxAct = InsightsTCMaxAct + "\n"


        if InsightsTCMaxAct == '':
            InsightsTCMaxAct = "No Transaction Class reached Max Active\n"
        else:
            self.ScoreTransaction = self.ScoreTransaction - 10      #deduct transaction score
            self.RiskTransaction = self.RiskTransaction + "TClass MaxActive has been reached. Please see Transaction Class report.\n"            

        InsightsTMString = InsightsTCTitle + InsightsTCMaxAct

        return InsightsTMString

    def InsightsDS(self):                                              #Insights into Dispatcher Statistics 
        if DSFlag == 0:
            return "No Dispatcher Statistics was found"

        ApplidList = list()
        QRDispRatioList = list()
        QRDispUtilRatioList = list()
        ASCPUUtilRatioList = list()

        for item in self.ItemList:
            ApplidList.append(item.Applid)
            QRDispRatioList.append(float(item.DSItem.QRDispRatio))
            QRDispUtilRatioList.append(float(item.DSItem.QRDispUtilRatio))
            ASCPUUtilRatioList.append(float(item.DSItem.ASCPUUtilRatio))

        InsightsDSTitle = "------Dispatcher Insights------\n"       

        MaxASCPUUtilRatio = max(ASCPUUtilRatioList)
        MaxIndex = ASCPUUtilRatioList.index(MaxASCPUUtilRatio)
        MinASCPUUtilRatio = min(ASCPUUtilRatioList)
        MinIndex = ASCPUUtilRatioList.index(MinASCPUUtilRatio)

        InsightsASCPUUtilRatio = "Maximum Address Space CPU/Interval Ratio = " + str(MaxASCPUUtilRatio) + "% (" + ApplidList[MaxIndex] + ")\n" + \
                              "Minmum Address Space CPU/Interval Ratio = "  + str(MinASCPUUtilRatio) + "% (" + ApplidList[MinIndex] + ")\n"    

        MaxQRDispUtilRatio = max(QRDispUtilRatioList)
        MaxIndex = QRDispUtilRatioList.index(MaxQRDispUtilRatio)
        MinQRDispUtilRatio = min(QRDispUtilRatioList)
        MinIndex = QRDispUtilRatioList.index(MinQRDispUtilRatio)

        InsightsQRDispUtilRatio = "Maximum QR Dispatch/Interval Ratio = " + str(MaxQRDispUtilRatio) + "% (" + ApplidList[MaxIndex] + ")\n" + \
                              "Minmum QR Dispatch/Interval Ratio = "  + str(MinQRDispUtilRatio) + "% (" + ApplidList[MinIndex] + ")\n"

        MaxQRDispRatio = max(QRDispRatioList)
        MaxIndex = QRDispRatioList.index(MaxQRDispRatio)
        MinQRDispRatio = min(QRDispRatioList)
        MinIndex = QRDispRatioList.index(MinQRDispRatio)

        InsightsQRDispRatio = "Maximum QR CPU/Dispatch Ratio = " + str(MaxQRDispRatio) + "% (" + ApplidList[MaxIndex] + ")\n" + \
                              "Minmum QR CPU/Dispatch Ratio = "  + str(MinQRDispRatio) + "% (" + ApplidList[MinIndex] + ")\n"

        InsightsQRDispRatioThresApplid = ''
        InsightsQRDispRatioThresApplidNum = 0

        for i in range(len(ApplidList)):
            if float(QRDispRatioList[i]) < QRDISPRATIOTHRES:
                InsightsQRDispRatioThresApplidNum = InsightsQRDispRatioThresApplidNum + 1
                InsightsQRDispRatioThresApplid = InsightsQRDispRatioThresApplid + ' ' + ApplidList[i]

        if InsightsQRDispRatioThresApplidNum > 0:
            self.ScoreDispatch = self.ScoreDispatch - 10      #deduct dispatch score
            self.RiskDispatch = self.RiskDispatch + "Low QR CPU/Dispatch ratio has been found. Please see Dispather report.\n"
            InsightsQRDispRatio = InsightsQRDispRatio + "\n" + \
                                  str(InsightsQRDispRatioThresApplidNum) + " region(s) were below QR CPU/Dispatch ratio threshold (" + \
                                  str(QRDISPRATIOTHRES) + "%):\n" + \
                                  InsightsQRDispRatioThresApplid + "\n"
        else:
            InsightsQRDispRatio = InsightsQRDispRatio + "\nAll regions reached QR CPU/Dispatch ratio threshold\n"

        InsightsDSString = InsightsDSTitle + InsightsASCPUUtilRatio + InsightsQRDispUtilRatio + InsightsQRDispRatio

        return InsightsDSString

    def InsightsSM(self):                                              #Insights into Storage Manager Statistics                                       
        if SMFlag == 0:
            return "No Storage Statistics was found"

        ApplidList = list()
        
        DSARatioList = list()
        DSALIMList = list()
        PeakDSAList = list()
        DSAHWMList = list()
        CDSARatioList = list()
        UDSARatioList = list()
        SDSARatioList = list()
        RDSARatioList = list()
        DSASOSTmsList = list()
        DSASTGVIOLTmsList = list()

        EDSARatioList = list()
        EDSALIMList = list()
        PeakEDSAList = list()
        EDSAHWMList = list()
        ECDSARatioList = list()
        EUDSARatioList = list()
        ESDSARatioList = list()
        ERDSARatioList = list()
        ETDSARatioList = list()
        EDSASOSTmsList = list()
        EDSASTGVIOLTmsList = list()


        for item in self.ItemList:
            ApplidList.append(item.Applid)
            DSARatioList.append(float(item.SMItem.DSARatio))
            DSALIMList.append(int(item.SMItem.DSALIM))
            PeakDSAList.append(int(item.SMItem.PeakDSA))
            DSAHWMList.append(int(item.SMItem.CDSAHWM + item.SMItem.UDSAHWM + item.SMItem.SDSAHWM + item.SMItem.RDSAHWM))
            CDSARatioList.append(float(item.SMItem.CDSARatio))
            UDSARatioList.append(float(item.SMItem.UDSARatio))
            SDSARatioList.append(float(item.SMItem.SDSARatio))
            RDSARatioList.append(float(item.SMItem.RDSARatio))
            DSASOSTmsList.append(int(item.SMItem.DSASOSTms))
            DSASTGVIOLTmsList.append(int(item.SMItem.DSASTGVIOLTms))


            EDSARatioList.append(float(item.SMItem.EDSARatio))
            EDSALIMList.append(int(item.SMItem.EDSALIM))
            PeakEDSAList.append(int(item.SMItem.PeakEDSA))
            EDSAHWMList.append(int(item.SMItem.ECDSAHWM + item.SMItem.EUDSAHWM + item.SMItem.ESDSAHWM + item.SMItem.ERDSAHWM))
            ECDSARatioList.append(float(item.SMItem.ECDSARatio))
            EUDSARatioList.append(float(item.SMItem.EUDSARatio))
            ESDSARatioList.append(float(item.SMItem.ESDSARatio))
            ERDSARatioList.append(float(item.SMItem.ERDSARatio))
            ETDSARatioList.append(float(item.SMItem.ETDSARatio))
            EDSASOSTmsList.append(int(item.SMItem.EDSASOSTms))
            EDSASTGVIOLTmsList.append(int(item.SMItem.EDSASTGVIOLTms))

        InsightsSMDSATitle = "------Storage Manager Insights (DSA part, below 16M)------\n"

        InsightsDSARatio = ''
        InsightsDSARatioThresApplid = ''
        InsightsDSARatioThresApplidNum = 0

        for i in range(len(ApplidList)):
            if float(DSARatioList[i]) >= DSARATIOTHRES:
                InsightsDSARatioThresApplidNum = InsightsDSARatioThresApplidNum + 1
                InsightsDSARatioThresApplid = InsightsDSARatioThresApplid + \
                                              "\n- " + ApplidList[i] + "\n" + \
                                              "-- DSA utilization ratio (Peak DSA size/DSALIM) = " + str(DSARatioList[i]) + "%\n" + \
                                              "-- DSLIMIT = " + ConvertGMK(str(DSALIMList[i])) + "\n" + \
                                              "-- Peak DSA size = " + ConvertGMK(str(PeakDSAList[i])) + "\n" + \
                                              "--- CDSA(" + str(CDSARatioList[i]) + "%) UDSA(" + str(UDSARatioList[i]) + \
                                                       "%) SDSA(" + str(SDSARatioList[i]) + "%) RDSA(" + str(RDSARatioList[i]) + "%)\n" \
                                              "-- High Water Mark of actual DSA usage = " + ConvertGMK(str(DSAHWMList[i])) + "\n"

        if InsightsDSARatioThresApplidNum > 0:
            self.ScoreStorage = self.ScoreStorage - 10      #deduct storage score
            self.RiskStorage = self.RiskStorage + "DSA usage was high. Please see Storage Manager report.\n"            
            InsightsDSARatio = str(InsightsDSARatioThresApplidNum) + " region(s) reached the DSA ratio threshold (" + str(DSARATIOTHRES) + "%):\n" + \
                               InsightsDSARatioThresApplid
        else:   #Pick the region with the maximum DSA ratio
            MaxDSARatio = max(DSARatioList)
            MaxIndex = DSARatioList.index(MaxDSARatio)
            InsightsDSARatioThresApplid = "- DSA utilization ratio (Peak DSA size/DSALIM) = " + str(DSARatioList[MaxIndex]) + "%\n" + \
                                          "- DSLIMIT = " + ConvertGMK(str(DSALIMList[MaxIndex])) + "\n" + \
                                          "- Peak DSA size = " + ConvertGMK(str(PeakDSAList[MaxIndex])) + "\n" + \
                                          "-- CDSA(" + str(CDSARatioList[MaxIndex]) + "%) UDSA(" + str(UDSARatioList[MaxIndex]) + \
                                                 "%) SDSA(" + str(SDSARatioList[MaxIndex]) + "%) RDSA(" + str(RDSARatioList[MaxIndex]) + "%)\n" \
                                          "- High Water Mark of actual DSA usage = " + ConvertGMK(str(DSAHWMList[MaxIndex])) + "\n"   
            InsightsDSARatio = "No region reached the DSA ratio threshold (" + str(DSARATIOTHRES) + "%)\n" + \
                               "The region with the maximum DSA utilization is " + ApplidList[MaxIndex] + ":\n" + \
                               InsightsDSARatioThresApplid      


        InsightsDSASOS = ''
        if max(DSASOSTmsList) == 0:
            InsightsDSASOS = "\nNo region ever met Short On Storage in DSA\n"
        else:
            self.ScoreStorage = self.ScoreStorage - 10      #deduct storage score
            self.RiskStorage = self.RiskStorage + "Short On Storage (DSA) happened. Please see Storage Manager report.\n"            
            InsightsDSASOS = "\nShort On Storage (DSA) happened in:\n"
            for i in range(len(ApplidList)):
                if DSASOSTmsList[i] > 0:
                    InsightsDSASOS = InsightsDSASOS + "  - " + ApplidList[i] + " " + str(DSASOSTmsList[i]) + " time(s)\n"

        InsightsDSASTGGVIOL = ''
        if max(DSASTGVIOLTmsList) == 0:
            InsightsDSASTGGVIOL = "\nNo region ever met Storage Violation in DSA\n"
        else:
            self.ScoreStorage = self.ScoreStorage - 10      #deduct storage score
            self.RiskStorage = self.RiskStorage + "Storage Violation (DSA) happened. Please see Storage Manager report.\n" 
            InsightsDSASTGGVIOL = "\nStorage Violation (DSA) happened in:\n"
            for i in range(len(ApplidList)):
                if DSASTGVIOLTmsList[i] > 0:
                    InsightsDSASTGGVIOL = InsightsDSASTGGVIOL + "  - " + ApplidList[i] + " " + str(DSASTGVIOLTmsList[i]) + " time(s)\n"
        
        InsightsSMString = InsightsSMDSATitle + InsightsDSARatio + InsightsDSASOS + InsightsDSASTGGVIOL


        InsightsSMEDSATitle = "------Storage Manager Insights (EDSA part, above 16M and below 2G)------\n"

        InsightsEDSARatio = ''
        InsightsEDSARatioThresApplid = ''
        InsightsEDSARatioThresApplidNum = 0

        for i in range(len(ApplidList)):
            if float(EDSARatioList[i]) >= EDSARATIOTHRES:
                InsightsEDSARatioThresApplidNum = InsightsEDSARatioThresApplidNum + 1
                InsightsEDSARatioThresApplid = InsightsEDSARatioThresApplid + \
                                              "\n- " + ApplidList[i] + "\n" + \
                                              "-- EDSA utilization ratio (Peak EDSA size/EDSALIM) = " + str(EDSARatioList[i]) + "%\n" + \
                                              "-- DSLIMIT = " + ConvertGMK(str(EDSALIMList[i])) + "\n" + \
                                              "-- Peak EDSA size = " + ConvertGMK(str(PeakEDSAList[i])) + "\n" + \
                                              "--- ECDSA(" + str(ECDSARatioList[i]) + "%) EUDSA(" + str(EUDSARatioList[i]) + \
                                                       "%) ESDSA(" + str(ESDSARatioList[i]) + "%) ERDSA(" + str(ERDSARatioList[i]) + "%)\n" \
                                              "-- High Water Mark of actual EDSA usage = " + ConvertGMK(str(EDSAHWMList[i])) + "\n"

        if InsightsEDSARatioThresApplidNum > 0:
            self.ScoreStorage = self.ScoreStorage - 10      #deduct storage score
            self.RiskStorage = self.RiskStorage + "EDSA usage was high. Please see Storage Manager report.\n"  
            InsightsEDSARatio = str(InsightsEDSARatioThresApplidNum) + " region(s) reached the EDSA ratio threshold (" + str(EDSARATIOTHRES) + "%):\n" + \
                               InsightsEDSARatioThresApplid
        else:   #Pick the region with the maximum EDSA ratio
            MaxEDSARatio = max(EDSARatioList)
            MaxIndex = EDSARatioList.index(MaxEDSARatio)
            InsightsEDSARatioThresApplid = "- EDSA utilization ratio (Peak EDSA size/EDSALIM) = " + str(EDSARatioList[MaxIndex]) + "%\n" + \
                                          "- DSLIMIT = " + ConvertGMK(str(EDSALIMList[MaxIndex])) + "\n" + \
                                          "- Peak EDSA size = " + ConvertGMK(str(PeakEDSAList[MaxIndex])) + "\n" + \
                                          "-- ECDSA(" + str(ECDSARatioList[MaxIndex]) + "%) EUDSA(" + str(EUDSARatioList[MaxIndex]) + \
                                                 "%) ESDSA(" + str(ESDSARatioList[MaxIndex]) + "%) ERDSA(" + str(ERDSARatioList[MaxIndex]) + "%)\n" \
                                          "- High Water Mark of actual EDSA usage = " + ConvertGMK(str(EDSAHWMList[MaxIndex])) + "\n"   
            InsightsEDSARatio = "No region reached the EDSA ratio threshold (" + str(EDSARATIOTHRES) + "%)\n" + \
                               "The region with the maximum EDSA utilization is " + ApplidList[MaxIndex] + ":\n" + \
                               InsightsEDSARatioThresApplid 

        InsightsEDSASOS = ''
        if max(EDSASOSTmsList) == 0:
            InsightsEDSASOS = "\nNo region ever met Short On Storage in EDSA\n"
        else:
            self.ScoreStorage = self.ScoreStorage - 10      #deduct storage score
            self.RiskStorage = self.RiskStorage + "Short On Storage (EDSA) happened. Please see Storage Manager report.\n" 
            InsightsEDSASOS = "\nShort On Storage (EDSA) happened in:\n"
            for i in range(len(ApplidList)):
                if EDSASOSTmsList[i] > 0:
                    InsightsEDSASOS = InsightsEDSASOS + "  - " + ApplidList[i] + " " + str(EDSASOSTmsList[i]) + " time(s)\n"

        InsightsEDSASTGGVIOL = ''
        if max(EDSASTGVIOLTmsList) == 0:
            InsightsEDSASTGGVIOL = "\nNo region ever met Storage Violation in EDSA\n"
        else:
            self.ScoreStorage = self.ScoreStorage - 10      #deduct storage score
            self.RiskStorage = self.RiskStorage + "Storage Violation (EDSA) happened. Please see Storage Manager report.\n" 
            InsightsEDSASTGGVIOL = "\nStorage Violation (EDSA) happened in:\n"
            for i in range(len(ApplidList)):
                if EDSASTGVIOLTmsList[i] > 0:
                    InsightsEDSASTGGVIOL = InsightsEDSASTGGVIOL + "  - " + ApplidList[i] + " " + str(EDSASTGVIOLTmsList[i]) + " time(s)\n"   

        InsightsSMString = InsightsSMString + "\n" + InsightsSMEDSATitle + InsightsEDSARatio + InsightsEDSASOS + InsightsEDSASTGGVIOL

        return InsightsSMString

    def InsightsLG(self):                                              #Insights into CICS log statistics
        if LGFlag == 0:
            return "No Log Statistics was found"     

        ApplidList = list()
        ByteWrittenList = list()
        WaitFullBufList = list()
        RetryErrorList = list()
        ShuntWriteRequestList = list()

        for item in self.ItemList:
            ApplidList.append(item.Applid)
            ByteWrittenList.append(int(item.LGItem.ByteWritten))
            WaitFullBufList.append(int(item.LGItem.WaitFullBuf))
            RetryErrorList.append(int(item.LGItem.RetryError))
            ShuntWriteRequestList.append(int(item.LGItem.ShuntWriteRequest))

        InsightsLGTitle = "------Log Manager Insights------\n"       

        MaxByteWritten = max(ByteWrittenList)
        MaxIndex = ByteWrittenList.index(MaxByteWritten)
        MinByteWritten = min(ByteWrittenList)
        MinIndex = ByteWrittenList.index(MinByteWritten)

        InsightsByteWritten = "Maximum bytes written in DFHLOG = " + str(MaxByteWritten) + " (" + ApplidList[MaxIndex] + ")\n" + \
                              "Minmum bytes written in DFHLOG = "  + str(MinByteWritten) + " (" + ApplidList[MinIndex] + ")\n"    

        InsightsWaitFullBufApplid = ''
        InsightsWaitFullBufApplidNum = 0

        InsightsRetryErrorApplid = ''
        InsightsRetryErrorApplidNum = 0

        for i in range(len(ApplidList)):
            if float(WaitFullBufList[i]) > 0:
                InsightsWaitFullBufApplidNum = InsightsWaitFullBufApplidNum + 1
                InsightsWaitFullBufApplid = InsightsWaitFullBufApplid + ' ' + ApplidList[i]        

            if float(RetryErrorList[i]) > 0:
                InsightsRetryErrorApplidNum = InsightsRetryErrorApplidNum + 1
                InsightsRetryErrorApplid = InsightsRetryErrorApplid + ' ' + ApplidList[i]

        if InsightsWaitFullBufApplidNum > 0:
            self.ScoreData = self.ScoreData - 10      #deduct Data score
            self.RiskData = self.RiskData + "Log buffer was ever full. Please see Log Manager report.\n" 
            InsightsWaitFullBuf = str(InsightsWaitFullBufApplidNum) + " region(s) ever waited due to log buffer full\n" + \
                                  InsightsWaitFullBufApplid + "\n"
        else:
            InsightsWaitFullBuf = "No region ever waited due to log buffer full\n"

        if InsightsRetryErrorApplidNum > 0:
            self.ScoreData = self.ScoreData - 10      #deduct Data score
            self.RiskData = self.RiskData + "Logger retryable errors happened. Please see Log Manager report.\n" 
            InsightsRetryError = str(InsightsRetryErrorApplidNum) + " region(s) ever met MVS system logger retryable errors\n" + \
                                  InsightsRetryErrorApplid + "\n"
        else:
            InsightsRetryError = "No region ever met MVS system logger retryable errors\n"

        MaxShuntWriteRequest = max(ShuntWriteRequestList)
        MaxIndex = ShuntWriteRequestList.index(MaxShuntWriteRequest)
        MinShuntWriteRequest = min(ShuntWriteRequestList)
        MinIndex = ShuntWriteRequestList.index(MinShuntWriteRequest)

        InsightsShuntWriteRequest = "Maximum shunting requests = " + str(MaxShuntWriteRequest) + " (" + ApplidList[MaxIndex] + ")\n" + \
                                     "Minmum shunting requests = "  + str(MinShuntWriteRequest) + " (" + ApplidList[MinIndex] + ")\n"  

        InsightsLGString =  InsightsByteWritten + InsightsWaitFullBuf + InsightsRetryError + InsightsShuntWriteRequest   
        return InsightsLGString   

    def InsightsDB(self):                                              #Insights into CICS DB2 statistics
        if DBFlag == 0:
            return "No DB2 Statistics was found"     

        ApplidList = list()
        PkConnWithTCBList = list()
        TCBLIMITRatioList = list()
        PkPoolTasksList = list()
        DBEntriesList = list()


        for item in self.ItemList:
            ApplidList.append(item.Applid)
            PkConnWithTCBList.append(int(item.DCItem.PkConnWithTCB))
            TCBLIMITRatioList.append(float(item.DCItem.DB2TCBRatio))
            PkPoolTasksList.append(int(item.DCItem.PkPoolTasks))
            DBEntriesList.append(item.DCItem.DB2ENTRYItemList)

        InsightsDBTitle = "------DB2 Insights------\n"

        MaxPkConnWithTCB = max(PkConnWithTCBList)
        MaxIndex = PkConnWithTCBList.index(MaxPkConnWithTCB)
        MinPkConnWithTCB = min(PkConnWithTCBList)
        MinIndex = PkConnWithTCBList.index(MinPkConnWithTCB)      

        InsightsPkConnWithTCB = "Maximum peak Connections with TCB = " + str(MaxPkConnWithTCB) + " (" + ApplidList[MaxIndex] + ")\n" + \
                              "Minmum peak Connections with TCB = "  + str(MinPkConnWithTCB) + " (" + ApplidList[MinIndex] + ")\n"   

        MaxTCBLIMITRatio = max(TCBLIMITRatioList)
        MaxIndex = TCBLIMITRatioList.index(MaxTCBLIMITRatio)
        InsightsTCBLIMITRatio = "Maximum TCBLIMIT utilization ratio = " + str(MaxTCBLIMITRatio) + "% (" + ApplidList[MaxIndex] + ")\n"

        if MaxTCBLIMITRatio == 100:
            self.ScoreData = self.ScoreData - 10      #deduct Data score
            self.RiskData = self.RiskData + "DB2 Connection has ever reached the TCBLIMIT, please refer to DB2 report.\n"             


        MaxPkPoolTasks = max(PkPoolTasksList)
        MaxIndex = PkPoolTasksList.index(MaxPkPoolTasks)
        MinPkPoolTasks = min(PkPoolTasksList)
        MinIndex = PkPoolTasksList.index(MinPkPoolTasks)      

        InsightsPkPoolTasks = "Maximum peak pool tasks = " + str(MaxPkPoolTasks) + " (" + ApplidList[MaxIndex] + ")\n" + \
                              "Minmum peak pool tasks = "  + str(MinPkPoolTasks) + " (" + ApplidList[MinIndex] + ")\n"       

        InsightsDB2ENTRY = "\nDB2ENTRY with workload by region (Call Count > 0):\n"
        InsightsDB2ENTRYByApplid = ''
        for i in range(len(ApplidList)):
            if PkConnWithTCBList[i] > 0 and len(DBEntriesList[i]) > 0:
                InsightsDB2ENTRYByApplid = InsightsDB2ENTRYByApplid + ApplidList[i] + ":\n"
                for ii in range(len(DBEntriesList[i])):
                    if  DBEntriesList[i][ii].CallCount > 0:
                        InsightsDB2ENTRYByApplid = InsightsDB2ENTRYByApplid + "- " + str(DBEntriesList[i][ii].DB2EntryName) + " " + \
                                                   "CallCount(" +  str(DBEntriesList[i][ii].CallCount) + ") " + \
                                                   "ThreadCreate(" +  str(DBEntriesList[i][ii].ThreadCreate) + ") " + \
                                                   "ThreadReuse(" +  str(DBEntriesList[i][ii].ThreadReuse) + ") " + \
                                                   "ThreadWtOrOF(" +  str(DBEntriesList[i][ii].ThreadWtOrOF) + ") " + \
                                                   "ThreadHWM(" +  str(DBEntriesList[i][ii].ThreadHWM) + ") " + \
                                                   "ThreadLimit(" +  str(DBEntriesList[i][ii].ThreadLimit) + ") " + \
                                                   "PthreadHWM(" +  str(DBEntriesList[i][ii].PthreadHWM) + ") " + \
                                                   "PthreadLimit(" +  str(DBEntriesList[i][ii].PthreadLimit) + ") " + \
                                                   "ThreadPrty(" +  str(DBEntriesList[i][ii].ThreadPrty) + ")\n"
        if InsightsDB2ENTRYByApplid == '':
            InsightsDB2ENTRY = "No DB2ENTRY was found"
        InsightsDBString = InsightsDBTitle + InsightsPkConnWithTCB + InsightsTCBLIMITRatio + InsightsPkPoolTasks + InsightsDB2ENTRY + InsightsDB2ENTRYByApplid
        return InsightsDBString

    def InsightsNQ(self):                                              #Insights into CICS Enqueue statistics
        if NQFlag == 0:
            return "No Enqueue Statistics was found"        

        ApplidList = list()
        ENQIssdList = list()
        ENQImmSuccessRatioList = list()
        ENQWtdList = list()
        ENQRtndList = list()
        ImmRejEqBsyList = list()
        ImmRejEqRtnList = list()

        for item in self.ItemList:
            ApplidList.append(item.Applid)
            ENQIssdList.append(int(item.NQItem.ENQIssd))
            ENQImmSuccessRatioList.append(float(item.NQItem.ENQImmSuccessRatio))
            ENQWtdList.append(int(item.NQItem.ENQWtd))
            ENQRtndList.append(int(item.NQItem.ENQRtnd))
            ImmRejEqBsyList.append(int(item.NQItem.ImmRejEqBsy))
            ImmRejEqRtnList.append(int(item.NQItem.ImmRejEqRtn))

        MaxENQIssd = max(ENQIssdList)
        MaxIndex = ENQIssdList.index(MaxENQIssd)
        MinENQIssd = min(ENQIssdList)
        MinIndex = ENQIssdList.index(MinENQIssd)      

        InsightsENQIssd = "Maximum enqueue requests = " + str(MaxENQIssd) + " (" + ApplidList[MaxIndex] + ")\n" + \
                          "Minmum enqueue requests = "  + str(MinENQIssd) + " (" + ApplidList[MinIndex] + ")\n"   

        MaxENQImmSuccessRatio = max(ENQImmSuccessRatioList)
        MaxIndex = ENQImmSuccessRatioList.index(MaxENQImmSuccessRatio)
        MinENQImmSuccessRatio = min(ENQImmSuccessRatioList)
        MinIndex = ENQImmSuccessRatioList.index(MinENQImmSuccessRatio)      

        InsightsENQImmSuccessRatio = "Maximum immediate success ratio = " + str(MaxENQImmSuccessRatio) + "% (" + ApplidList[MaxIndex] + ")\n" + \
                              "Minmum immediate success ratio = "  + str(MinENQImmSuccessRatio) + "% (" + ApplidList[MinIndex] + ")\n"           

        if MinENQImmSuccessRatio >= ENQIMMESUCCESSTHRESHOLD:
            InsightsENQImmeSuccessThreshold = "\nAll regions reached Enqueue immediate success ratio threshold (" + str(ENQIMMESUCCESSTHRESHOLD) + "%)\n"
        else:
            self.ScoreOther = self.ScoreOther - 10      #deduct Other score
            self.RiskOther = self.RiskOther + "Enqueue success ratio was ever lower than the threshold. Please see Enqueue Manager report.\n" 
            InsightsENQImmeSuccessThreshold = "\nRegions below Enqueue immediate success ratio threshold:\n"
            for i in range(len(ENQImmSuccessRatioList)):
                if ENQImmSuccessRatioList[i] < ENQIMMESUCCESSTHRESHOLD:
                    InsightsENQImmeSuccessThreshold = InsightsENQImmeSuccessThreshold + "- " + ApplidList[i] + \
                                                      " ENQImmSuccessRatio(" + str(ENQImmSuccessRatioList[i]) + "%) " + \
                                                      " ENQWait(" + str(ENQWtdList[i]) + ") " + \
                                                      " ENQRetained(" + str(ENQRtndList[i]) + ") " + \
                                                      " ENQBusyImmeReject(" + str(ImmRejEqBsyList[i]) + ") " + \
                                                      " ENQRetainedImmReject(" + str(ImmRejEqRtnList[i])

        InsightsNQString = InsightsENQIssd + InsightsENQImmSuccessRatio + InsightsENQImmeSuccessThreshold
        return InsightsNQString

    def InsightsTSQ(self):                                             #Insights into CICS TSQ statistics
        if TSQFlag == 0:
            return "No TSQ Statistics was found" 

        ApplidList = list()
        PutqMainReqsList = list()
        GetqMainReqsList = list()
        PutqAuxReqsList = list()
        GetqAuxReqsList = list()
        PutqShareReqsList = list()
        GetqShareReqsList = list()
        WriteMTCIList = list()
        AuxStgExhList = list()
        TSCompressList = list()
        BufferWaitList = list()
        StringWaitList = list()
        IOErrorTSQDSList = list()


        for item in self.ItemList:
            ApplidList.append(item.Applid)
            PutqMainReqsList.append(int(item.TSQItem.PutqMainReqs))
            GetqMainReqsList.append(int(item.TSQItem.GetqMainReqs))
            PutqAuxReqsList.append(int(item.TSQItem.PutqAuxReqs))
            GetqAuxReqsList.append(int(item.TSQItem.GetqAuxReqs))
            PutqShareReqsList.append(int(item.TSQItem.PutqShareReqs))
            GetqShareReqsList.append(int(item.TSQItem.GetqShareReqs))
            WriteMTCIList.append(int(item.TSQItem.WriteMTCI))
            AuxStgExhList.append(int(item.TSQItem.AuxStgExh))
            TSCompressList.append(int(item.TSQItem.TSCompress))
            BufferWaitList.append(int(item.TSQItem.BufferWait))
            StringWaitList.append(int(item.TSQItem.StringWait))
            IOErrorTSQDSList.append(int(item.TSQItem.IOErrorTSQDS))



        MaxPutqMainReqs = max(PutqMainReqsList)
        MaxIndex = PutqMainReqsList.index(MaxPutqMainReqs)
        MinPutqMainReqs = min(PutqMainReqsList)
        MinIndex = PutqMainReqsList.index(MinPutqMainReqs)

        InsightsPutqMainReqs = "Maximum workload in Main TSQ = " + str(MaxPutqMainReqs) + "(Putq)," + str(GetqMainReqsList[MaxIndex]) + "(Getq) (" + \
                                    ApplidList[MaxIndex] + ")\n" + \
                                    "Minmum Main TSQ = "  + str(MinPutqMainReqs) + "(Putq)," + str(GetqMainReqsList[MinIndex]) + "(Getq) (" + \
                                    ApplidList[MinIndex] + ")\n"  

        MaxPutqAuxReqs = max(PutqAuxReqsList)
        MaxIndex = PutqAuxReqsList.index(MaxPutqAuxReqs)
        MinPutqAuxReqs = min(PutqAuxReqsList)
        MinIndex = PutqAuxReqsList.index(MinPutqAuxReqs)

        InsightsPutqAuxReqs = "Maximum workload in Aux TSQ = " + str(MaxPutqAuxReqs) + "(Putq)," + str(GetqAuxReqsList[MaxIndex]) + "(Getq) (" + \
                                    ApplidList[MaxIndex] + ")\n" + \
                                    "Minmum Aux TSQ = "  + str(MinPutqAuxReqs) + "(Putq)," + str(GetqAuxReqsList[MinIndex]) + "(Getq) (" + \
                                    ApplidList[MinIndex] + ")\n"  

        MaxPutqShareReqs = max(PutqShareReqsList)
        MaxIndex = PutqShareReqsList.index(MaxPutqShareReqs)
        MinPutqShareReqs = min(PutqShareReqsList)
        MinIndex = PutqShareReqsList.index(MinPutqShareReqs)

        InsightsPutqShareReqs = "Maximum workload in Shared TSQ = " + str(MaxPutqShareReqs) + "(Putq)," + str(GetqShareReqsList[MaxIndex]) + "(Getq) (" + \
                                    ApplidList[MaxIndex] + ")\n" + \
                                    "Minmum Share TSQ = "  + str(MinPutqShareReqs) + "(Putq)," + str(GetqShareReqsList[MinIndex]) + "(Getq) (" + \
                                    ApplidList[MinIndex] + ")\n\n"  
        

        MaxWriteMTCI = max(WriteMTCIList)
        MaxIndex = WriteMTCIList.index(MaxWriteMTCI)

        if MaxWriteMTCI > 0:
            self.ScoreQueue = self.ScoreQueue - 5      #deduct Queue score
            self.RiskQueue = self.RiskQueue + "Write More Than CI ever happened. Please see Temporary Storage Queue report.\n"             
 
        InsightsWriteMTCI = "Maximum times (Write More Than CI) = " + str(MaxWriteMTCI) + " (" + ApplidList[MaxIndex] + ")\n"

        MaxAuxStgExh = max(AuxStgExhList)
        MaxIndex = AuxStgExhList.index(MaxAuxStgExh)
        if MaxAuxStgExh > 0:
            self.ScoreQueue = self.ScoreQueue - 10      #deduct Queue score
            self.RiskQueue = self.RiskQueue + "Auxilary storage was ever exhausted. Please see Temporary Storage Queue report.\n"             
 
        InsightsAuxStgExh = "Maximum times (Auxilary Storage Exhausted) = " + str(MaxAuxStgExh) + " (" + ApplidList[MaxIndex] + ")\n"

        MaxTSCompress = max(TSCompressList)
        MaxIndex = TSCompressList.index(MaxTSCompress)
        if MaxTSCompress > 0:
            self.ScoreQueue = self.ScoreQueue - 10      #deduct Queue score
            self.RiskQueue = self.RiskQueue + "Temp storage compressions ever happened. Please see Temporary Storage Queue report.\n"    
 
        InsightsTSCompress = "Maximum times (Temp Storage Compressions) = " + str(MaxTSCompress) + " (" + ApplidList[MaxIndex] + ")\n"

        MaxBufferWait = max(BufferWaitList)
        MaxIndex = BufferWaitList.index(MaxBufferWait)
        if MaxBufferWait > 0:
            self.ScoreQueue = self.ScoreQueue - 5      #deduct Queue score
            self.RiskQueue = self.RiskQueue + "Buffer Wait ever happened. Please see Temporary Storage Queue report.\n"    
 
        InsightsBufferWait = "Maximum times (Buffer Wait) = " + str(MaxBufferWait) + " (" + ApplidList[MaxIndex] + ")\n"


        MaxStringWait = max(StringWaitList)
        MaxIndex = StringWaitList.index(MaxStringWait)
        if MaxStringWait > 0:
            self.ScoreQueue = self.ScoreQueue - 5      #deduct Queue score
            self.RiskQueue = self.RiskQueue + "String Wait ever happened. Please see Temporary Storage Queue report.\n" 
 
        InsightsStringWait = "Maximum times (String Wait) = " + str(MaxStringWait) + " (" + ApplidList[MaxIndex] + ")\n"

        MaxIOErrorTSQDS = max(IOErrorTSQDSList)
        MaxIndex = IOErrorTSQDSList.index(MaxIOErrorTSQDS)
        if MaxIOErrorTSQDS > 0:
            self.ScoreQueue = self.ScoreQueue - 10      #deduct Queue score
            self.RiskQueue = self.RiskQueue + "IO errors in TSQ dataset ever happened. Please see Temporary Storage Queue report.\n"         
 
        InsightsIOErrorTSQDS = "Maximum times (IO errors in TSQ dataset) = " + str(MaxIOErrorTSQDS) + " (" + ApplidList[MaxIndex] + ")\n"


        InsightsTSQString = InsightsPutqMainReqs + InsightsPutqAuxReqs + InsightsPutqShareReqs + \
                            InsightsWriteMTCI + InsightsAuxStgExh + InsightsTSCompress + InsightsBufferWait + InsightsStringWait + InsightsIOErrorTSQDS

        return InsightsTSQString

    def InsightsTDQ(self):                                             #Insights into CICS TDQ statistics
        if TDQFlag == 0:
            return "No TDQ Statistics was found" 

        ApplidList = list()
        PutqIntraReqsList = list()
        GetqIntraReqsList = list()
        BufferWaitList = list()
        StringWaitList = list()
        IOErrorsList = list()
        NOSPACETmsList = list()


        for item in self.ItemList:
            ApplidList.append(item.Applid)
            PutqIntraReqsList.append(int(item.TDQItem.PutqIntraReqs))
            GetqIntraReqsList.append(int(item.TDQItem.GetqIntraReqs))
            NOSPACETmsList.append(int(item.TDQItem.NOSPACETms))
            BufferWaitList.append(int(item.TDQItem.BufferWait))
            StringWaitList.append(int(item.TDQItem.StringWait))
            IOErrorsList.append(int(item.TDQItem.IOErrors))


        MaxPutqIntraReqs = max(PutqIntraReqsList)
        MaxIndex = PutqIntraReqsList.index(MaxPutqIntraReqs)
        MinPutqIntraReqs = min(PutqIntraReqsList)
        MinIndex = PutqIntraReqsList.index(MinPutqIntraReqs)

        InsightsPutqIntraReqs = "Maximum workload in Intra TDQ = " + str(MaxPutqIntraReqs) + "(Putq)," + str(GetqIntraReqsList[MaxIndex]) + "(Getq) (" + \
                                    ApplidList[MaxIndex] + ")\n" + \
                                    "Minmum Intra TDQ = "  + str(MinPutqIntraReqs) + "(Putq)," + str(GetqIntraReqsList[MinIndex]) + "(Getq) (" + \
                                    ApplidList[MinIndex] + ")\n"  


        MaxBufferWait = max(BufferWaitList)
        MaxIndex = BufferWaitList.index(MaxBufferWait)
        if MaxBufferWait > 0:
            self.ScoreQueue = self.ScoreQueue - 5      #deduct Queue score
            self.RiskQueue = self.RiskQueue + "Buffer Wait ever happened. Please see Transient Data Queue report.\n" 

        InsightsBufferWait = "Maximum times (Buffer Wait) = " + str(MaxBufferWait) + " (" + ApplidList[MaxIndex] + ")\n"


        MaxStringWait = max(StringWaitList)
        MaxIndex = StringWaitList.index(MaxStringWait)
        if MaxStringWait > 0:
            self.ScoreQueue = self.ScoreQueue - 5      #deduct Queue score
            self.RiskQueue = self.RiskQueue + "String Wait ever happened. Please see Transient Data Queue report.\n" 

        InsightsStringWait = "Maximum times (String Wait) = " + str(MaxStringWait) + " (" + ApplidList[MaxIndex] + ")\n"

        MaxIOErrors = max(IOErrorsList)
        MaxIndex = IOErrorsList.index(MaxIOErrors)
        if MaxIOErrors > 0:
            self.ScoreQueue = self.ScoreQueue - 5      #deduct Queue score
            self.RiskQueue = self.RiskQueue + "IO errors in TDQ intrapartition dataset ever happened. Please see Transient Data Queue report.\n" 
 
        InsightsIOErrors = "Maximum times (IO errors in TDQ intrapartition dataset) = " + str(MaxIOErrors) + " (" + ApplidList[MaxIndex] + ")\n"

        MaxNOSPACETms = max(NOSPACETmsList)
        MaxIndex = NOSPACETmsList.index(MaxNOSPACETms)
        if MaxNOSPACETms > 0:
            self.ScoreQueue = self.ScoreQueue - 10      #deduct Queue score
            self.RiskQueue = self.RiskQueue + "TDQ NOSPACE condition ever happened. Please see Transient Data Queue report.\n" 
 
        InsightsNOSPACETms = "Maximum times (NOSPACE condition) = " + str(MaxNOSPACETms) + " (" + ApplidList[MaxIndex] + ")\n"

        InsightsTDQString = InsightsPutqIntraReqs + InsightsBufferWait + InsightsStringWait + InsightsIOErrors + InsightsNOSPACETms

        return InsightsTDQString


    def InsightsCON(self):                                             #Insights into CICS Connection statistics
        if CONFlag == 0:
            return "No Connection Statistics was found"    

        ApplidList = list()
        QueuedAllocList = list()
        FailedLinkAllocList = list()
        FailedAllocSessUsedList = list()


        for item in self.ItemList:
            ApplidList.append(item.Applid)
            QueuedAllocList.append(int(item.CONItem.QueuedAlloc))
            FailedLinkAllocList.append(int(item.CONItem.FailedLinkAlloc))
            FailedAllocSessUsedList.append(int(item.CONItem.FailedAllocSessUsed))


        MaxQueuedAlloc = max(QueuedAllocList)
        MaxIndex = QueuedAllocList.index(MaxQueuedAlloc)
        if MaxQueuedAlloc > 0:
            self.ScoreOther = self.ScoreOther - 10      #deduct Other score
            self.RiskOther = self.RiskOther + "Queued allocates of connection existed. Please see Connection report.\n" 
 
        InsightsQueuedAlloc = "Maximum number of queued allocates = " + str(MaxQueuedAlloc) + " (" + ApplidList[MaxIndex] + ")\n"


        MaxFailedLinkAlloc = max(FailedLinkAllocList)
        MaxIndex = FailedLinkAllocList.index(MaxFailedLinkAlloc)
        if MaxFailedLinkAlloc > 0:
            self.ScoreOther = self.ScoreOther - 10      #deduct Other score
            self.RiskOther = self.RiskOther + "Failed link allocates existed. Please see Connection report.\n" 
 
        InsightsFailedLinkAlloc = "Maximum number of failed link allocates = " + str(MaxFailedLinkAlloc) + " (" + ApplidList[MaxIndex] + ")\n"

        MaxFailedAllocSessUsed = max(FailedAllocSessUsedList)
        MaxIndex = FailedAllocSessUsedList.index(MaxFailedAllocSessUsed)
        if MaxFailedAllocSessUsed > 0:
            self.ScoreOther = self.ScoreOther - 10      #deduct Other score
            self.RiskOther = self.RiskOther + "Failed session allocates existed. Please see Connection report.\n" 
 
        InsightsFailedAllocSessUsed = "Maximum number of failed session allocates = " + str(MaxFailedAllocSessUsed) + " (" + ApplidList[MaxIndex] + ")\n"

        InsightsCONString = InsightsQueuedAlloc + InsightsFailedLinkAlloc + InsightsFailedAllocSessUsed

        return InsightsCONString



    def InsightsDU(self):                                             #Insights into CICS Dump statistics
        if DUFlag == 0:
            return "No Dump Statistics was found" 

        ApplidList = list()
        SysDumpTakenList = list()
        SysDumpSuppList = list()
        TranDumpTakenList = list()
        TranDumpSuppList = list()


        for item in self.ItemList:
            ApplidList.append(item.Applid)
            SysDumpTakenList.append(int(item.DUItem.SysDumpTaken))
            SysDumpSuppList.append(int(item.DUItem.SysDumpSupp))
            TranDumpTakenList.append(int(item.DUItem.TranDumpTaken))
            TranDumpSuppList.append(int(item.DUItem.TranDumpSupp))


        MaxSysDumpTaken = max(SysDumpTakenList)
        MaxIndex = SysDumpTakenList.index(MaxSysDumpTaken)
        if MaxSysDumpTaken > 0:
            self.ScoreOther = self.ScoreOther - 10      #deduct Other score
            self.RiskOther = self.RiskOther + "System dump was taken. Please see Dump report.\n"         
 
        InsightsSysDumpTaken = "Maximum system dumps taken = " + str(MaxSysDumpTaken) + " (" + ApplidList[MaxIndex] + ")\n"


        MaxTranDumpTaken = max(TranDumpTakenList)
        MaxIndex = TranDumpTakenList.index(MaxTranDumpTaken)
        if MaxTranDumpTaken > 0:
            self.ScoreOther = self.ScoreOther - 10      #deduct Other score
            self.RiskOther = self.RiskOther + "Transaction dump was taken. Please see Dump report.\n"  
 
        InsightsTranDumpTaken = "Maximum transaction dumps taken = " + str(MaxTranDumpTaken) + " (" + ApplidList[MaxIndex] + ")\n"

        MaxSysDumpSupp = max(SysDumpSuppList)
        MaxIndex = SysDumpSuppList.index(MaxSysDumpSupp)
 
        InsightsSysDumpSupp = "Maximum system dumps Suppressed = " + str(MaxSysDumpSupp) + " (" + ApplidList[MaxIndex] + ")\n"


        MaxTranDumpSupp = max(TranDumpSuppList)
        MaxIndex = TranDumpSuppList.index(MaxTranDumpSupp)
 
        InsightsTranDumpSupp = "Maximum transaction dumps Suppressed = " + str(MaxTranDumpSupp) + " (" + ApplidList[MaxIndex] + ")\n"

        InsightsDUString = InsightsSysDumpTaken + InsightsSysDumpSupp + InsightsTranDumpTaken + InsightsSysDumpSupp

        return InsightsDUString


    def InsightsRiskSummary(self):
        TotalScore = int ((self.ScoreTransaction + self.ScoreDispatch + self.ScoreStorage + self.ScoreData + self.ScoreQueue + self.ScoreOther)/6)
        RiskSummString = "The total score of this interval is " + str(TotalScore) + "\n\n"

        RiskSummString = RiskSummString + "Score by component:\n" + \
                         "- Transaction (Transaction and TClass) = " + str(self.ScoreTransaction) + "\n" + \
                         "- Dispatch (Dispatcher) = " + str(self.ScoreDispatch) + "\n" + \
                         "- Storage (DSA and EDSA) = " + str(self.ScoreStorage) + "\n" + \
                         "- Data (DB2 and logging) = " + str(self.ScoreData) + "\n" + \
                         "- Queue (TSQ and TDQ) = " + str(self.ScoreQueue) + "\n" + \
                         "- Others (Connection, dump and enqueue) = " + str(self.ScoreOther) + "\n\n"

        if TotalScore < 100:
            RiskSummString = RiskSummString + "The following potential risks were observed:\n" + \
                            self.RiskTransaction + self.RiskDispatch + self.RiskStorage + self.RiskData + self.RiskQueue + self.RiskOther 
        return RiskSummString

    def IntervalScoreJSON(self):
        TotalScore = int ((self.ScoreTransaction + self.ScoreDispatch + self.ScoreStorage + self.ScoreData + self.ScoreQueue + self.ScoreOther)/6)
        JSONInfo = "{\"TotalScore\":\"" + str(TotalScore) + "\"" + \
        ",\"ScoreTransaction\":\"" + str(self.ScoreTransaction) + "\"" + \
        ",\"ScoreDispatch\":\"" + str(self.ScoreDispatch) + "\"" + \
        ",\"ScoreStorage\":\"" + str(self.ScoreStorage) + "\"" + \
        ",\"ScoreData\":\"" + str(self.ScoreData) + "\"" + \
        ",\"ScoreQueue\":\"" + str(self.ScoreQueue) + "\"" + \
        ",\"ScoreOther\":\"" + str(self.ScoreOther) + "\"" + \
        "}"
        return JSONInfo 



    def HealthSummary(self):
        InsightsInterval = ''
        if len(self.ItemList) == 0:
            print ("No record is availbale")
            InsightsInterval = "No record is availbale for " + self.ResTimeMin + "~" + self.ColTimeMin + "\n"
            return InsightsInterval
        InsightsInterval = "0==================================================================================\n" + \
                           "Health Summary for interval: " + self.ResTimeMin + "~" + self.ColTimeMin + "\n" + \
                           "0==================================================================================\n"

        InsightsInterval = InsightsInterval + self.InsightsTM() + "\n" + \
                           self.InsightsTC() + "\n" + \
                           self.InsightsDS() + "\n" + \
                           self.InsightsSM() + "\n" + \
                           self.InsightsDB() + "\n" + \
                           self.InsightsLG() + "\n" + \
                           self.InsightsNQ() + "\n" + \
                           self.InsightsTSQ() + "\n" + \
                           self.InsightsTDQ() + "\n" + \
                           self.InsightsCON() + "\n" + \
                           self.InsightsDU() + "\n\n\n"
        return InsightsInterval

    
class IntervalReport:
    IntervalItemList = list()
    SubListbyInterval = list()
    ApplidSet = set()
    ApplidNum = 0
    IntervalSet = set()
    IntervalNum = 0    
    PeakColTime = ''
    PeakTotalTran = 0
    PeakIndex = -1
    ExtractFlag = 0
    
    def __init__(self):
        self.IntervalItemList = list()
        self.SubListbyInterval = list()
        self.ApplidSet = set()
        self.IntervalSet = set()
        self.PeakIndex = -1        

    def AppendItem(self,IntItem):
        self.IntervalItemList.append(IntItem)

    def ExtractInfo(self):
        global PEAK_INTERVAL
        if self.ExtractFlag == 0:
            for item in self.IntervalItemList:
                self.ApplidSet.add(item.Applid)
            self.ApplidNum = len(self.ApplidSet)
            self.IntervalNum = int(len(self.IntervalItemList)/self.ApplidNum)

            for i in range(self.IntervalNum):
                ListByIntervalTemp = IntervalItemListByInterval()
                TotTranTemp = 0           
                for ii in range(self.ApplidNum):
                    ListByIntervalTemp.AppendItem(self.IntervalItemList[self.IntervalNum*ii+i])
                    TotTranTemp = TotTranTemp + self.IntervalItemList[self.IntervalNum*ii+i].TMItem.TotTran

                if TotTranTemp > self.PeakTotalTran:
                    self.PeakTotalTran = TotTranTemp
                    self.PeakIndex = i
                self.SubListbyInterval.append(ListByIntervalTemp)

            self.PeakColTime = self.IntervalItemList[self.PeakIndex].ColTime
            PEAK_INTERVAL = self.IntervalItemList[self.PeakIndex].ResTime + "~" + self.IntervalItemList[self.PeakIndex].ColTime
            self.ExtractFlag = 1

    def SummInfoJSON(self):
        global TMFlag
        global TCFlag
        global DSFlag
        global LGFlag
        global DBFlag
        global NQFlag
        global TSQFlag
        global TDQFlag
        global SMFlag
        global CONFlag
        global DUFlag 
        self.ExtractInfo()
        JSONSumm = ''
        JSONStart = "[\n"
        JSONInfoPart = "{\"APPNUM\":\"" + str(self.ApplidNum) + \
        "\",\"PEAKINTERVALINDEX\":\"" + str(self.PeakIndex) + \
        "\",\"INTERVALNUM\":\"" + str(self.IntervalNum) + \
        "\",\"PEAKINTERVAL\":\"" + str(self.PeakColTime) + \
        "\",\"PEAKTOTALTRAN\":\"" + str(self.PeakTotalTran) + \
        "\",\"TMFLAG\":\"" + str(TMFlag) + \
        "\",\"TCFLAG\":\"" + str(TCFlag) + \
        "\",\"DSFLAG\":\"" + str(DSFlag) + \
        "\",\"LGFLAG\":\"" + str(LGFlag) + \
        "\",\"DBFLAG\":\"" + str(DBFlag) + \
        "\",\"NQFLAG\":\"" + str(NQFlag) + \
        "\",\"TSQFLAG\":\"" + str(TSQFlag) + \
        "\",\"TDQFLAG\":\"" + str(TDQFlag) + \
        "\",\"SMFLAG\":\"" + str(SMFlag) + \
        "\",\"CONFLAG\":\"" + str(CONFlag) + \
        "\",\"DUFLAG\":\"" + str(DUFlag) + "\"}"
        JSONEnd = "\n]"
        JSONSumm = JSONStart + JSONInfoPart + JSONEnd
        return JSONSumm

    def TrendInfoJSON(self):
        self.ExtractInfo()
        JSONTrend = ''
        JSONStart = "[\n"
        JSONTrendItemPart = ''

        for i in range(len(self.IntervalItemList)):
            if i == 0:
                JSONTrendItemPart =  self.IntervalItemList[0].IntervalBriefJSON() 
            else:
                JSONTrendItemPart =  JSONTrendItemPart + ',\n' + self.IntervalItemList[i].IntervalBriefJSON()
        JSONEnd = "\n]"
        JSONTrend = JSONStart + JSONTrendItemPart + JSONEnd
        return JSONTrend          

    def PeakInfoJSON(self):
        self.ExtractInfo()
        JSONPeak = ''
        JSONStart = "[\n"
        JSONPeakItemPart = ''
        for i in range(self.ApplidNum):
            if i == 0:
                JSONPeakItemPart =  self.IntervalItemList[self.IntervalNum*i+self.PeakIndex].IntervalFullJSON()
            else:
                JSONPeakItemPart =  JSONPeakItemPart + ',\n' + self.IntervalItemList[self.IntervalNum*i+self.PeakIndex].IntervalFullJSON()
        JSONEnd = "\n]"
        JSONPeak = JSONStart + JSONPeakItemPart + JSONEnd        
        return JSONPeak

    def PeakScoreJSON(self):
        self.ExtractInfo()
        JSONScore = ''
        JSONStart = "[\n"       
        ScoreString = '' 
        if self.PeakIndex >= 0:
            ScoreString = self.SubListbyInterval[self.PeakIndex].IntervalScoreJSON()
        JSONEnd = "\n]"
        JSONScore = JSONStart + ScoreString + JSONEnd   
        return JSONScore

    def InsightsTMPeak(self):
        InsightsTMString = ''
        if self.PeakIndex >= 0:
            InsightsTMString = self.SubListbyInterval[self.PeakIndex].InsightsTM()
        return InsightsTMString


    def InsightsDSPeak(self):
        InsightsDSString = ''
        if self.PeakIndex >= 0:
            InsightsDSString = self.SubListbyInterval[self.PeakIndex].InsightsDS()
        return InsightsDSString


    def InsightsSMPeak(self):
        InsightsSMString = ''
        if self.PeakIndex >= 0:
            InsightsSMString = self.SubListbyInterval[self.PeakIndex].InsightsSM()
        return InsightsSMString

    def InsightsTCPeak(self):
        InsightsTCString = ''
        if self.PeakIndex >= 0:
            InsightsTCString = self.SubListbyInterval[self.PeakIndex].InsightsTC()
        return InsightsTCString   

    def InsightsLGPeak(self):
        InsightsLGString = ''
        if self.PeakIndex >= 0:
            InsightsLGString = self.SubListbyInterval[self.PeakIndex].InsightsLG()
        return InsightsLGString    

    def InsightsDBPeak(self):
        InsightsDBString = ''
        if self.PeakIndex >= 0:
            InsightsDBString = self.SubListbyInterval[self.PeakIndex].InsightsDB()
        return InsightsDBString    

    def InsightsNQPeak(self):
        InsightsNQString = ''
        if self.PeakIndex >= 0:
            InsightsNQString = self.SubListbyInterval[self.PeakIndex].InsightsNQ()
        return InsightsNQString   


    def InsightsTSQPeak(self):
        InsightsTSQString = ''
        if self.PeakIndex >= 0:
            InsightsTSQString = self.SubListbyInterval[self.PeakIndex].InsightsTSQ()
        return InsightsTSQString  


    def InsightsTDQPeak(self):
        InsightsTDQString = ''
        if self.PeakIndex >= 0:
            InsightsTDQString = self.SubListbyInterval[self.PeakIndex].InsightsTDQ()
        return InsightsTDQString  

    def InsightsCONPeak(self):
        InsightsCONString = ''
        if self.PeakIndex >= 0:
            InsightsCONString = self.SubListbyInterval[self.PeakIndex].InsightsCON()
        return InsightsCONString 

    def InsightsDUPeak(self):
        InsightsDUString = ''
        if self.PeakIndex >= 0:
            InsightsDUString = self.SubListbyInterval[self.PeakIndex].InsightsDU()
        return InsightsDUString 

    def InsightsScorePeak(self):
        InsightsScoreString = ''
        if self.PeakIndex >= 0:
            InsightsScoreString = self.SubListbyInterval[self.PeakIndex].InsightsRiskSummary()
        return InsightsScoreString         

    def InsightsAllInterval(self):
        InsightsAll = ''
        for item in self.SubListbyInterval:
            InsightsAll = InsightsAll + item.HealthSummary()

        fo_sum = open(reportDirName+'//HealthSummary.txt', 'w')
        fo_sum.write(InsightsAll)
        fo_sum.close()



#######################################################################################################################################
#Read user defined configration from config.json
#Set default configration
#Mainline codes begin from here

global CICSVERSION
global TCLASSNAME
global DSARATIOTHRES
global EDSARATIOTHRES
global QRDISPRATIOTHRES
global ENQIMMESUCCESSTHRESHOLD
global APPLIDPATTERN
global STATISTICS_DATE
global PEAK_INTERVAL
global TMFlag
global DSFlag
global LGFlag
global DBFlag
global NQFlag
global TSQFlag
global TDQFlag
global SMFlag
global CONFlag
global DUFlag

TCLASSNAME = "*TOTALS*"                #Change the TClass name per your request, by default TOTALS is used
ENQNAME = "*TOTALS*"                   #Change the ENQ name per your request, by default TOTALS is used
CICSVERSION = "6.8.0"
DSARATIOTHRES = 90.00
EDSARATIOTHRES = 90.00
QRDISPRATIOTHRES = 50.00
ENQIMMESUCCESSTHRESHOLD = 90.00
STATISTICS_DATE = ''
PEAK_INTERVAL = ''
TMFlag = 0
TCFlag = 0
DSFlag = 0
LGFlag = 0
DBFlag = 0
NQFlag = 0
TSQFlag = 0
TDQFlag = 0
SMFlag = 0
CONFlag = 0
DUFlag = 0

fo_json = open("config.json","r")
config = json.load(fo_json)
fo_json.close()
TCLASSNAME = config["TClassName"]
ENQNAME = config["ENQName"] 
DSARATIOTHRES = float(config["DSARatioThreshold"])
EDSARATIOTHRES = float(config["EDSARatioThreshold"])
QRDISPRATIOTHRES = float(config["QRDispRatioThreshold"])
ENQIMMESUCCESSTHRESHOLD = float(config["ENQImmeSuccessThreshold"])
APPLIDPATTERN = config["ApplidPattern"]
    

#######################################################################################################################################
STATISTICS_DATE = ''

#######################################################################################################################################
#Main logic begins here
#######################################################################################################################################
#Open a directory and select CICS Statistics
InitialDir = os.getcwd()      
input_filename = tkFileDialog.askopenfilename(initialdir = InitialDir)
fo_input = open(input_filename,"r")
print("Begin to analyze file: " + fo_input.name)

try:
    LineList = fo_input.readlines()
except MemoryError:
    print ("Storage is enough to scan the statistics file.")
    exit()

#Check CICS version by reading utility program version
'''
    1CICS 6.8.0 STATISTICS UTILITY PROGRAM                                    REPORT DATE 06/01/2015   REPORT TIME 17:05:14   PAGE     1
'''
CICSVERSION = 'null'
CICSVersion2 = 'null'

if  "1CICS" in LineList[0].upper() and "STATISTICS UTILITY PROGRAM" in LineList[0].upper():
    CICSVERSION = LineList[0].split(" ")[1]
    if CICSVERSION == '7.0.0':
        CICSVersion2 = '5.3'
    if CICSVERSION == '6.9.0':
        CICSVersion2 = '5.2'
    if CICSVERSION == '6.8.0':
        CICSVersion2 = '5.1'
    if CICSVERSION == '6.7.0':
        CICSVersion2 = '4.2'
    if CICSVERSION == '6.6.0':
        CICSVersion2 = '4.1'

if CICSVERSION == 'null':
    print ("The CICS statistics file is not valid.")
    exit()

if CICSVersion2 == 'null':
    print ("The CICS statistics version is not supported")
    print ("The tool only supports CICS 4.1 (660) and above")
    exit()

#Initialize classes
IntItem = IntervalItem()
IntRep = IntervalReport()
IntRep.AppendItem(IntItem)
TMRep = TransactionManagerReport()
DSRep = DispatcherReport()
NQRep = EnqueueReport()
SMRep = StorageManagerReport()
LGRep = LogManagerReport()
DBRep = DB2Report()
DURep = DumpReport()
TSQRep = TSQReport()
TDQRep = TDQReport()
CONRep = ConnectionReport()


index = 1
while (index < len(LineList)):
    line = LineList[index]
    '''
    +___________________________________________________________________________________________________________________________________
     Interval Report     5 (02:00:00)   Collection Date-Time 06/01/2015-10:00:00  Last Reset 08:00:00  Applid CP11P1AA  Jobname CP11P1AA
     END OF DAY STATISTICS REPORT       COLLECTION DATE-TIME 01/21/2014-00:00:00  LAST RESET 23:00:00  APPLID CIP1BAB2  JOBNAME CIP1BAB2
     Requested Statistics Report        Collection Date-Time 08/15/2015-08:55:00  Last Reset 00:00:00  Applid CI1ACAA1  Jobname CI1ACAA1
    +___________________________________________________________________________________________________________________________________     
    '''
    if "INTERVAL REPORT" in line.upper() :  #Interval Report Information
        ReportType = 0
        CIntervalInfoList = line.split(" ")                                  
        CIntervalInfoList = [x for x in CIntervalInfoList if x != '']
        CApplid = CIntervalInfoList[11]
        CCollectDate = CIntervalInfoList[6].split("-")[0]
        CCollectTime = CIntervalInfoList[6].split("-")[1]
        CLastResetTime = CIntervalInfoList[9]
        CInterval = CIntervalInfoList[3].lstrip('(')
        CInterval = CInterval.strip(')')

        HHMMSS = CInterval.split(":")
        HH=HHMMSS[0]
        MM=HHMMSS[1]
        SS=HHMMSS[2]
        CIntervalV = int(HH)*3600 + int(MM)*60 + float(SS)

        if ApplidMatch(APPLIDPATTERN,CApplid) == 0:     #Skip the APPLID
            flag = 0
            while ( index + 1 < len(LineList)):
                newline = LineList[index+1]
                if "INTERVAL REPORT" in newline.upper() or \
                "END OF DAY STATISTICS REPORT" in newline.upper() or \
                "REQUESTED STATISTICS REPORT" in newline.upper():
                    if newline != line:  #find a new interval
                        flag  = 1
                        break
                index = index + 1
            if flag  == 1:
                continue
            else:
                break 

        if CApplid != IntItem.Applid or CLastResetTime != IntItem.ResTime:   #found a new interval
            if IntItem.Applid == '' and IntItem.ResTime == '':               #this is the first interval
                IntItem.Applid = CApplid
                IntItem.Date = CCollectDate
                IntItem.ResTime = CLastResetTime
                IntItem.ColTime = CCollectTime
                IntItem.UpdateIntervalInfo()
                STATISTICS_DATE = IntItem.Date
            else:
                IntItem = IntervalItem()
                IntRep.AppendItem(IntItem)
                IntItem.Applid = CApplid
                IntItem.Date = CCollectDate
                IntItem.ResTime = CLastResetTime
                IntItem.ColTime = CCollectTime
                IntItem.UpdateIntervalInfo()

    if "END OF DAY STATISTICS REPORT" in line.upper():          #End of Day Statistics
        ReportType = 1                                             
        CIntervalInfoList = line.split(" ")                                  
        CIntervalInfoList = [x for x in CIntervalInfoList if x != '']
        CApplid = CIntervalInfoList[12]
        CCollectDate = CIntervalInfoList[7].split("-")[0]
        CCollectTime = CIntervalInfoList[7].split("-")[1]
        CLastResetTime = CIntervalInfoList[10]
      
        if CCollectTime == "00:00:00":
            CCollectTime = "24:00:00"

        CColHour = CCollectTime.split(":")[0]

        CIntervalV =   int(CColHour)*3600 + int(CCollectTime.split(":")[1])*60 + float(CCollectTime.split(":")[2]) \
                     - int(CLastResetTime.split(":")[0])*3600 - int(CLastResetTime.split(":")[1])*60 - float(CLastResetTime.split(":")[2])
        if float (CIntervalV) == 0:
            CIntervalV = float (24*60*60)


        if ApplidMatch(APPLIDPATTERN,CApplid) == 0:     #Skip the APPLID if it is not in the specified APPLID
            flag = 0
            while ( index + 1 < len(LineList)):
                newline = LineList[index+1]
                if "INTERVAL REPORT" in newline.upper() or \
                "END OF DAY STATISTICS REPORT" in newline.upper() or \
                "REQUESTED STATISTICS REPORT" in newline.upper():
                    if newline != line:  #find a new interval                      
                        flag  = 1
                        break
                index = index + 1
            if flag  == 1:
                continue
            else:
                break 


        if CApplid != IntItem.Applid or CLastResetTime != IntItem.ResTime:   #found a new interval
            if IntItem.Applid == '' and IntItem.ResTime == '':               #this is the first interval
                IntItem.Applid = CApplid
                IntItem.Date = CCollectDate
                IntItem.ResTime = CLastResetTime
                IntItem.ColTime = CCollectTime
                IntItem.UpdateIntervalInfo()
                STATISTICS_DATE = IntItem.Date
            else:
                IntItem = IntervalItem()
                IntRep.AppendItem(IntItem)
                IntItem.Applid = CApplid
                IntItem.Date = CCollectDate
                IntItem.ResTime = CLastResetTime
                IntItem.ColTime = CCollectTime
                IntItem.UpdateIntervalInfo()

    if "REQUESTED STATISTICS REPORT" in line.upper():          #Requested Statistics
        ReportType = 2                                            
        CIntervalInfoList = line.split(" ")                                  
        CIntervalInfoList = [x for x in CIntervalInfoList if x != '']
        CApplid = CIntervalInfoList[10]
        CCollectDate = CIntervalInfoList[5].split("-")[0]
        CCollectTime = CIntervalInfoList[5].split("-")[1]
        CLastResetTime = CIntervalInfoList[8]

        CColHour = CCollectTime.split(":")[0]
        if CColHour == "00":
            CColHour =  "24"
        CIntervalV =   int(CColHour)*3600 + int(CCollectTime.split(":")[1])*60 + float(CCollectTime.split(":")[2]) \
                     - int(CLastResetTime.split(":")[0])*3600 - int(CLastResetTime.split(":")[1])*60 - float(CLastResetTime.split(":")[2])
        if float (CIntervalV) == 0:
            CIntervalV = float (24*60*60)

        if ApplidMatch(APPLIDPATTERN,CApplid) == 0:     #Skip the APPLID if it is not in the specified APPLID
            flag = 0
            while ( index + 1 < len(LineList)):
                newline = LineList[index+1]
                if "INTERVAL REPORT" in newline.upper() or \
                "END OF DAY STATISTICS REPORT" in newline.upper() or \
                "REQUESTED STATISTICS REPORT" in newline.upper():
                    if newline != line:  #find a new interval
                        flag  = 1
                        break
                index = index + 1
            if flag  == 1:
                continue
            else:
                break 

        if CApplid != IntItem.Applid or CLastResetTime != IntItem.ResTime:   #found a new interval
            if IntItem.Applid == '' and IntItem.ResTime == '':               #this is the first interval
                IntItem.Applid = CApplid
                IntItem.Date = CCollectDate
                IntItem.ResTime = CLastResetTime
                IntItem.ColTime = CCollectTime
                IntItem.UpdateIntervalInfo()
                STATISTICS_DATE = IntItem.Date
            else:
                IntItem = IntervalItem()
                IntRep.AppendItem(IntItem)
                IntItem.Applid = CApplid
                IntItem.Date = CCollectDate
                IntItem.ResTime = CLastResetTime
                IntItem.ColTime = CCollectTime
                IntItem.UpdateIntervalInfo()

    if "SUMMARY REPORT" in line.upper():          #Summary Report, we don't support Summary Report
        while ( index + 1 < len(LineList)):
            line = LineList[index+1]
            if "INTERVAL REPORT" in line.upper() or \
            "END OF DAY STATISTICS REPORT" in line.upper() or \
            "REQUESTED STATISTICS REPORT" in line.upper():
                break
            index = index + 1
        if index + 1 == len(LineList):
            break
        else:
            continue                
    '''
    CICS 680 and below:
    +___________________________________________________________________________________________________________________________________
    0TRANSACTION MANAGER STATISTICS
    +______________________________

       Total number of transactions (user + system)                      :                         14,018
    0  Current MAXTASKS limit                                            :                            250
       Current number of active user transactions                        :                             19
       Current number of MAXTASK queued user transactions                :                              0
    0  Times the MAXTASKS limit reached                                  :                              0
    0  Peak number of MAXTASK queued user transactions                   :                              0
       Peak number of active user transactions                           :                             31
       Total number of active user transactions                          :                          14008
       Total number of MAXTASK delayed user transactions                 :                              0
    0  Total MAXTASK queuing time                                        :                   000-00:00:00
       Total MAXTASK queuing time of currently queued user transactions  :                       00:00:00

    CICS 690:
    +___________________________________________________________________________________________________________________________________
    0TRANSACTION MANAGER STATISTICS
    +______________________________

       Total number of transactions (user + system)                      :                             42
    0  Current MAXTASKS limit                                            :                            200
       Time MAXTASKS last changed                                        :      10/30/2015  18:26:04.5772
       Current number of active user transactions                        :                             13
       Time last transaction attached                                    :      11/05/2015  17:00:00.2405
       Current number of MAXTASK queued user transactions                :                              0
    0  Times the MAXTASKS limit reached                                  :                              0
       Time the MAXTASKS limit last reached                              :      --/--/----  --:--:--.----
       Currently at MAXTASKS limit                                       :                             No
    0  Peak number of MAXTASK queued user transactions                   :                              0
       Peak number of active user transactions                           :                             14
       Total number of active user transactions                          :                             42
       Total number of MAXTASK delayed user transactions                 :                              0
    0  Total MAXTASK queuing time                                        :                   000-00:00:00
       Total MAXTASK queuing time of currently queued user transactions  :                       00:00:00
    '''
    if "0TRANSACTION MANAGER STATISTICS" in line:
        TMFlag = 1
        OffsetTotlTran = 3
        OffsetMXT = 4
        OffsetMXTTimeRch = 7
        OffsetPkQued = 8
        OffsetPkActv = 9
        OffsetTotlActUsr = 10
        OffsetToltlDly = 11

        if CICSVERSION == "6.9.0":
            OffsetMXTTimeRch = 9
            OffsetPkQued = 12
            OffsetPkActv = 13
            OffsetTotlActUsr = 14
            OffsetToltlDly = 15

        CTotlTran = LineList[index+OffsetTotlTran].split(" ")                               #Total transaction number, split with BLANK
        CTotlTran = [x for x in CTotlTran if x != '']
        CTotlTran = CTotlTran[8].strip("\r\n")
        if ',' in CTotlTran:
            CTotlTran = CTotlTran.split(",")[0]+CTotlTran.split(",")[1]
  
        CMXT = LineList[index + OffsetMXT].split(" ")                                #MXT setting
        CMXT = [x for x in CMXT if x != '']                         
        CMXT = CMXT[5].strip("\r\n")


        CMXTTimeRch = LineList[index + OffsetMXTTimeRch].split(" ")                         #Times reach MXT
        CMXTTimeRch = [x for x in CMXTTimeRch if x != '']
        CMXTTimeRch =  CMXTTimeRch[7].strip("\r\n")


        CPkQued = LineList[index + OffsetPkQued].split(" ")                             #Peak queued request due to MXT
        CPkQued = [x for x in CPkQued if x != '']
        CPkQued =  CPkQued[9].strip("\r\n")


        CPkActv = LineList[index + OffsetPkActv].split(" ")                             #Peak Active user transaction number
        CPkActv = [x for x in CPkActv if x != '']
        CPkActv =  CPkActv[7].strip("\r\n")


        CTotlActUsr = LineList[index + OffsetTotlActUsr].split(" ")                         #Total Active user transaction number
        CTotlActUsr = [x for x in CTotlActUsr if x != '']
        CTotlActUsr =  CTotlActUsr[7].strip("\r\n")


        CTotlDly = LineList[index + OffsetToltlDly].split(" ")                            #Total MXT delayed user transaction
        CTotlDly = [x for x in CTotlDly if x != '']
        CTotlDly =  CTotlDly[8].strip("\r\n")


        CMXTRatio = '%.2f'%(float(CPkActv) / float(CMXT) * 100)         #Peak Active/MXT
 
        IntItem.TMItem.TotTran = StringToInt(CTotlTran)
        IntItem.TMItem.MXT = StringToInt(CMXT)
        IntItem.TMItem.MXTTimeRch = StringToInt(CMXTTimeRch)
        IntItem.TMItem.PkQued = StringToInt(CPkQued)
        IntItem.TMItem.PkActv = StringToInt(CPkActv)
        IntItem.TMItem.TotlActUsr = StringToInt(CTotlActUsr)
        IntItem.TMItem.TotlDly = StringToInt(CTotlDly)
        IntItem.TMItem.MXTRatio = CMXTRatio
        IntItem.TMItem.IntervalValue = CIntervalV      

        TMRep.AppendItem(IntItem.TMItem)

        index = index + 13                                                    #Skip the Transaction Manager Statistics


    '''
    CICS all version:
    +___________________________________________________________________________________________________________________________________
    0TRANSACTION CLASS STATISTICS
    +____________________________
    0
    0  Tclass   Number Max  Purge  <----------------- T O T A L ------------------>  Peak   Peak  Times Times   <-- C U R R E N T -->
       Name    Trandfs Act Thresh  Attaches  AcptImm PrgImm Queued PrgQ'd    Q-Time   Act Queued MaxAct PrgThr  Act  Queued QueueTime
    +  ______________________________________________________________________________________________________________________________
       TCLBCSN       0  10      1         0        0      0      0      0  00:00:00     0      0      0      1    0       0  00:00:00
       TCL0001       0   1      0         0        0      0      0      0  00:00:00     0      0      0      0    0       0  00:00:00
       TCL0002       0   1      0         0        0      0      0      0  00:00:00     0      0      0      0    0       0  00:00:00
       TCL0003       0   1      0         0        0      0      0      0  00:00:00     0      0      0      0    0       0  00:00:00
       TCL0004       0   1      0         0        0      0      0      0  00:00:00     0      0      0      0    0       0  00:00:00
       ______________________________________________________________________________________________________________________________
       *TOTALS*      3                    0        0      0      0      0  00:00:00                   0      1    0       0  00:00:00
    '''
    if "0TRANSACTION CLASS STATISTICS" in line.upper():
        TCFlag = 1
        index = index + 6
        while "*TOTALS*" not in LineList[index+1]:    #Force to break the while loop
            if "DFH" in LineList[index]:              #Ingnore CICS supplied TCLASS
                index = index + 1                     #Continue the loop
                continue
            if "ACT  QUEUED QUEUETIME" in LineList[index].upper():
                index = index + 1                     #Continue the loop
                continue    
            CTClassEntry = LineList[index].split(" ")                   
            CTClassEntry = [x for x in CTClassEntry if x != '']
            if len(CTClassEntry) == 17:                                  #Find a valid entry that was included in the supplied TClass list
                TCItem = TClassItem()
                CTClassName = CTClassEntry[0]                            #Transaction class name
                CTClassDef   = CTClassEntry[1]                           #Transaction class definition number
                CTClassMaxAct = CTClassEntry[2]                          #Max Active setting
                CTClassPrgThrd = CTClassEntry[3]                         #Purge Threshold setting
                CTClassAtt   = CTClassEntry[4]                           #Transaction attached
                CTClassAcptImm  = CTClassEntry[5]                        #Transaction accepted immediately
                CTClassPrgImm = CTClassEntry[6]                          #Transaction purged immediately
                CTClassQued = CTClassEntry[7]                            #Transaction queued for TCLASS
                CTClassPrgQd = CTClassEntry[8]                           #Transaction purged while queuing for TCLASS
                CTClassQuedTime = CTClassEntry[9]                        #Queued time due to TCLASS
                CTClassPckAct = CTClassEntry[10]                         #Peak active
                CTClassPckQued = CTClassEntry[11]                        #Peak queued
                CTClassMaxActTims = CTClassEntry[12]                     #MXT Active times
                CTClassPrgThrsTims = CTClassEntry[13]                    #Purge Threshold times

                TCItem.TClassName = CTClassName
                TCItem.TClassDef = StringToInt(CTClassDef)
                TCItem.MaxAct = StringToInt(CTClassMaxAct)   
                TCItem.PrgThrs = StringToInt(CTClassPrgThrd)
                TCItem.Attaches = StringToInt(CTClassAtt)
                TCItem.AcptImm = StringToInt(CTClassAcptImm)
                TCItem.PrgImm = StringToInt(CTClassPrgImm)
                TCItem.Queued = StringToInt(CTClassQued)
                TCItem.PrgQd = StringToInt(CTClassPrgQd)
                TCItem.QuedTime = CTClassQuedTime
                TCItem.PckAct = StringToInt(CTClassPckAct)
                TCItem.PckQued = StringToInt(CTClassPckQued)
                TCItem.MaxActTimes = StringToInt(CTClassMaxActTims)
                TCItem.PrgThrsTimes = StringToInt(CTClassPrgThrsTims)

                IntItem.TMItem.AppendTClassItem(TCItem)
            index = index + 1 #Increase index for the while loop

        index = index + 1  #Go to the TOTALS

        if "*TOTALS*" in LineList[index]:
            TCItem = TClassItem()                                  
            CTClassName = "*TOTALS*"
            CTClassEntry = LineList[index].split(" ")                    #Extract TCLASS Total summary
            CTClassEntry = [x for x in CTClassEntry if x != '']
            if CTClassEntry[0] == '0':
                del CTClassEntry[0]
            CTClassDef   = CTClassEntry[1]                               #Transaction class definition number
            CTClassMaxAct = '-1'                                          #Max Active setting
            CTClassPrgThrd = '-1'                                         #Purge Threshold setting
            CTClassAtt   = CTClassEntry[2]                               #Transaction attached
            CTClassAcptImm  = CTClassEntry[3]                            #Transaction accepted immediately
            CTClassPrgImm = CTClassEntry[4]                              #Transaction purged immediately
            CTClassQued = CTClassEntry[5]                                #Transaction queued for TCLASS
            CTClassPrgQd = CTClassEntry[6]                               #Transaction purged while queuing for TCLASS
            CTClassQuedTime = CTClassEntry[7]                            #Queued time due to TCLASS
            CTClassPckAct = '-1'                                         #Peak active
            CTClassPckQued = '-1'                                        #Peak queued
            CTClassMaxActTims = CTClassEntry[8]                          #MXT Active times
            CTClassPrgThrsTims = CTClassEntry[9]                         #Purge Threshold times


            TCItem.TClassName = CTClassName
            TCItem.TClassDef = StringToInt(CTClassDef)
            TCItem.MaxAct = StringToInt(CTClassMaxAct)   
            TCItem.PrgThrs = StringToInt(CTClassPrgThrd)
            TCItem.Attaches = StringToInt(CTClassAtt)
            TCItem.AcptImm = StringToInt(CTClassAcptImm)
            TCItem.PrgImm = StringToInt(CTClassPrgImm)
            TCItem.Queued = StringToInt(CTClassQued)
            TCItem.PrgQd = StringToInt(CTClassPrgQd)
            TCItem.QuedTime = CTClassQuedTime
            TCItem.PckAct = StringToInt(CTClassPckAct)
            TCItem.PckQued = StringToInt(CTClassPckQued)
            TCItem.MaxActTimes = StringToInt(CTClassMaxActTims)
            TCItem.PrgThrsTimes = StringToInt(CTClassPrgThrsTims)

            IntItem.TMItem.AppendTClassItem(TCItem)

    '''
    CICS 680 and below:
    +___________________________________________________________________________________________________________________________________
    0DISPATCHER STATISTICS
    +_____________________
    0  Dispatcher Start Date and Time. . . . . . . : 09/08/2015  22:10:53.1448
    0  Address Space CPU Time. . . . . . . . . . . :     00:02:35.494468
       Address Space SRB Time. . . . . . . . . . . :     00:00:00.405410
    0  Current number of dispatcher tasks. . . . . :                  46
       Peak number of dispatcher tasks . . . . . . :                  65
    0  Current ICV time (msec) . . . . . . . . . . :               10000
       Current ICVR time (msec). . . . . . . . . . :               10000
       Current ICVTSD time (msec). . . . . . . . . :                   0
    0  Current PRTYAGE time (msec) . . . . . . . . :                 500
    0  Current MRO (QR) Batching (MROBTCH) value . :                   1
    0  Number of Excess TCB Scans. . . . . . . . . :                   1
       Excess TCB Scans - No TCB Detached. . . . . :                   1
       Number of Excess TCBs Detached. . . . . . . :                   0
       Average Excess TCBs Detached per Scan . . . :                   0
    0  Number of CICS TCB MODEs. . . . . . . . . . :                  18
    0  Number of CICS TCB POOLs. . . . . . . . . . :                   4

    CICS 690:
    +___________________________________________________________________________________________________________________________________
    0DISPATCHER STATISTICS
    +_____________________
    0  Dispatcher Start Date and Time. . . . . . . : 10/30/2015  18:25:56.3139
    0  Address Space CPU Time. . . . . . . . . . . :     00:00:00.324791
       Address Space SRB Time. . . . . . . . . . . :     00:00:00.017099
    0  Current number of dispatcher tasks. . . . . :                  37
       Peak number of dispatcher tasks . . . . . . :                  38
    0  Current ICV time (msec) . . . . . . . . . . :                5000
       Current ICVR time (msec). . . . . . . . . . :                 500
       Current ICVTSD time (msec). . . . . . . . . :                 500
    0  Current PRTYAGE time (msec) . . . . . . . . :                   0
    0  Current MRO (QR) Batching (MROBTCH) value . :                   1
    0  Last Excess TCB Scan. . . . . . . . . . . . : 11/05/2015  16:56:35.7644
       Number of Excess TCB Scans. . . . . . . . . :                   6
       Last Excess TCB Scan - No TCB Detached. . . : 11/05/2015  16:56:35.7644
       Excess TCB Scans - No TCB Detached. . . . . :                   6
       Number of Excess TCBs Detached. . . . . . . :                   0
       Average Excess TCBs Detached per Scan . . . :                   0
    0  Number of CICS TCB MODEs. . . . . . . . . . :                  18
    0  Number of CICS TCB POOLs. . . . . . . . . . :                   4

    '''
    if "Dispatcher Start Date and Time" in line or "Dispatcher Start Date and Time".upper() in line:         
        DSFlag = 1           
        CASCPU = LineList[index+1].split(" ")                                 #Address Space CPU time
        CASCPU = [x for x in CASCPU if x != '']
        CASCPU = CASCPU[16].strip("\r\n")
        HHMMSS = CASCPU.split(":")
        HH=HHMMSS[0]
        MM=HHMMSS[1]
        SS=HHMMSS[2]    
        CASCPUV = float(HH)*3600 + float(MM)*60 + float(SS)       
        CASCPUUtilRatio = '%.2f'%(float(CASCPUV)/float(CIntervalV)*100)

        CASSRB = LineList[index + 2].split(" ")                             #Address Space SRB time
        CASSRB = [x for x in CASSRB if x != '' and x != '.']
        CASSRB = CASSRB[5].strip("\r\n")
        HHMMSS = CASSRB.split(":")
        HH=HHMMSS[0]
        MM=HHMMSS[1]
        SS=HHMMSS[2]
        CASSRBV = float(HH)*3600 + float(MM)*60 + float(SS)
        CSRBUtilRatio = '%.2f'%(float(CASSRBV)/float(CIntervalV)*100)

        CICV = LineList[index + 5].split(" ")                               #ICV setting
        CICV = [x for x in CICV if x != '' and x != '.']
        CICV = CICV[6].strip("\r\n")

        CICVR = LineList[index + 6].split(" ")                              #ICVR setting
        CICVR = [x for x in CICVR if x != '' and x != '.']
        CICVR = CICVR[5].strip("\r\n")

        if IntItem.TMItem.TotlActUsr == 0:
                CCPUPerUsrTran = 0.00
        else: 
                CCPUPerUsrTran = '%.6f'%((float(CASSRBV)+float(CASCPUV))/IntItem.TMItem.TotlActUsr)
        
        IntItem.DSItem.ASCPU = CASCPU
        IntItem.DSItem.ASCPUUtilRatio = CASCPUUtilRatio
        IntItem.DSItem.ASSRB = CASSRB
        IntItem.DSItem.SRBCPUUtilRatio = CSRBUtilRatio
        IntItem.DSItem.CPUPerUsrTran = CCPUPerUsrTran
        IntItem.DSItem.ICV = StringToInt(CICV)
        IntItem.DSItem.ICVR = StringToInt(CICVR)
        
        IntItem.DSItem.ASCPUV = CASCPUV
        IntItem.DSItem.ASSRBV = CASSRBV

        index = index + 6                                                  #Skip first part of Dispatcher Statistics
 
    if "0  Number of CICS TCB MODEs" in line or "0  Number of CICS TCB MODEs".upper() in line:  
        TCBModeNum = LineList[index].split(" ")
        TCBModeNum = [x for x in TCBModeNum if x != '' and x != '.']
        TCBModeNum = int(TCBModeNum[7].strip("\r\n"))

    '''
    Only applicable for CICS 690
    +___________________________________________________________________________________________________________________________________
    0DISPATCHER STATISTICS
    +_____________________
    -  CICS TCB Mode Statistics
    +  ________________________
    0  TCB         TCB    < TCBs Attached >   <- TCBs In Use ->     TCB     <-  Dispatchable Queue   ->
       Mode  Open  Pool   Current      Peak   Current      Peak  Attaches   Current      Peak   Average
    +  ________________________________________________________________________________________________
    0   QR    No   N/A          1         1         1         1         0         1         2      1.00
    '''
    if "Dispatchable Queue" in line or "Dispatchable Queue".upper() in line:
        CQRDispQueue = LineList[index + 3].split(" ")                        #QR dispatch information
        CQRDispQueue = [x for x in CQRDispQueue if x != '']
        CQRDispQueuePk = CQRDispQueue[10]
        CQRDispQueueAvg = CQRDispQueue[11].strip("\r\n")    

        IntItem.DSItem.QRDspQueuePk = StringToInt(CQRDispQueuePk)
        IntItem.DSItem.QRDspQueueAvg = float(CQRDispQueueAvg)

        index = index + 20

    '''
    CICS all version:
    -  TCB    < TCBs Attached >     TCB     Attach        MVS       Accumulated          Accumulated          Accumulated
       Mode   Current      Peak  Attaches  Failures     Waits    Time in MVS wait      Time Dispatched         Time / TCB
    +  _____________________________________________________________________________________________________________________
    0   QR          1         1         0         0    128737      00:04:07.708795      00:00:54.339366      00:00:17.903181
        RO          1         1         0         0     30434      00:04:52.666838      00:00:07.328270      00:00:04.339738
        CO          0         0         0         0         0      00:00:00.000000      00:00:00.000000      00:00:00.000000
        SZ          0         0         0         0         0      00:00:00.000000      00:00:00.000000      00:00:00.000000
        RP          0         0         0         0         0      00:00:00.000000      00:00:00.000000      00:00:00.000000
        FO          1         1         0         0         0      00:00:00.000000      00:00:00.000000      00:00:00.000000
        SL          1         1         0         0         0      00:00:00.000000      00:00:00.000000      00:00:00.000000
        SO          1         1         0         0         0      00:00:00.000000      00:00:00.000000      00:00:00.000000
        SP          1         1         0         0         0      00:00:00.000000      00:00:00.000000      00:00:00.000000
        EP          2         2         0         0         0      00:00:00.000000      00:00:00.000000      00:00:00.000000
        TP          0         0         0         0         0      00:00:00.000000      00:00:00.000000      00:00:00.000000
        D2          1         1         0         0        10      00:05:00.220009      00:00:00.231101      00:00:00.005096
        S8          0         0         0         0         0      00:00:00.000000      00:00:00.000000      00:00:00.000000
        L8         51        51         0         0     83123      01:45:50.602125      00:18:41.045653      00:02:23.266390
        L9          0         0         0         0         0      00:00:00.000000      00:00:00.000000      00:00:00.000000
        X8          0         0         0         0         0      00:00:00.000000      00:00:00.000000      00:00:00.000000
        X9          0         0         0         0         0      00:00:00.000000      00:00:00.000000      00:00:00.000000
        T8          0         0         0         0         0      00:00:00.000000      00:00:00.000000      00:00:00.000000
    +  _____________________________________________________________________________________________________________________
       Totals      60
    '''
    if "Accumulated          Accumulated" in line or "Accumulated          Accumulated".upper() in line:                                            
        CQRDispInfo = LineList[index + 3].split(" ")                        #QR dispatch information
        CQRDispInfo = [x for x in CQRDispInfo if x != '']
        CQRDisp = CQRDispInfo[-2]
        CQRCPU  = CQRDispInfo[-1].strip("\r\n")

        HHMMSS = CQRDisp.split(":")
        HH=HHMMSS[0]
        MM=HHMMSS[1]
        SS=HHMMSS[2]
        CQRDispV = float(HH)*3600 + float(MM)*60 + float(SS)

        HHMMSS = CQRCPU.split(":")
        HH=HHMMSS[0]
        MM=HHMMSS[1]
        SS=HHMMSS[2]
        CQRCPUV = float(HH)*3600 + float(MM)*60 + float(SS)

        if CQRDispV > 0:
            CQRUtilRatio = '%.2f'%(float(CQRCPUV)/float(CIntervalV)*100)
            CQRDispRatio = '%.2f'%(float(CQRCPUV)/float(CQRDispV)*100)
            CQRDispUtilRatio = '%.2f'%(float(CQRDispV)/float(CIntervalV)*100)
            if float(CQRDispRatio) > 100.00:  #this could happen when both Disp and CPU are very small
                CQRDispRatio = 100.00

            IntItem.DSItem.QRDsp = CQRDisp
            IntItem.DSItem.QRCPU = CQRCPU
            IntItem.DSItem.QRCPUUtilRatio = CQRUtilRatio
            IntItem.DSItem.QRDispUtilRatio = CQRDispUtilRatio
            IntItem.DSItem.QRDispRatio = CQRDispRatio

        modeindex = 1
        while (modeindex <= TCBModeNum):
            if "L8" in LineList[index+2+modeindex]:
                CL8DispInfo = LineList[index + 2+modeindex].split(" ")                        #L8 dispatch information
                CL8DispInfo = [x for x in CL8DispInfo if x != '']
                CL8Disp = CL8DispInfo[-2]
                CL8CPU  = CL8DispInfo[-1].strip("\r\n")

                HHMMSS = CL8Disp.split(":")
                HH=HHMMSS[0]
                MM=HHMMSS[1]
                SS=HHMMSS[2]
                CL8DispV = float(HH)*3600 + float(MM)*60 + float(SS)

                HHMMSS = CL8CPU.split(":")
                HH=HHMMSS[0]
                MM=HHMMSS[1]
                SS=HHMMSS[2]
                CL8CPUV = float(HH)*3600 + float(MM)*60 + float(SS)

                if CL8DispV > 0:
                    CL8UtilRatio = '%.2f'%(float(CL8CPUV)/float(CIntervalV)*100)
                    CL8DispRatio = '%.2f'%(float(CL8CPUV)/float(CL8DispV)*100)
                    CL8DispUtilRatio = '%.2f'%(float(CL8DispV)/float(CIntervalV)*100)
                    if float(CL8DispRatio) > 100.00:  #this could happen when both Disp and CPU are very small
                        CL8DispRatio = 100.00

                    IntItem.DSItem.L8Dsp = CL8Disp
                    IntItem.DSItem.L8CPU = CL8CPU
                    IntItem.DSItem.L8CPUUtilRatio = CL8UtilRatio
                    IntItem.DSItem.L8DispUtilRatio = CL8DispUtilRatio
                    IntItem.DSItem.L8DispRatio = CL8DispRatio                
                break
            modeindex = modeindex + 1
                         
        index = index + TCBModeNum + 2                                                  #Skip Dispatcher TCB Statistics
        
    '''
    CICS 680 and below:
    +___________________________________________________________________________________________________________________________________
    0DISPATCHER STATISTICS
    +_____________________
    -  CICS TCB Pool Statistics
    +  ________________________
    0  TCB Pool. . . . . . . . . . . . . . . . . . . . :          OPEN
    0  Current TCBs attached in this TCB Pool. . . . . :            47  Current TCBs in use in this TCB Pool. . . . . . :             8
       Peak TCBs attached in this TCB Pool . . . . . . :            47  Peak TCBs in use in this TCB Pool . . . . . . . :            32
    0  Max TCB Pool limit (MAXOPENTCBS). . . . . . . . :           332  Times at Max TCB Pool Limit (MAXOPENTCBS) . . . :             0
    0  Total Requests delayed by Max TCB Pool Limit. . :             0  Total Number of TCB Mismatch waits. . . . . . . :             0
       Total Max TCB Pool Limit delay time . . . . . . : 00:00:00.0000  Total TCB Mismatch wait time. . . . . . . . . . : 00:00:00.0000
    0  Current Requests delayed by Max TCB Pool Limit. :             0  Current TCB Mismatch waits. . . . . . . . . . . :             0
       Current Max TCB Pool Limit delay time . . . . . : 00:00:00.0000  Current TCB Mismatch wait time. . . . . . . . . : 00:00:00.0000
       Peak Requests delayed by Max TCB Pool Limit . . :             0  Peak TCB Mismatch waits . . . . . . . . . . . . :             0
    0                                                                   Requests Delayed by MVS storage constraint. . . :             0
                                                                        Total MVS storage constraint delay time . . . . : 00:00:00.0000
    
    CICS 690:
    +___________________________________________________________________________________________________________________________________
    0DISPATCHER STATISTICS
    +_____________________
    -  CICS TCB Pool Statistics
    +  ________________________
    0  TCB Pool. . . . . . . . . . . . . . . . . . . . :          OPEN
    0  Current TCBs attached in this TCB Pool. . . . . :             4  Current TCBs in use in this TCB Pool. . . . . . :             1
       Peak TCBs attached in this TCB Pool . . . . . . :             4  Peak TCBs in use in this TCB Pool . . . . . . . :             3
    0  Max TCB Pool limit (MAXOPENTCBS). . . . . . . . :           180  Times at Max TCB Pool Limit (MAXOPENTCBS) . . . :             0
    0  Time Max TCB Pool Limit last reached. . . . . . : --:--:--.----
       Total Requests delayed by Max TCB Pool Limit. . :             0  Total Number of TCB Mismatch waits. . . . . . . :             0
       Total Max TCB Pool Limit delay time . . . . . . : 00:00:00.0000  Total TCB Mismatch wait time. . . . . . . . . . : 00:00:00.0000
    0  Current Requests delayed by Max TCB Pool Limit. :             0  Current TCB Mismatch waits. . . . . . . . . . . :             0
       Current Max TCB Pool Limit delay time . . . . . : 00:00:00.0000  Current TCB Mismatch wait time. . . . . . . . . : 00:00:00.0000
       Peak Requests delayed by Max TCB Pool Limit . . :             0  Peak TCB Mismatch waits . . . . . . . . . . . . :             0
    0                                                                   Requests Delayed by MVS storage constraint. . . :             0
                                                                        Total MVS storage constraint delay time . . . . : 00:00:00.0000
    '''

    if ("CICS TCB Pool Statistics" in line or "CICS TCB Pool Statistics".upper() in line)  and "OPEN" in LineList[index + 2]:                                            
        CPeakOpenTCB = LineList[index + 4].split(" ")                        #Peak Open TCB attached
        CPeakOpenTCB = [x for x in CPeakOpenTCB if x != '' and x != '.']
        CPeakOpenTCB = CPeakOpenTCB[8]     

        CMAXOpenTCBs = LineList[index + 5].split(" ")                        #MAXOPENTCBS
        CMAXOpenTCBs = [x for x in CMAXOpenTCBs if x != '' and x != '.']
        CMAXOpenTCBs = CMAXOpenTCBs[7]      

        COpenTCBUtilRatio = '%.2f'% (float(CPeakOpenTCB)/float(CMAXOpenTCBs)*100)
        
        IntItem.DSItem.PkOpTCB = CPeakOpenTCB
        IntItem.DSItem.MAXOpTCB = CMAXOpenTCBs
        IntItem.DSItem.OpTCBRatio = COpenTCBUtilRatio
        DSRep.AppendItem(IntItem.DSItem)

        index = index + 12                                                  #Skip Open TCB Pool Statistics

    '''
    CICS 680 and below:
    +___________________________________________________________________________________________________________________________________
    0ENQUEUE STATISTICS
    +__________________

    0    ENQ      ENQs     ENQs      Enqueue    Sysplex   Sysplex      ENQs      Enqueue   Immediate-rejection <---Waiting rejection---->
       Poolname  Issued   Waited  Waiting time  Waited  Waiting time Retained   Retention   Enqbusy  Retained  Retained Operator Timeout
    +  __________________________________________________________________________________________________________________________________

       CICSMQ          0        0 00:00:00.000        0 00:00:00.000        0 000-00:00:00        0         0         0        0        0
       DISPATCH        0        0 00:00:00.000        0 00:00:00.000        0 000-00:00:00        0         0         0        0        0
       EPADDR          0        0 00:00:00.000        0 00:00:00.000        0 000-00:00:00        0         0         0        0        0
       EXECADDR        0        0 00:00:00.000        0 00:00:00.000        0 000-00:00:00        0         0         0        0        0
       EXECPLEX        0        0 00:00:00.000        0 00:00:00.000        0 000-00:00:00        0         0         0        0        0
       EXECSTRN       10        0 00:00:00.000        0 00:00:00.000        0 000-00:00:00        0         0         0        0        0
    +  __________________________________________________________________________________________________________________________________
       *TOTALS*       43        0 00:00:00.000        0 00:00:00.000        0 000-00:00:00        0         0         0        0        0    

    CICS 690 (Bug???):
    +___________________________________________________________________________________________________________________________________
    0ENQUEUE STATISTICS
    +__________________

    0    ENQ      ENQs     ENQs      Enqueue    Sysplex   Sysplex      ENQs      Enqueue   Immediate-rejection <---Waiting rejection----
       Poolname  Issued   Waited  Waiting time  Waited  Waiting time Retained   Retention   Enqbusy  Retained  Retained Operator Timeout
    +  _________________________________________________________________________________________________________________________________

       CICSMQ          0        0 00:00:00.000        0 00:00:00.000        0 000-00:00:00        0         0         0        0
       DISPATCH        0        0 00:00:00.000        0 00:00:00.000        0 000-00:00:00        0         0         0        0
       EPADDR          0        0 00:00:00.000        0 00:00:00.000        0 000-00:00:00        0         0         0        0
       EXECADDR        0        0 00:00:00.000        0 00:00:00.000        0 000-00:00:00        0         0         0        0
       EXECPLEX        0        0 00:00:00.000        0 00:00:00.000        0 000-00:00:00        0         0         0        0
       EXECSTRN       90        0 00:00:00.000        0 00:00:00.000        0 000-00:00:00        0         0         0        0
    +  _________________________________________________________________________________________________________________________________
       *TOTALS*      248        0 00:00:00.000        0 00:00:00.000        0 000-00:00:00        0         0         0        0
    ''' 

    if "0ENQUEUE STATISTICS" in line.upper():
        NQFlag = 1
        while ("*TOTALS*" not in LineList[index]):
            if ENQNAME != "*TOTALS*" and (ENQNAME in LineList[index]) :
                CENQEntry = LineList[index].split(" ")             #Extract ENQ Total summary
                CENQEntry = [x for x in CENQEntry if x != '']
                CENQIssued  = CENQEntry[1]                         #ENQs Issued
                CENQWaited = CENQEntry[2]                          #ENQs Waited
                CENQWTT = CENQEntry[3]                             #Enqueue waiting time
                CENQSysWaited = CENQEntry[4]                       #Sysplex Waited
                CENQSysWTT  = CENQEntry[5]                         #Sysplex Waiting time
                CENQRetained = CENQEntry[6]                        #ENQs Retained
                CENQRetention = CENQEntry[7]                       #Enqueue Retention
                CENQImmRejBusy = CENQEntry[8]                      #Immediate-rejection Enqbusy
                CENQImmRejRetained = CENQEntry[9]                  #Immediate-rejection Retained
                CENQWtRejRetained = CENQEntry[10]                  #Waiting rejection Retained
                CENQWtOperator = CENQEntry[11]                     #Waiting rejection Operator
                CENQWtTimeout = CENQEntry[12].strip("\r\n")          #Waiting rejection Timeout
                break
            index = index + 1

        if "*TOTALS*" in LineList[index]:                        #Default to TOTALS if not found
            ENQNAME = "*TOTALS*"
            CENQEntry = LineList[index].split(" ")             #Extract ENQ Total summary
            CENQEntry = [x for x in CENQEntry if x != '']
            CENQIssued  = CENQEntry[1]                         #ENQs Issued
            CENQWaited = CENQEntry[2]                          #ENQs Waited
            CENQWTT = CENQEntry[3]                             #Enqueue waiting time
            CENQSysWaited = CENQEntry[4]                       #Sysplex Waited
            CENQSysWTT  = CENQEntry[5]                         #Sysplex Waiting time
            CENQRetained = CENQEntry[6]                        #ENQs Retained
            CENQRetention = CENQEntry[7]                       #Enqueue Retention
            CENQImmRejBusy = CENQEntry[8]                      #Immediate-rejection Enqbusy
            CENQImmRejRetained = CENQEntry[9]                  #Immediate-rejection Retained
            CENQWtRejRetained = CENQEntry[10]                  #Waiting rejection Retained
            CENQWtOperator = CENQEntry[11]                     #Waiting rejection Operator
            if CICSVERSION == "6.9.0": # bug!!!
                CENQWtTimeout = '0'
            else:
                CENQWtTimeout = CENQEntry[12].strip("\r\n")        #Waiting rejection Timeout
        
        if float(CENQIssued) > 0:
            ENQBusyRatio='%.2f'%(float(CENQImmRejBusy)/float(CENQIssued)*100)
            ENQImmRetRatio='%.2f'%(float(CENQImmRejRetained)/float(CENQIssued)*100)
            ENQWttRatio='%.2f'%(float(CENQWaited)/float(CENQIssued)*100)
            ENQRetRatio='%.2f'%(float(CENQRetained)/float(CENQIssued)*100)

            ENQImmSuccessRatio = 100.00 - float(ENQBusyRatio) - float(ENQWttRatio) - float(ENQRetRatio) - float(ENQImmRetRatio)
        else:
            ENQBusyRatio = 0.00
            ENQWttRatio= 0.00
            ENQRetRatio= 0.00
            ENQImmRetRatio = 0.00
            ENQImmSuccessRatio = 0.00

        IntItem.NQItem.ENQPoolName = ENQNAME
        IntItem.NQItem.ENQIssd = StringToInt(CENQIssued)
        IntItem.NQItem.ENQWtd = StringToInt(CENQWaited)
        IntItem.NQItem.ENQWTT = CENQWTT
        IntItem.NQItem.ENQSysWtd = StringToInt(CENQSysWaited)
        IntItem.NQItem.ENQSysWtT = CENQSysWTT
        IntItem.NQItem.ENQRtnd = StringToInt(CENQRetained)
        IntItem.NQItem.ENQRtnT = CENQRetention
        IntItem.NQItem.ImmRejEqBsy = StringToInt(CENQImmRejBusy)
        IntItem.NQItem.ImmRejEqRtn = StringToInt(CENQImmRejRetained)
        IntItem.NQItem.WtRejRtn = StringToInt(CENQWtRejRetained)
        IntItem.NQItem.WtRejOp = StringToInt(CENQWtOperator)
        IntItem.NQItem.WtRejTo = StringToInt(CENQWtTimeout)
        IntItem.NQItem.ENQBusyRatio = float(ENQBusyRatio)
        IntItem.NQItem.ENQWttRatio = float(ENQWttRatio)
        IntItem.NQItem.ENQRetRatio = float(ENQRetRatio)
        IntItem.NQItem.ENQImmRetRatio = float(ENQImmRetRatio)        
        IntItem.NQItem.ENQImmSuccessRatio = float(ENQImmSuccessRatio)        
        NQRep.AppendItem(IntItem.NQItem)        


    '''
    +___________________________________________________________________________________________________________________________________
    0TEMPORARY STORAGE
    +_________________
    0  Put/Putq main storage requests         :              352
       Get/Getq main storage requests         :              352
    0  TSMAINLIMIT Setting                    :         67108864
       Times at TSMAINLIMIT                   :                0
    0  Current storage used for TSMAINLIMIT   :                4
       Peak storage used for TSMAINLIMIT      :               55
       Number of TS queues auto-deleted       :                0
       Number of times cleanup task has run   :                4
    0  Put/Putq auxiliary storage requests    :            49764
       Get/Getq auxiliary storage requests    :            49764
    0  Peak temporary storage names in use    :                6
       Current temporary storage names in use :                2
       Number of entries in longest queue     :                1
       Times queues created                   :            50116
    0  Control interval size                  :             4096
       Available bytes per control interval   :             4032
       Segments per control interval          :               63
       Bytes per segment                      :               64
       Writes more than control interval      :               50
       Longest auxiliary temp storage record  :             6817
    0  Number of control intervals available  :            53999
       Peak control intervals in use          :                4
       Current control intervals in use       :                3
       Times aux. storage exhausted           :                0
       Number of temp storage compressions    :               74
    0  Temporary storage buffers              :               45
       Buffer waits                           :                0
       Peak users waiting on buffer           :                0
       Current users waiting on buffer        :                0
       Buffer writes                          :                0
       Forced writes for recovery             :                0
       Buffer reads                           :                0
       Format writes                          :                0
    0  Temporary storage strings              :               20
       Peak number of strings in use          :                0
       Times string wait occurred             :                0
       Peak number of users waiting on string :                0
       Current users waiting on string        :                0
    0  I/O errors on TS dataset               :                0
       Shared pools defined                   :                1
       Shared pools currently connected       :                0
       Shared read requests                   :                0
    0  Shared write requests                  :                0
    '''       
    if "0TEMPORARY STORAGE" in line.upper():
        TSQFlag = 1
        CPutqMainReqs = LineList[index+2].split(" ")  
        CPutqMainReqs = [x for x in CPutqMainReqs if x != '']
        CPutqMainReqs = CPutqMainReqs[6].strip("\r\n")

        CGetqMainReqs = LineList[index+3].split(" ")  
        CGetqMainReqs = [x for x in CGetqMainReqs if x != '']
        CGetqMainReqs = CGetqMainReqs[5].strip("\r\n")

        CTSMAINLIMIT = LineList[index+4].split(" ")  
        CTSMAINLIMIT = [x for x in CTSMAINLIMIT if x != '']
        CTSMAINLIMIT = CTSMAINLIMIT[4].strip("\r\n")

        CTSMAINLIMITTms = LineList[index+5].split(" ")  
        CTSMAINLIMITTms = [x for x in CTSMAINLIMITTms if x != '']
        CTSMAINLIMITTms = CTSMAINLIMITTms[4].strip("\r\n")

        CPeakUsedTSMAIN = LineList[index+7].split(" ")  
        CPeakUsedTSMAIN = [x for x in CPeakUsedTSMAIN if x != '']
        CPeakUsedTSMAIN = CPeakUsedTSMAIN[6].strip("\r\n")

        CTSMAINLIMITRatio = '%.4f'%(float(CPeakUsedTSMAIN)/float(CTSMAINLIMIT)*100)

        CAutoDelTSQs = LineList[index+8].split(" ")  
        CAutoDelTSQs = [x for x in CAutoDelTSQs if x != '']
        CAutoDelTSQs = CAutoDelTSQs[6].strip("\r\n")

        CPutqAuxReqs = LineList[index+10].split(" ")  
        CPutqAuxReqs = [x for x in CPutqAuxReqs if x != '']
        CPutqAuxReqs = CPutqAuxReqs[6].strip("\r\n")

        CGetqAuxReqs = LineList[index+11].split(" ")  
        CGetqAuxReqs = [x for x in CGetqAuxReqs if x != '']
        CGetqAuxReqs = CGetqAuxReqs[5].strip("\r\n")        

        CQueueCreTms = LineList[index+15].split(" ")  
        CQueueCreTms = [x for x in CQueueCreTms if x != '']
        CQueueCreTms = CQueueCreTms[4].strip("\r\n")  

        CCISize = LineList[index+16].split(" ")  
        CCISize = [x for x in CCISize if x != '']
        CCISize = CCISize[5].strip("\r\n")

        CWriteMTCI = LineList[index+20].split(" ")  
        CWriteMTCI = [x for x in CWriteMTCI if x != '']
        CWriteMTCI = CWriteMTCI[6].strip("\r\n") 

        CLongestRec = LineList[index+21].split(" ")  
        CLongestRec = [x for x in CLongestRec if x != '']
        CLongestRec = CLongestRec[6].strip("\r\n")

        CAuxStgExh = LineList[index+25].split(" ")  
        CAuxStgExh = [x for x in CAuxStgExh if x != '']
        CAuxStgExh = CAuxStgExh[5].strip("\r\n") 

        CTSCompress = LineList[index+26].split(" ")  
        CTSCompress = [x for x in CTSCompress if x != '']
        CTSCompress = CTSCompress[6].strip("\r\n") 

        CBufferWait = LineList[index+28].split(" ")  
        CBufferWait = [x for x in CBufferWait if x != '']
        CBufferWait = CBufferWait[3].strip("\r\n") 

        CStringWait = LineList[index+37].split(" ")  
        CStringWait = [x for x in CStringWait if x != '']
        CStringWait = CStringWait[5].strip("\r\n")         

        CIOErrorTSQDS = LineList[index+40].split(" ")  
        CIOErrorTSQDS = [x for x in CIOErrorTSQDS if x != '']
        CIOErrorTSQDS = CIOErrorTSQDS[7].strip("\r\n")          

        CGetqShareReqs = LineList[index+43].split(" ")  
        CGetqShareReqs = [x for x in CGetqShareReqs if x != '']
        CGetqShareReqs = CGetqShareReqs[4].strip("\r\n") 

        CPutqShareReqs = LineList[index+44].split(" ")  
        CPutqShareReqs = [x for x in CPutqShareReqs if x != '']
        CPutqShareReqs = CPutqShareReqs[5].strip("\r\n") 

        IntItem.TSQItem.PutqMainReqs = StringToInt(CPutqMainReqs)
        IntItem.TSQItem.GetqMainReqs = StringToInt(CGetqMainReqs)
        IntItem.TSQItem.TSMAINLIMIT = StringToInt(CTSMAINLIMIT)
        IntItem.TSQItem.TSMAINLIMITTms = StringToInt(CTSMAINLIMITTms)
        IntItem.TSQItem.PeakUsedTSMAIN = StringToInt(CPeakUsedTSMAIN)
        IntItem.TSQItem.TSMAINLIMITRatio = CTSMAINLIMITRatio
        IntItem.TSQItem.AutoDelTSQs = StringToInt(CAutoDelTSQs)
        IntItem.TSQItem.PutqAuxReqs = StringToInt(CPutqAuxReqs)
        IntItem.TSQItem.GetqAuxReqs = StringToInt(CGetqAuxReqs)
        IntItem.TSQItem.QueueCreTms = StringToInt(CQueueCreTms)
        IntItem.TSQItem.CISize = StringToInt(CCISize)
        IntItem.TSQItem.WriteMTCI = StringToInt(CWriteMTCI)
        IntItem.TSQItem.LongestRec = StringToInt(CLongestRec)
        IntItem.TSQItem.AuxStgExh = StringToInt(CAuxStgExh)
        IntItem.TSQItem.TSCompress = StringToInt(CTSCompress)
        IntItem.TSQItem.BufferWait = StringToInt(CBufferWait)
        IntItem.TSQItem.StringWait = StringToInt(CStringWait)
        IntItem.TSQItem.IOErrorTSQDS = StringToInt(CIOErrorTSQDS)
        IntItem.TSQItem.GetqShareReqs = StringToInt(CGetqShareReqs)
        IntItem.TSQItem.PutqShareReqs = StringToInt(CPutqShareReqs)

        TSQRep.AppendItem(IntItem.TSQItem)

    '''
    +___________________________________________________________________________________________________________________________________
    0TRANSIENT DATA
    +______________

       Control interval size                     :    32768
       Control intervals                         :    22000
       Current control interval in use           :        2
       Peak control intervals used               :        6
    0  Times NOSPACE occurred                    :        0
    0  Writes to intrapartition dataset          :        0
       Reads from intrapartition dataset         :        0
       Formatting writes                         :        0
       I/O errors                                :        0
    0  Intrapartition buffers                    :     3000
       Current buffers containing valid data     :        1
       Peak intra. buffers containing valid data :        5
       Intrapartition accesses                   :   254880
       Current concurrent buffer accesses        :        0
       Peak concurrent intrapartition accesses   :        1
       Intrapartition buffer waits               :        0
       Current intrapartition buffer waits       :        0
       Intrapartition buffer waits          :        0
    0  Number of strings                         :      255
       Times string accessed                     :        0
       Current concurrent string accesses        :        0
       Peak concurrent string accesses           :        0
       Intrapartition string waits               :        0
       Current intrapartition string waits       :        0
       String waits                         :        0
    '''
    if "0TRANSIENT DATA" in line.upper() and "CONTROL INTERVAL SIZE" in LineList[index+3].upper():
        TDQFlag = 1
        CNOSPACETms = LineList[index+7].split(" ")  
        CNOSPACETms = [x for x in CNOSPACETms if x != '']
        CNOSPACETms = CNOSPACETms[5].strip("\r\n") 

        CPutqIntraReqs = LineList[index+8].split(" ")  
        CPutqIntraReqs = [x for x in CPutqIntraReqs if x != '']
        CPutqIntraReqs = CPutqIntraReqs[6].strip("\r\n") 

        CGetqIntraReqs = LineList[index+9].split(" ")  
        CGetqIntraReqs = [x for x in CGetqIntraReqs if x != '']
        CGetqIntraReqs = CGetqIntraReqs[5].strip("\r\n") 

        CIOErrors = LineList[index+11].split(" ")  
        CIOErrors = [x for x in CIOErrors if x != '']
        CIOErrors = CIOErrors[3].strip("\r\n") 

        CIntraBuffers = LineList[index+12].split(" ")  
        CIntraBuffers = [x for x in CIntraBuffers if x != '']
        CIntraBuffers = CIntraBuffers[4].strip("\r\n") 

        CPeakIntraBuffData = LineList[index+14].split(" ")  
        CPeakIntraBuffData = [x for x in CPeakIntraBuffData if x != '']
        CPeakIntraBuffData = CPeakIntraBuffData[7].strip("\r\n") 

        CBufferWait = LineList[index+18].split(" ")  
        CBufferWait = [x for x in CBufferWait if x != '']
        CBufferWait = CBufferWait[4].strip("\r\n") 

        CStringNum = LineList[index+21].split(" ")  
        CStringNum = [x for x in CStringNum if x != '']
        CStringNum = CStringNum[5].strip("\r\n") 


        CStringWait = LineList[index+25].split(" ")  
        CStringWait = [x for x in CStringWait if x != '']
        CStringWait = CStringWait[4].strip("\r\n") 

        IntItem.TDQItem.PutqIntraReqs = StringToInt(CPutqIntraReqs)
        IntItem.TDQItem.GetqIntraReqs = StringToInt(CGetqIntraReqs)
        IntItem.TDQItem.IOErrors = StringToInt(CIOErrors)
        IntItem.TDQItem.BufferWait = StringToInt(CBufferWait) 
        IntItem.TDQItem.StringWait = StringToInt(CStringWait)
        IntItem.TDQItem.NOSPACETms = StringToInt(CNOSPACETms)
        IntItem.TDQItem.IntraBuffers = StringToInt(CIntraBuffers)
        IntItem.TDQItem.PeakIntraBuffData = StringToInt(CPeakIntraBuffData)
        IntItem.TDQItem.StringNum = StringToInt(CStringNum)

        TDQRep.AppendItem(IntItem.TDQItem)

    '''
    CICS 680 and below:
    0STORAGE MANAGER STATISTICS
    +__________________________
    0  Global Statistics
    +  _________________
    0  Storage protection . . . . . . . . :         ACTIVE
       Transaction isolation. . . . . . . :       INACTIVE
       Reentrant programs . . . . . . . . :        PROTECT
    0  Current DSA limit. . . . . . . . . :          5120K    MVS storage request waits. . . . . . . . . . . . . :                     0
       Current DSA total. . . . . . . . . :          1536K    Total time waiting for MVS storage . . . . . . . . :         00:00:00.0000
       Peak DSA total . . . . . . . . . . :          1536K
    0  Current EDSA limit . . . . . . . . :          1280M
       Current EDSA total . . . . . . . . :          1278M
       Peak EDSA total. . . . . . . . . . :          1278M
    0  MEMLIMIT size. . . . . . . . . . . :       NOLIMIT     IARV64 GETSTOR request size. . . . . . . . . . . . :                 1024M
       MEMLIMIT set by. . . . . . . . . . :         REGION
                                                              IARV64 FROMGUARD Failures. . . . . . . . . . . . . :                     0
       Current Address Space active . . . :          1050M    IARV64 FROMGUARD Failure size. . . . . . . . . . . :                     0
       Peak Address Space active. . . . . :          1050M
                                                              Private Memory Objects . . . . . . . . . . . . . . :                    23
       Current GDSA allocated . . . . . . :          1024M    Bytes allocated to Private Memory Objects. . . . . :         2,169,503,744
       Peak GDSA allocated. . . . . . . . :          1024M    Bytes hidden within Private Memory Objects . . . . :         1,068,498,944
                                                              Peak bytes usable within Private Memory Objects. . :         1,105,199,104
       Current GDSA active. . . . . . . . :          1023M
       Peak GDSA active . . . . . . . . . :          1023M    Shared Memory Objects. . . . . . . . . . . . . . . :                     0
                                                              Bytes allocated to Shared Memory Objects . . . . . :                     0
                                                              Peak bytes usable within Shared Memory Objects . . :                     0
    0                                                         Auxiliary slots backing Private Memory Objects . . :                     0
                                                              HWM Auxiliary slots backing Private Memory Objects :                     0
    0                                                         Real Frames backing Private Memory Objects . . . . :                 4,665
                                                              HWM Real Frames backing Private Memory Objects . . :                 5,689
    0                                                         Number of Large Memory Objects Allocated . . . . . :                     0
                                                              Number of Large Pages backed in Real Storage . . . :                     0
    '''

    if "0STORAGE MANAGER STATISTICS" in line.upper():
        SMFlag = 1
        CStgProt = LineList[index+4].split(" ")
        CStgProt = [x for x in CStgProt if x != '' and x != '.']
        CStgProt = CStgProt[4].strip("\r\n")

        CStgIso = LineList[index+5].split(" ") 
        CStgIso = [x for x in CStgIso if x != '' and x != '.']
        CStgIso = CStgIso[3].strip("\r\n")

        CReentPg = LineList[index+6].split(" ")
        CReentPg = [x for x in CReentPg if x != '' and x != '.']
        CReentPg = CReentPg[3].strip("\r\n")
        
        CDSALIM = LineList[index+7].split(" ") 
        CDSALIM = [x for x in CDSALIM if x != '' and x != '.']
        CDSALIM = CDSALIM[5].strip("\r\n")

        CPeakDSA = LineList[index + 9].split(" ") 
        CPeakDSA = [x for x in CPeakDSA if x != '' and x != '.']
        CPeakDSA = CPeakDSA[4].strip("\r\n")
        
        CEDSALIM = LineList[index + 10].split(" ") 
        CEDSALIM = [x for x in CEDSALIM if x != '' and x !='.']
        CEDSALIM = CEDSALIM[5].strip("\r\n")
        
        CPeakEDSA = LineList[index + 12].split(" ") 
        CPeakEDSA = [x for x in CPeakEDSA if x != '' and x !='.']
        CPeakEDSA = CPeakEDSA[4].strip("\r\n")

        CMemLim = LineList[index + 13].split(" ") 
        CMemLim = [x for x in CMemLim if x != '' and x !='.']
        CMemLim = CMemLim[4].strip("\r\n").replace(",","")


        IntItem.SMItem.STGPROT = CStgProt
        IntItem.SMItem.TRANISO = CStgIso
        IntItem.SMItem.REENTPG = CReentPg
        IntItem.SMItem.DSALIM = StringToInt(CDSALIM)
        IntItem.SMItem.PeakDSA = StringToInt(CPeakDSA)
        IntItem.SMItem.EDSALIM = StringToInt(CEDSALIM)
        IntItem.SMItem.PeakEDSA = StringToInt(CPeakEDSA)
        IntItem.SMItem.MEMLIMIT = CMemLim

        index = index + 31    #Skip Storage Manager Statistics
        
    '''
    +___________________________________________________________________________________________________________________________________
    0  Dynamic Storage Areas (above 16M)
    +  _________________________________
    0                                          ECDSA          EUDSA          ESDSA          ERDSA          ETDSA
    +                                ___________________________________________________________________________
    0  Current DSA size. . . . . . :         208896K       1041408K         21504K         35840K          1024K
       Peak DSA Size . . . . . . . :         208896K       1041408K         21504K         35840K          1024K
       Cushion Size. . . . . . . . :            128K             0K           128K           256K           128K
    0  Free storage (inc. cushion) :          15152K        915072K          1088K          1288K           992K
       Percentage free storage . . :              7%            87%             5%             3%            96%
       Peak free storage . . . . . :          15284K       1037824K          1088K          1288K           992K
       Lowest free storage . . . . :          14628K        480128K          1088K          1288K           992K
       Largest free area . . . . . :           5120K         21504K           572K           748K           992K
    0  Getmain Requests. . . . . . :         1684313         284611          18080              0          72320
       Freemain Requests . . . . . :         1684310         284608          18080              0          72320
    0  Current no of Subpools. . . :             393             31             14              4              6
       Add Subpool Requests. . . . :           36223          36223              0              0              0
       Delete Subpool Requests . . :           36223          36223              0              0              0
    0  Times no storage returned . :               0              0              0              0              0
       Times request suspended . . :               0              0              0              0              0
    0  Current suspended . . . . . :               0              0              0              0              0
       Peak requests suspended . . :               0              0              0              0              0
       Purged while waiting. . . . :               0              0              0              0              0
    0  Times cushion released. . . :               0              0              0              0              0
       Times went short on storage :               0              0              0              0              0
       Total time SOS. . . . . . . :   00:00:00.0000  00:00:00.0000  00:00:00.0000  00:00:00.0000  00:00:00.0000
       Average time SOS. . . . . . :   00:00:00.0000  00:00:00.0000  00:00:00.0000  00:00:00.0000  00:00:00.0000
    0  Storage violations. . . . . :               0              0              0              0              0
       Access. . . . . . . . . . . :            CICS           USER           USER       READONLY        TRUSTED
    0  Current extents . . . . . . :              43             62             21             22              1
       Extents added . . . . . . . :               0              0              0              0              0
       Extents released. . . . . . :               0              0              0              0              0
    '''
    if "DYNAMIC STORAGE AREAS (BELOW 16M)" in line.upper():
        C4PARTOFDSA = LineList[index + 5].split(" ")
        C4PARTOFDSA = [x for x in C4PARTOFDSA if x != '' and x !='.']
        CCDSA = C4PARTOFDSA[4].strip("\r\n")
        CUDSA = C4PARTOFDSA[5].strip("\r\n")
        CSDSA = C4PARTOFDSA[6].strip("\r\n")
        CRDSA = C4PARTOFDSA[7].strip("\r\n")

        CFREESTG = LineList[index + 7].split(" ")
        CFREESTG = [x for x in CFREESTG if x != '' and x !='.']
        CFSCDSA = CFREESTG[6].strip("\r\n")
        CFSUDSA = CFREESTG[7].strip("\r\n")
        CFSSDSA = CFREESTG[8].strip("\r\n")
        CFSRDSA = CFREESTG[9].strip("\r\n")
        

        CLOWFREESTG = LineList[index + 10].split(" ")
        CLOWFREESTG=[x for x in CLOWFREESTG if x != '' and x !='.']        
        CLFSCDSA=CLOWFREESTG[4].strip("\r\n")
        CLFSUDSA=CLOWFREESTG[5].strip("\r\n")
        CLFSSDSA=CLOWFREESTG[6].strip("\r\n")
        CLFSRDSA=CLOWFREESTG[7].strip("\r\n")


        CLRGFREEAREA = LineList[index + 11].split(" ")
        CLRGFREEAREA=[x for x in CLRGFREEAREA if x != '' and x !='.']        
        CLFACDSA=CLRGFREEAREA[4].strip("\r\n")
        CLFAUDSA=CLRGFREEAREA[5].strip("\r\n")
        CLFASDSA=CLRGFREEAREA[6].strip("\r\n")
        CLFARDSA=CLRGFREEAREA[7].strip("\r\n")

        CDSASOSTMS = LineList[index + 23].split(" ")
        CDSASOSTMS = [x for x in CDSASOSTMS if x != '' and x !='.']
        CDSASOSTMS = StringToInt(CDSASOSTMS[6].strip("\r\n")) + StringToInt(CDSASOSTMS[7].strip("\r\n")) \
                     + StringToInt(CDSASOSTMS[8].strip("\r\n")) + StringToInt(CDSASOSTMS[9].strip("\r\n")) 

        CDSASTGVIOLTMS = LineList[index + 26].split(" ")
        CDSASTGVIOLTMS = [x for x in CDSASTGVIOLTMS if x != '' and x !='.'] 
        CDSASTGVIOLTMS = StringToInt(CDSASTGVIOLTMS[4].strip("\r\n")) + StringToInt(CDSASTGVIOLTMS[5].strip("\r\n")) \
                     + StringToInt(CDSASTGVIOLTMS[6].strip("\r\n")) + StringToInt(CDSASTGVIOLTMS[7].strip("\r\n")) 
 
        if StringToInt(CFSCDSA) > 0:
            CFRAGRATIOCDSA = '%.2f'%(float(StringToInt(CLFACDSA))/float(StringToInt(CFSCDSA)))
        else:
            CFRAGRATIOCDSA = 1.00

        if StringToInt(CFSUDSA) > 0:
            CFRAGRATIOUDSA = '%.2f'%(float(StringToInt(CLFAUDSA))/float(StringToInt(CFSUDSA))) 
        else:
            CFRAGRATIOUDSA = 1.00      

        if StringToInt(CFSSDSA) > 0:
            CFRAGRATIOSDSA = '%.2f'%(float(StringToInt(CLFASDSA))/float(StringToInt(CFSSDSA))) 
        else:
            CFRAGRATIOSDSA = 1.00    

        if StringToInt(CFSRDSA) > 0:
            CFRAGRATIORDSA = '%.2f'%(float(StringToInt(CLFARDSA))/float(StringToInt(CFSRDSA))) 
        else:
            CFRAGRATIORDSA = 1.00    
                
        IntItem.SMItem.CDSASize = StringToInt(CCDSA)
        IntItem.SMItem.UDSASize = StringToInt(CUDSA)
        IntItem.SMItem.SDSASize = StringToInt(CSDSA)
        IntItem.SMItem.RDSASize = StringToInt(CRDSA)

        IntItem.SMItem.CDSAHWM = StringToInt(CCDSA) - StringToInt(CLFSCDSA)
        IntItem.SMItem.UDSAHWM = StringToInt(CUDSA) - StringToInt(CLFSUDSA)
        IntItem.SMItem.SDSAHWM = StringToInt(CSDSA) - StringToInt(CLFSSDSA)
        IntItem.SMItem.RDSAHWM = StringToInt(CRDSA) - StringToInt(CLFSRDSA)
        IntItem.SMItem.DSAHWMTotal = IntItem.SMItem.CDSAHWM + IntItem.SMItem.UDSAHWM + IntItem.SMItem.SDSAHWM + IntItem.SMItem.RDSAHWM
        IntItem.SMItem.DSAHWMRatio = '%.2f'%(float(IntItem.SMItem.DSAHWMTotal)/float(IntItem.SMItem.DSALIM)*100)

        IntItem.SMItem.DSASOSTms = CDSASOSTMS
        IntItem.SMItem.DSASTGVIOLTms = CDSASTGVIOLTMS    
        IntItem.SMItem.CDSAFragRatio = CFRAGRATIOCDSA
        IntItem.SMItem.UDSAFragRatio = CFRAGRATIOUDSA
        IntItem.SMItem.SDSAFragRatio = CFRAGRATIOSDSA
        IntItem.SMItem.RDSAFragRatio = CFRAGRATIORDSA

        index = index + 30    #Skip Storage Manager Statistics

    '''
    CICS 660 and below:
    0  Dynamic Storage Areas (above 16M)
    +  _________________________________
    0                                          ECDSA          EUDSA          ESDSA          ERDSA
    +                                ____________________________________________________________
    0  Current DSA size. . . . . . :          61440K         41984K          1024K        201728K
       Peak DSA Size . . . . . . . :          61440K         41984K          1024K        201728K
       Cushion Size. . . . . . . . :            128K             0K           128K           256K
    0  Free storage (inc. cushion) :           3084K         41664K           844K          2224K
       Percentage free storage . . :              5%            99%            82%             1%
       Peak free storage . . . . . :           3208K         41664K           848K          2324K
       Lowest free storage . . . . :           2116K         19072K           844K          2224K
       Largest free area . . . . . :           1024K          2048K           844K           528K
    0  Getmain Requests. . . . . . :         147445K        110746K        1179591              2
       Freemain Requests . . . . . :         147445K        110746K        1179591              0
    0  Current no of Subpools. . . :             385             22             13              4
       Add Subpool Requests. . . . :         2354975        2354975              0              0
       Delete Subpool Requests . . :         2354975        2354975              0              0
    0  Times no storage returned . :               0              0              0              0
       Times request suspended . . :               0              0              0              0
    0  Current suspended . . . . . :               0              0              0              0
       Peak requests suspended . . :               0              0              0              0
       Purged while waiting. . . . :               0              0              0              0
    0  Times cushion released. . . :               0              0              0              0
       Times went short on storage :               0              0              0              0
       Total time SOS. . . . . . . :   00:00:00.0000  00:00:00.0000  00:00:00.0000  00:00:00.0000
       Average time SOS. . . . . . :   00:00:00.0000  00:00:00.0000  00:00:00.0000  00:00:00.0000
    0  Storage violations. . . . . :               0              0              0              0
       Access. . . . . . . . . . . :            CICS           USER           USER       READONLY
    0  Current extents . . . . . . :              56             30              1            176
       Extents added . . . . . . . :               0              0              0              0
       Extents released. . . . . . :               0              0              0              0

    CICS 670 and above:
    +___________________________________________________________________________________________________________________________________
    0  Dynamic Storage Areas (above 16M)
    +  _________________________________
    0                                          ECDSA          EUDSA          ESDSA          ERDSA          ETDSA
    +                                ___________________________________________________________________________
    0  Current DSA size. . . . . . :         208896K       1041408K         21504K         35840K          1024K
       Peak DSA Size . . . . . . . :         208896K       1041408K         21504K         35840K          1024K
       Cushion Size. . . . . . . . :            128K             0K           128K           256K           128K
    0  Free storage (inc. cushion) :          15152K        915072K          1088K          1288K           992K
       Percentage free storage . . :              7%            87%             5%             3%            96%
       Peak free storage . . . . . :          15284K       1037824K          1088K          1288K           992K
       Lowest free storage . . . . :          14628K        480128K          1088K          1288K           992K
       Largest free area . . . . . :           5120K         21504K           572K           748K           992K
    0  Getmain Requests. . . . . . :         1684313         284611          18080              0          72320
       Freemain Requests . . . . . :         1684310         284608          18080              0          72320
    0  Current no of Subpools. . . :             393             31             14              4              6
       Add Subpool Requests. . . . :           36223          36223              0              0              0
       Delete Subpool Requests . . :           36223          36223              0              0              0
    0  Times no storage returned . :               0              0              0              0              0
       Times request suspended . . :               0              0              0              0              0
    0  Current suspended . . . . . :               0              0              0              0              0
       Peak requests suspended . . :               0              0              0              0              0
       Purged while waiting. . . . :               0              0              0              0              0
    0  Times cushion released. . . :               0              0              0              0              0
       Times went short on storage :               0              0              0              0              0
       Total time SOS. . . . . . . :   00:00:00.0000  00:00:00.0000  00:00:00.0000  00:00:00.0000  00:00:00.0000
       Average time SOS. . . . . . :   00:00:00.0000  00:00:00.0000  00:00:00.0000  00:00:00.0000  00:00:00.0000
    0  Storage violations. . . . . :               0              0              0              0              0
       Access. . . . . . . . . . . :            CICS           USER           USER       READONLY        TRUSTED
    0  Current extents . . . . . . :              43             62             21             22              1
       Extents added . . . . . . . :               0              0              0              0              0
       Extents released. . . . . . :               0              0              0              0              0   
    '''

    if "DYNAMIC STORAGE AREAS (ABOVE 16M)" in line.upper():        
        C5PARTOFEDSA=LineList[index + 5].split(" ")
        C5PARTOFEDSA=[x for x in C5PARTOFEDSA if x != '' and x !='.']
        CECDSA=C5PARTOFEDSA[4].strip("\r\n")
        CEUDSA=C5PARTOFEDSA[5].strip("\r\n")
        CESDSA=C5PARTOFEDSA[6].strip("\r\n")
        CERDSA=C5PARTOFEDSA[7].strip("\r\n")
        if CICSVERSION == "6.6.0":
            CETDSA = "0"
        else:
            CETDSA=C5PARTOFEDSA[8].strip("\r\n")

        CFREESTG = LineList[index + 7].split(" ")
        CFREESTG = [x for x in CFREESTG if x != '' and x !='.']
        CFSECDSA = CFREESTG[6].strip("\r\n")
        CFSEUDSA = CFREESTG[7].strip("\r\n")
        CFSESDSA = CFREESTG[8].strip("\r\n")
        CFSERDSA = CFREESTG[9].strip("\r\n") 


        CLOWFREESTG = LineList[index + 10].split(" ")
        CLOWFREESTG=[x for x in CLOWFREESTG if x != '' and x !='.']        
        CLFSECDSA=CLOWFREESTG[4].strip("\r\n")
        CLFSEUDSA=CLOWFREESTG[5].strip("\r\n")
        CLFSESDSA=CLOWFREESTG[6].strip("\r\n")
        CLFSERDSA=CLOWFREESTG[7].strip("\r\n")



        CLRGFREEAREA = LineList[index + 11].split(" ")
        CLRGFREEAREA=[x for x in CLRGFREEAREA if x != '' and x !='.']        
        CLFAECDSA=CLRGFREEAREA[4].strip("\r\n")
        CLFAEUDSA=CLRGFREEAREA[5].strip("\r\n")
        CLFAESDSA=CLRGFREEAREA[6].strip("\r\n")
        CLFAERDSA=CLRGFREEAREA[7].strip("\r\n")

        CEDSASOSTMS = LineList[index + 23].split(" ")
        CEDSASOSTMS = [x for x in CEDSASOSTMS if x != '' and x !='.']
        CEDSASOSTMS = StringToInt(CEDSASOSTMS[6].strip("\r\n")) + StringToInt(CEDSASOSTMS[7].strip("\r\n")) \
                     + StringToInt(CEDSASOSTMS[8].strip("\r\n")) + StringToInt(CEDSASOSTMS[9].strip("\r\n")) 

        CEDSASTGVIOLTMS = LineList[index + 26].split(" ")
        CEDSASTGVIOLTMS = [x for x in CEDSASTGVIOLTMS if x != '' and x !='.'] 
        CEDSASTGVIOLTMS = StringToInt(CEDSASTGVIOLTMS[4].strip("\r\n")) + StringToInt(CEDSASTGVIOLTMS[5].strip("\r\n")) \
                     + StringToInt(CEDSASTGVIOLTMS[6].strip("\r\n")) + StringToInt(CEDSASTGVIOLTMS[7].strip("\r\n"))        
        
        DSARatio = '%.2f'%(float((StringToInt(CPeakDSA))/float(StringToInt(CDSALIM))*100))
        EDSARatio = '%.2f'%(float((StringToInt(CPeakEDSA))/float(StringToInt(CEDSALIM))*100))
        
        DSATotal=StringToInt(CCDSA)+StringToInt(CUDSA)+StringToInt(CSDSA)+StringToInt(CRDSA)
        EDSATotal=StringToInt(CECDSA)+StringToInt(CEUDSA)+StringToInt(CESDSA)+StringToInt(CERDSA)+StringToInt(CETDSA)
        CDSARatio= '%.2f'%(float(StringToInt(CCDSA))/DSATotal*100)
        UDSARatio= '%.2f'%(float(StringToInt(CUDSA))/DSATotal*100)
        SDSARatio= '%.2f'%(float(StringToInt(CSDSA))/DSATotal*100)
        RDSARatio= '%.2f'%(float(StringToInt(CRDSA))/DSATotal*100)
        
        
        ECDSARatio= '%.2f'%(float(StringToInt(CECDSA))/EDSATotal*100)
        EUDSARatio= '%.2f'%(float(StringToInt(CEUDSA))/EDSATotal*100)
        ESDSARatio= '%.2f'%(float(StringToInt(CESDSA))/EDSATotal*100)
        ERDSARatio= '%.2f'%(float(StringToInt(CERDSA))/EDSATotal*100)
        ETDSARatio= '%.2f'%(float(StringToInt(CETDSA))/EDSATotal*100)


        CFRAGRATIOECDSA = '%.2f'%(float(StringToInt(CLFAECDSA))/float(StringToInt(CFSECDSA)))
        CFRAGRATIOEUDSA = '%.2f'%(float(StringToInt(CLFAEUDSA))/float(StringToInt(CFSEUDSA))) 
        CFRAGRATIOESDSA = '%.2f'%(float(StringToInt(CLFAESDSA))/float(StringToInt(CFSESDSA))) 
        CFRAGRATIOERDSA = '%.2f'%(float(StringToInt(CLFAERDSA))/float(StringToInt(CFSERDSA)))    


        IntItem.SMItem.ECDSASize = StringToInt(CECDSA)
        IntItem.SMItem.EUDSASize = StringToInt(CEUDSA)
        IntItem.SMItem.ESDSASize = StringToInt(CESDSA)
        IntItem.SMItem.ERDSASize = StringToInt(CERDSA)
        IntItem.SMItem.ETDSASize = StringToInt(CETDSA)

        IntItem.SMItem.ECDSAHWM = StringToInt(CECDSA) - StringToInt(CLFSECDSA)
        IntItem.SMItem.EUDSAHWM = StringToInt(CEUDSA) - StringToInt(CLFSEUDSA)
        IntItem.SMItem.ESDSAHWM = StringToInt(CESDSA) - StringToInt(CLFSESDSA)
        IntItem.SMItem.ERDSAHWM = StringToInt(CERDSA) - StringToInt(CLFSERDSA)
        IntItem.SMItem.EDSAHWMTotal = IntItem.SMItem.ECDSAHWM + IntItem.SMItem.EUDSAHWM + IntItem.SMItem.ESDSAHWM + IntItem.SMItem.ERDSAHWM
        IntItem.SMItem.EDSAHWMRatio = '%.2f'%(float(IntItem.SMItem.EDSAHWMTotal)/float(IntItem.SMItem.EDSALIM)*100)

        IntItem.SMItem.EDSASOSTms = CEDSASOSTMS
        IntItem.SMItem.EDSASTGVIOLTms = CEDSASTGVIOLTMS    

        IntItem.SMItem.DSARatio = DSARatio
        IntItem.SMItem.EDSARatio = EDSARatio
        IntItem.SMItem.CDSARatio = CDSARatio
        IntItem.SMItem.UDSARatio = UDSARatio
        IntItem.SMItem.SDSARatio = SDSARatio
        IntItem.SMItem.RDSARatio = RDSARatio
        IntItem.SMItem.ECDSARatio = ECDSARatio
        IntItem.SMItem.EUDSARatio = EUDSARatio
        IntItem.SMItem.ESDSARatio = ESDSARatio
        IntItem.SMItem.ERDSARatio = ERDSARatio
        IntItem.SMItem.ETDSARatio = ETDSARatio

        IntItem.SMItem.ECDSAFragRatio = CFRAGRATIOECDSA
        IntItem.SMItem.EUDSAFragRatio = CFRAGRATIOEUDSA
        IntItem.SMItem.ESDSAFragRatio = CFRAGRATIOESDSA
        IntItem.SMItem.ERDSAFragRatio = CFRAGRATIOERDSA

        SMRep.AppendItem(IntItem.SMItem)    

        index = index + 30    #Skip Storage Manager Statistics    

    '''
    +___________________________________________________________________________________________________________________________________
    0  DYNAMIC STORAGE AREAS (ABOVE 2GB)
    +  _________________________________
    0                                          GCDSA
    +                                _______________
    0  CURRENT DSA SIZE. . . . . . :              6M
       PEAK DSA SIZE . . . . . . . :              9M
       CUSHION SIZE. . . . . . . . :             64M
    0  FREE STORAGE (INC. CUSHION) :           2042M
       PEAK FREE STORAGE . . . . . :           2042M
       LOWEST FREE STORAGE . . . . :           2042M
       LARGEST FREE AREA . . . . . :           2041M
    0  GETMAIN REQUESTS. . . . . . :               3
       FREEMAIN REQUESTS . . . . . :               3
    0  CURRENT NO OF SUBPOOLS. . . :              11
       ADD SUBPOOL REQUESTS. . . . :               2
       DELETE SUBPOOL REQUESTS . . :               2
    0  TIMES NO STORAGE RETURNED . :               0
       TIMES REQUEST SUSPENDED . . :               0
    0  CURRENT SUSPENDED . . . . . :               0
       PEAK REQUESTS SUSPENDED . . :               0
       PURGED WHILE WAITING. . . . :               0
    0  TIMES CUSHION RELEASED. . . :               0
       TIMES WENT SHORT ON STORAGE :               0
       TOTAL TIME SOS. . . . . . . :   00:00:00.0000
       AVERAGE TIME SOS. . . . . . :   00:00:00.0000
    0  STORAGE VIOLATIONS. . . . . :               0
       ACCESS. . . . . . . . . . . :            CICS
    0  CURRENT EXTENTS . . . . . . :               1
       EXTENTS ADDED . . . . . . . :               0
       EXTENTS RELEASED. . . . . . :               0
    '''
    if "DYNAMIC STORAGE AREAS (ABOVE 2GB)" in line.upper():
        CGDSASOS =  LineList[index+22].split(" ")
        CGDSASOS =  [x for x in CGDSASOS if x != '' and x != '.']
        CGDSASOS = CGDSASOS[6].strip("\r\n")

        CGDSASTGVOIL =  LineList[index+25].split(" ")
        CGDSASTGVOIL =  [x for x in CGDSASTGVOIL if x != '' and x != '.']
        CGDSASTGVOIL = CGDSASTGVOIL[4].strip("\r\n")      

        IntItem.SMItem.GDSASTGVIOLTms = int(CGDSASOS)
        IntItem.SMItem.CGDSASTGVOIL = int(CGDSASTGVOIL)

        index = index + 29

    '''
    +___________________________________________________________________________________________________________________________________
    0DUMP STATISTICS
    +_______________
    -  System Dumps
    +  ____________
    0  Dumps taken      :        0
       Dumps suppressed :        0
    '''
    if "-  SYSTEM DUMPS" in line.upper() and "DUMPS TAKEN" in LineList[index+2].upper():
        DUFlag = 1
        CSysDumpTaken = LineList[index+2].split(" ")
        CSysDumpTaken = [x for x in CSysDumpTaken if x != '' and x != '.']
        CSysDumpTaken = CSysDumpTaken[4].strip("\r\n")


        CSysDumpSupp = LineList[index+3].split(" ")
        CSysDumpSupp =  [x for x in CSysDumpSupp if x != '']
        CSysDumpSupp = CSysDumpSupp[3].strip("\r\n")

        IntItem.DUItem.SysDumpTaken = int(CSysDumpTaken)
        IntItem.DUItem.SysDumpSupp = int(CSysDumpSupp)

        index = index + 3

    '''
    -  Transaction Dumps
    +  _________________
    0  Dumps taken      :        0
       Dumps suppressed :        0
    '''
    if "-  TRANSACTION DUMPS" in line.upper() and "DUMPS TAKEN" in LineList[index+2].upper():
        CTranDumpTaken = LineList[index+2].split(" ")
        CTranDumpTaken =  [x for x in CTranDumpTaken if x != '']
        CTranDumpTaken = CTranDumpTaken[4].strip("\r\n")

        CTranDumpSupp = LineList[index+3].split(" ")
        CTranDumpSupp =  [x for x in CTranDumpSupp if x != '']
        CTranDumpSupp = CTranDumpSupp[3].strip("\r\n")

        IntItem.DUItem.TranDumpTaken = int(CTranDumpTaken)
        IntItem.DUItem.TranDumpSupp = int(CTranDumpSupp)

        DURep.AppendItem(IntItem.DUItem)

        index = index + 3



    '''
    +___________________________________________________________________________________________________________________________________
    0LOGSTREAM GLOBAL STATISTICS
    +___________________________
    0  Activity Keypoint Frequency (AKPFREQ) . . . . . :       5000
       Activity Keypoints Taken. . . . . . . . . . . . :          0
    0  Log Deferred Force (LGDFINT) Interval (msec). . :          5
    '''
    if "0LOGSTREAM GLOBAL STATISTICS" in line.upper():
        LGFlag = 1
        CAKPFREQ =  LineList[index+2].split(" ")
        CAKPFREQ =  [x for x in CAKPFREQ if x != '' and x != '.']
        CAKPFREQ = CAKPFREQ[6].strip("\r\n")

        CAKPTaken = LineList[index+3].split(" ")                                 
        CAKPTaken = [x for x in CAKPTaken if x != '' and x != '.']
        CAKPTaken = CAKPTaken[4].strip("\r\n")

        CLGDFINT = LineList[index+4].split(" ")   
        CLGDFINT = [x for x in CLGDFINT if x != '' and x != '.']
        CLGDFINT = CLGDFINT[8].strip("\r\n")        

        index = index + 5

    '''
    +___________________________________________________________________________________________________________________________________
    0LOGSTREAM STATISTICS - RESOURCE
    +_______________________________
    0                              System                    Max Block  DASD  Retention   Auto    Delete    Query
       Log Stream Name               Log   Structure Name      Length   Only    Period   Delete  Requests  Requests
    +  ____________________________________________________________________________________________________________

       CIRUFA.CIT3FA12.DFHLOG        Yes                        64000   Yes          0     No           0         3
       CIRUFA.CIT3FA12.DFHSHUNT      Yes                        64000   Yes          0     No           0         0
       ____________________________________________________________________________________________________________
       *TOTALS*                                                                                         0         3
    '''
    if "0LOGSTREAM STATISTICS - RESOURCE" in line:    
        CDFHLOG = LineList[index+6].split(" ")
        CDFHLOG = [x for x in CDFHLOG if x != '' and x != '.']
        CDFHLOGName = CDFHLOG[0]
        CDFHLOGDASD = CDFHLOG[3]

        index = index + 10

    '''
    +___________________________________________________________________________________________________________________________________
    0LOGSTREAM STATISTICS - REQUEST
    +______________________________
    0                               Write      Bytes    Buffer     Waits    Current      Peak      Total     Browse    Browse     Retry
       Log Stream Name             Requests   Written   Appends  Buff Full Frce Wtrs  Frce Wtrs  Force Wts   Starts     Reads    Errors
    +  _________________________________________________________________________________________________________________________________

       CIRUFA.CIT3FA12.DFHLOG             0         0         0          0         0          0          0         0         0         0
       CIRUFA.CIT3FA12.DFHSHUNT           0         0         0          0         0          0          0         0         0         0
       _________________________________________________________________________________________________________________________________
       *TOTALS*                           0         0         0          0                               0         0         0         0
    '''
    if "0LOGSTREAM STATISTICS - REQUEST" in line:             
        CLOGRequest =LineList[index+6].split(" ")
        CLOGRequest=[x for x in CLOGRequest if x != '']
        CWriteReq = CLOGRequest[1]
        CBytesWritten= CLOGRequest[2]
        CBufAppend = CLOGRequest[3]
        CWttFufBuf = CLOGRequest[4]
        CPkForceWts = CLOGRequest[6]
        CRetryError = CLOGRequest[10].strip("\r\n")

        IntItem.LGItem.AKPFREQ = StringToInt(CAKPFREQ)
        IntItem.LGItem.AKPTaken = StringToInt(CAKPTaken)
        IntItem.LGItem.LGDFINT = StringToInt(CLGDFINT)
        IntItem.LGItem.DFHLOG = CDFHLOGName
        IntItem.LGItem.DASDOnly = CDFHLOGDASD
        IntItem.LGItem.AKPTaken = StringToInt(CAKPTaken)
        IntItem.LGItem.WriteReq = StringToInt(CWriteReq)
        IntItem.LGItem.ByteWritten = StringToInt(CBytesWritten)
        IntItem.LGItem.BufAppend = StringToInt(CBufAppend)
        IntItem.LGItem.WaitFullBuf = StringToInt(CWttFufBuf)
        IntItem.LGItem.PkForceWts = StringToInt(CPkForceWts)

        CShuntRequest = LineList[index+7].split(" ")
        CShuntRequest=[x for x in CShuntRequest if x != '']
        if "DFHSHUNT" in CShuntRequest[0]:
            CShuntWriteRequest = CShuntRequest[1]
            IntItem.LGItem.ShuntWriteRequest = StringToInt(CShuntWriteRequest)

        LGRep.AppendItem(IntItem.LGItem)

        index = index + 10

    '''            
    +___________________________________________________________________________________________________________________________________
    0DB2 CONNECTION STATISTICS
    +_________________________

       DB2 Connection Name . . . . . . . . . . . . :   CBSDB2C      DB2 Connect Date / Time . . . . . . . . . . :   09/08/2015 / 22:11:18
       DB2 Groupid . . . . . . . . . . . . . . . . :   BMA0         DB2 Disconnect Date / Time. . . . . . . . . :
       Resyncmember. . . . . . . . . . . . . . . . :   YES
       DB2 Sysid . . . . . . . . . . . . . . . . . :   BMA1         DB2 Release . . . . . . . . . . . . . . . . :   10.1.0
    0  Connection Limit (TCB Limit). . . . . . . . :        120     Current number of Connections with a TCB. . :          5
       Thread reuselimit . . . . . . . . . . . . . :       1000     Peak number of Connections with a TCB . . . :         28
       Current number of Connections without a TCB :         44
    0  Current number of tasks on the Conn Readyq. :          0
       Peak number of tasks on the Conn Readyq . . :          0
    0  Pool Thread Plan name . . . . . . . . . . . :
       Pool Thread Dynamic Planexit name . . . . . :   DSNCUEXT
       Pool Thread Authtype. . . . . . . . . . . . :   USERID       Pool Thread Authid. . . . . . . . . . . . . :
       Pool Thread Accountrec setting. . . . . . . :   TASK
       Pool Thread Threadwait setting. . . . . . . :   YES
       Pool Thread Priority. . . . . . . . . . . . :   HIGH
    0  Number of calls using Pool Threads. . . . . :          0
       Number of Pool Thread Signons . . . . . . . :          0
       Number of Pool Thread Partial Signons . . . :          0
       Number of Pool Thread Commits . . . . . . . :          0
       Number of Pool Thread Aborts. . . . . . . . :          0
       Number of Pool Thread Single Phases . . . . :          0
       Number of Pool Thread Creates . . . . . . . :          0
       Number of Pool Thread Reuses. . . . . . . . :          0
       Number of Pool Thread Terminates. . . . . . :          0     Total times reuselimit hit by a pool thread :          0
       Number of Pool Thread Waits . . . . . . . . :          0
    0  Current Pool Thread Limit . . . . . . . . . :        120
       Current number of Pool Threads in use . . . :          0
       Peak number of Pool Threads in use. . . . . :          0
    0  Current number of Pool tasks. . . . . . . . :          0
       Peak number of Pool tasks . . . . . . . . . :          0
       Total number of Pool tasks. . . . . . . . . :          0
    0  Current number of tasks on the Pool Readyq. :          0
       Peak number of tasks on the Pool Readyq . . :          0
    0  Command Thread Authtype . . . . . . . . . . :   USERID       Command Thread Authid . . . . . . . . . . . :
    0  Number of Calls using Command Threads . . . :          0
       Number of Command Thread Signons. . . . . . :          0
       Number of Command Thread Creates. . . . . . :          0
       Number of Command Thread Terminates . . . . :          0
       Number of Command Thread Overflows to Pool. :          0
    0  Command Thread Limit. . . . . . . . . . . . :          1
       Current number of Command Threads . . . . . :          0
       Peak number of Command Threads. . . . . . . :          0
    '''

    if "0DB2 CONNECTION STATISTICS" in line:
        DBFlag = 1
        CDB2Conn = LineList[index+3].split(" ")
        CDB2Conn = [x for x in CDB2Conn if x != '' and x != '.']
        CDB2Conn = CDB2Conn[4]

        CTCBLimit = LineList[index+7].split(" ")
        CTCBLimit = [x for x in CTCBLimit if x != '' and x != '.']
        CTCBLimit = CTCBLimit[6]

        CPkConnWithTCB = LineList[index+8].split(" ")
        CPkConnWithTCB = [x for x in CPkConnWithTCB if x != '' and x != '.']

        if CICSVERSION == "6.6.0":
            CPkConnWithTCB = CPkConnWithTCB[8].strip("\r\n")
        else:
            CPkConnWithTCB = CPkConnWithTCB[12].strip("\r\n")

        CPkConnReadyq = LineList[index+11].split(" ")
        CPkConnReadyq = [x for x in CPkConnReadyq if x != '' and x != '.']
        CPkConnReadyq = CPkConnReadyq[9].strip("\r\n")

        CPoolPriority = LineList[index+17].split(" ")
        CPoolPriority = [x for x in CPoolPriority if x != '' and x != '.']
        CPoolPriority = CPoolPriority[4].strip("\r\n")

        CPkPoolTasks = LineList[index+32].split(" ")
        CPkPoolTasks = [x for x in CPkPoolTasks if x != '' and x != '.']
        CPkPoolTasks = CPkPoolTasks[6].strip("\r\n")
      
        CPkPoolReadyq = LineList[index+35].split(" ")
        CPkPoolReadyq = [x for x in CPkPoolReadyq if x != '' and x != '.']
        CPkPoolReadyq = CPkPoolReadyq[9].strip("\r\n")        

        CPkCmdThreads = LineList[index+44].split(" ")
        CPkCmdThreads = [x for x in CPkCmdThreads if x != '' and x != '.']
        CPkCmdThreads = CPkCmdThreads[6].strip("\r\n")

        CPkDB2TCBRatio = str( '%.2f'%(float(CPkConnWithTCB) / float(CTCBLimit) * 100))  #Peak connections/tcblimit
     
        IntItem.DCItem.DB2Conn = CDB2Conn
        IntItem.DCItem.TCBLimit = StringToInt(CTCBLimit)
        IntItem.DCItem.PkConnWithTCB = StringToInt(CPkConnWithTCB)
        IntItem.DCItem.PkConnReadyq = StringToInt(CPkConnReadyq)
        IntItem.DCItem.PoolPriority = CPoolPriority
        IntItem.DCItem.PkPoolTasks = StringToInt(CPkPoolTasks)
        IntItem.DCItem.PkPoolReadyq = StringToInt(CPkPoolReadyq)
        IntItem.DCItem.PkCmdThreads = StringToInt(CPkCmdThreads)
        IntItem.DCItem.DB2TCBRatio = float(CPkDB2TCBRatio)

        DBRep.AppendItem(IntItem.DCItem)

        index = index + 44                                                    #Skip the DB2CONN Statistics
        
    '''
    0DB2ENTRY STATISTICS - RESOURCES
    +_______________________________
    0  DB2Entry    Plan    PlanExit    Auth     Auth   Account  Thread  Thread
         Name      Name      Name       Id      Type   Records   Wait    Prty
    +  _______________________________________________________________________

       CICSIAD   CIUCICS                       USERID   NONE     POOL   EQUAL
       DB2EAT    PLANCGAT            NGDBA     N/A      TASK     POOL   EQUAL
       DB2EK     PLANCGK             NGDBA     N/A      TASK     POOL   EQUAL
       DB2EL     PLANCGL             NGDBA     N/A      TASK     POOL   EQUAL
       DB2EM     PLANCGM             NGDBA     N/A      TASK     POOL   EQUAL
       DB2EN     PLANCGN             NGDBA     N/A      TASK     POOL   EQUAL
       DB2EO     PLANCGO             NGDBA     N/A      TASK     POOL   EQUAL
       DB2EOT    PLANCGOT            NGDBA     N/A      TASK     POOL   EQUAL
       DB2EP     PLANCGP             NGDBA     N/A      TASK     POOL   EQUAL
       DB2EQ     PLANCGQ             NGDBA     N/A      TASK     POOL   EQUAL
       DB2ER     PLANCGR             NGDBA     N/A      TASK     POOL   EQUAL
       DB2ES     PLANCGS             NGDBA     N/A      TASK     POOL   EQUAL
       DB2ET     PLANCGT             NGDBA     N/A      TASK     POOL   EQUAL
       DB2EU     PLANCGU             NGDBA     N/A      TASK     POOL   EQUAL
       DB2EV     PLANCGV             NGDBA     N/A      TASK     POOL   EQUAL
       DB2EW     PLANCGW             NGDBA     N/A      TASK     POOL   EQUAL
       DB2EX     PLANCGX             NGDBA     N/A      TASK     POOL   EQUAL
       DB2EY     PLANCGY             NGDBA     N/A      TASK     POOL   EQUAL
       DB2EZ     PLANCGZ             NGDBA     N/A      TASK     POOL   EQUAL
       DB2E0     PLANCG0             NGDBA     N/A      TASK     POOL   EQUAL
       DB2E1     PLANCG1             NGDBA     N/A      TASK     POOL   EQUAL
       DB2E2     PLANCG2             NGDBA     N/A      TASK     POOL   EQUAL
       DB2E3     PLANCG3             NGDBA     N/A      TASK     POOL   EQUAL
       DB2E4     PLANCG4             NGDBA     N/A      TASK     POOL   EQUAL
       DB2E5     PLANCG5             NGDBA     N/A      TASK     POOL   EQUAL
       DB2E6     PLANCG6             NGDBA     N/A      TASK     POOL   EQUAL
       DB2E7     PLANCG7             NGDBA     N/A      TASK     POOL   EQUAL
       DB2E8     PLANCG8             NGDBA     N/A      TASK     POOL   EQUAL
       DB2E9     PLANCG9             NGDBA     N/A      TASK     POOL   EQUAL
    '''
    if "0DB2ENTRY STATISTICS - RESOURCES" in line:
        index =  index + 6
        while ("DB2ENTRY STATISTICS - REQUESTS" not in LineList[index+1]):    #Force to escape from while loop
            CDB2EntryResource = LineList[index].split(" ")
            CDB2EntryResource = [x for x in CDB2EntryResource if x != '']
            CThreadPrty = CDB2EntryResource[-1].strip("\r\n")
            if CThreadPrty == "HIGH" or CThreadPrty== "LOW" or CThreadPrty == "EQUAL":  #It should be an valid entry
                DEItem = DB2ENTRYItem()
                DEItem.DB2EntryName = CDB2EntryResource[0]
                DEItem.AccountRec = CDB2EntryResource[-3]
                DEItem.ThreadWait = CDB2EntryResource[-2]
                DEItem.ThreadPrty = CThreadPrty
                IntItem.DCItem.AppendDB2Entry(DEItem)                    #Append a new DB2Entry in this DB2CONN      
            index = index + 1

    '''
    +___________________________________________________________________________________________________________________________________
    0DB2ENTRY STATISTICS - REQUESTS
    +______________________________
    0  DB2Entry    Call     Signon    Partial   Commit     Abort    Single    Thread    Thread    Thread       Thread
         Name      Count     Count    Signon     Count     Count     Phase    Create     Reuse     Terms   Waits/Overflows
    +  ___________________________________________________________________________________________________________________

       CICSIAD          0         0         0         0         0         0         0         0         0             0
       DB2EAT           0         0         0         0         0         0         0         0         0             0
       DB2EK            0         0         0         0         0         0         0         0         0             0
       DB2EL            0         0         0         0         0         0         0         0         0             0
       DB2EM            0         0         0         0         0         0         0         0         0             0
       DB2EN        41096       768       768       583       372       476       330      1101       330             0
       DB2EO       600227      4747      4747        14       124      4964       418      4628       418            56
       DB2EOT    11131240    185320    185320     23671    117812     45159       253    185909       252             0
       DB2EP         3353        33        33         0         6        83        21        68        21             0
       DB2EQ            0         0         0         0         0         0         0         0         0             0
       DB2ER         6045      1209      1209         0         0      1209       153      1056       152             0
       DB2ES            0         0         0         0         0         0         0         0         0             0
       DB2ET        33089       183       183         0         0       202        72       130        71             0
       DB2EU            0         0         0         0         0         0         0         0         0             0
       DB2EV            0         0         0         0         0         0         0         0         0             0
       DB2EW      1599612     12233     12233         2        30     12246        14     12264        14             0
       DB2EX        59976       141       141         0        30       341        89       282        89             0
       DB2EY          822         6         6         0         1         7         5         3         5             0
       DB2EZ          506        10        10         0         0        11         3         8         3             0
       DB2E0       505632      5160      5160         0       410      4772       144      5038       144             0
       DB2E1       201520      2859      2859         0        41      5021       160      4901       161             0
       DB2E2        83354       233       233         0        58       424       117       362       117             0
       DB2E3      1161174      3968      3968         0       637      7148       126      7659       127             0
       DB2E4       369160      2364      2363         0       119      3820       162      3764       163             0
       DB2E5      3170112      1806      1806        30       252      1756       237      1801       237             0
       DB2E6        48024        96        96         0        11       148        63        96        63             0
       DB2E7          288         8         8         1         0        14         4        11         4             0
       DB2E8            0         0         0         0         0         0         0         0         0             0
       DB2E9            0         0         0         0         0         0         0         0         0             0
       ___________________________________________________________________________________________________________________
       *TOTALS*  19015230    221144    221143     24301    119903     87801      2371    229081      2371            56     
    '''

    if "DB2ENTRY STATISTICS - REQUESTS" in line:
        index = index + 6

        for item in IntItem.DCItem.DB2ENTRYItemList:
            CDB2EntryReq = LineList[index].split(" ")             
            CDB2EntryReq = [x for x in CDB2EntryReq if x != '']            
            CCallCount  = CDB2EntryReq[1]                         #Call count
            CSingonCount = CDB2EntryReq[2]                        #Singon Count
            CPartialSignon = CDB2EntryReq[3]                      #Partical signon     
            CCommitCount = CDB2EntryReq[4]                        #2PC Count
            CAbortCount = CDB2EntryReq[5]                         #Abort Count
            CSinglePhase =  CDB2EntryReq[6]                       #1PC Count
            CThreadCreate = CDB2EntryReq[7]                       #Thread Create
            CThreadReuse  = CDB2EntryReq[8]                       #Thread Reuse
            CThreadTerms = CDB2EntryReq[9]                        #Thread Terms
            CThreadWtOf = CDB2EntryReq[10].strip("\r\n")          #Thread wait or overflow
            if float(CCommitCount) + float(CSinglePhase) + float(CAbortCount) != 0:
                CCommitRatio = str( '%.2f'%( (float(CCommitCount) + float(CSinglePhase)) / (float(CCommitCount) + float(CSinglePhase) + float(CAbortCount)) * 100))
            else:
                CCommitRatio = "0"
        
            item.CallCount = StringToInt(CCallCount)
            item.SignonCount = StringToInt(CSingonCount)
            item.PartialSignon = StringToInt(CPartialSignon)
            item.TwoPCommitCount = StringToInt(CCommitCount)
            item.AbortCount = StringToInt(CAbortCount)
            item.OnePCommitCount = StringToInt(CSinglePhase)
            item.ThreadCreate = StringToInt(CThreadCreate)
            item.ThreadReuse = StringToInt(CThreadReuse)
            item.ThreadTerms = StringToInt(CThreadTerms)
            item.ThreadWtOrOF = StringToInt(CThreadWtOf)
            item.CommitRatio = float(CCommitRatio)

            index = index + 1  
    '''
    CICS 660 and below:
    +___________________________________________________________________________________________________________________________________
    0DB2ENTRY STATISTICS - PERFORMANCE
    +_________________________________
    0  DB2Entry   Thread    Thread    Thread    Pthread   Pthread   Pthread    Task      Task      Task     Readyq    Readyq
         Name      Limit    Current     HWM      Limit    Current     HWM     Current     HWM      Total    Current     HWM
    +  ______________________________________________________________________________________________________________________

       CICSIAD         15         0         0         0         0         0         0         0         0         0         0
       DB2EAT          20         0         0        10         0         0         0         0         0         0         0
       DB2EK           10         0         0         5         0         0         0         0         0         0         0
       DB2EL           10         0         0         5         0         0         0         0         0         0         0
       DB2EM           10         0         0         5         0         0         0         0         0         0         0
       DB2EN           20         0         8         5         1         5         0         8      1431         0         0
       DB2EO           10         0        10         5         1         5         0        20      4735         0         0
       DB2EOT          20         0         7         5         2         5         0         7    185314         0         0
       DB2EP           10         0         1         5         0         1         0         1        33         0         0
       DB2EQ           10         0         0         5         0         0         0         0         0         0         0
       DB2ER           10         0         2         5         1         2         0         2      1209         0         0
       DB2ES           10         0         0         5         0         0         0         0         0         0         0
       DB2ET           10         0         1         5         1         1         0         1       183         0         0
       DB2EU           10         0         0         5         0         0         0         0         0         0         0
       DB2EV           10         0         0         5         0         0         0         0         0         0         0
       DB2EW           10         0         2         5         1         2         0         2     12233         0         0
       DB2EX           10         0         3         5         1         3         0         3       141         0         0
       DB2EY           10         0         1         5         0         1         0         1         6         0         0
       DB2EZ           10         0         1         5         0         1         0         1        10         0         0
       DB2E0          100         0         2        50         1         2         0         2      5160         0         0
       DB2E1           60         0         1        30         0         1         0         1      2859         0         0
       DB2E2           30         0         1        15         0         1         0         1       233         0         0
       DB2E3           10         0         3         5         0         3         0         3      3968         0         0
       DB2E4           30         0         3        15         0         3         0         3      2364         0         0
       DB2E5           10         0         2         5         1         2         0         2      1806         0         0
       DB2E6           10         0         1         5         0         1         0         1        96         0         0
       DB2E7           10         0         1         5         0         1         0         1         8         0         0
       DB2E8           10         0         0         5         0         0         0         0         0         0         0
       DB2E9           10         0         0         5         0         0         0         0         0         0         0
       ______________________________________________________________________________________________________________________
       *TOTALS*       505         0                 235        10                   0              221789         0       

    CICS 670 and above:
    0DB2ENTRY STATISTICS - PERFORMANCE
    +_________________________________
    0  DB2Entry   Thread    Thread    Thread    Pthread   Pthread   Pthread    Task      Task      Task     Readyq    Readyq    Reuselm
         Name      Limit    Current     HWM      Limit    Current     HWM     Current     HWM      Total    Current     HWM       hits
    +  ________________________________________________________________________________________________________________________________

       DTPB           100         0         2        40         0         2         0         2        12         0         0         0
       ________________________________________________________________________________________________________________________________
       *TOTALS*       100         0                  40         0                   0                  12         0                   0

    '''
    if "DB2ENTRY STATISTICS - PERFORMANCE" in line:
        index = index + 6

        for item in IntItem.DCItem.DB2ENTRYItemList:
            CDB2EntryPerf = LineList[index].split(" ")             
            CDB2EntryPerf = [x for x in CDB2EntryPerf if x != '']
            CThreadLimit  = CDB2EntryPerf[1]                       #Thread Limit
            CThreadHWM = CDB2EntryPerf[3]                          #Thread HWM
            CPthreadLimit = CDB2EntryPerf[4]                       #Phread Limit
            CPthreadHWM = CDB2EntryPerf[6]                         #Phread HWM
            CReadyq  = CDB2EntryPerf[11]                           #Readyq HWM

            if CICSVERSION == "6.6.0":
                CReadyq = CReadyq.strip("\r\n")
                CReuselm = 0
            else:
                CReuselm = CDB2EntryPerf[12].strip("\r\n")               #Reuselm hits
 
            CThreadRatio = str( '%.2f'%(float(CThreadHWM) / float(CThreadLimit) * 100))  #Thread HWM/thread limit
            if CPthreadLimit == "0":
                CPthreadRatio = 0
            else:
                CPthreadRatio = str( '%.2f'%(float(CPthreadHWM) / float(CPthreadLimit) * 100))  #Protected thread HWM/Protected thread limit
        
            item.ThreadLimit = StringToInt(CThreadLimit)
            item.ThreadHWM = StringToInt(CThreadHWM)
            item.PthreadLimit = StringToInt(CPthreadLimit)
            item.PthreadHWM = StringToInt(CPthreadHWM)
            item.Readyq = StringToInt(CReadyq)
            item.Reuselm = StringToInt(CReuselm)
            item.ThreadRatio = float(CThreadRatio)
            item.PthreadRatio = float(CPthreadRatio)

            index = index + 1  

    '''
+___________________________________________________________________________________________________________________________________
0  SYSTEM ENTRY
+  ____________
0  ** SYSTEM TOTALS **
+  ___________________________________________________________
0  AIDS IN CHAIN. . . . . . . . . . . . . . . . . . :        0       GENERIC AIDS IN CHAIN. . . . . . . . . . . . . . :        0
0  ATIS SATISFIED BY CONTENTION LOSERS. . . . . . . :        0       ATIS SATISFIED BY CONTENTION WINNERS . . . . . . :        0
0  TOTAL BIDS SENT. . . . . . . . . . . . . . . . . :        0
   CURRENT BIDS IN PROGRESS . . . . . . . . . . . . :        0
0  TOTAL NUMBER OF ALLOCATES. . . . . . . . . . . . :    15053
   QUEUED ALLOCATES . . . . . . . . . . . . . . . . :        0
   FAILED LINK ALLOCATES. . . . . . . . . . . . . . :        0
   FAILED ALLOCATES DUE TO SESSIONS IN USE. . . . . :        0
0  NUMBER OF QUEUELIMIT ALLOCATES REJECTED. . . . . :        0       NUMBER OF XZIQUE ALLOCATES REJECTED. . . . . . . :        0
   NUMBER OF MAXQTIME ALLOCATE QUEUE PURGES . . . . :        0       NUMBER OF XZIQUE ALLOCATE QUEUE PURGES . . . . . :        0
   NUMBER OF MAXQTIME ALLOCATES PURGED. . . . . . . :        0       NUMBER OF XZIQUE ALLOCATES PURGED. . . . . . . . :        0
0  TERMINAL SHARING REQUESTS - TOTAL. . . . . . . . :        0
     TERMINAL SHARING REQUESTS - CHANNEL. . . . . . :        0
0  FUNCTION SHIPPING REQUESTS
+  __________________________
     FILE CONTROL (FC). . . . . . . . . . . . . . . :        0
     INTERVAL CONTROL (IC) - TOTAL. . . . . . . . . :    15053
       INTERVAL CONTROL (IC) - CHANNEL. . . . . . . :        0
     PROGRAM CONTROL (PC) - TOTAL . . . . . . . . . :        0
       PROGRAM CONTROL (PC) - CHANNEL . . . . . . . :        0
     TRANSIENT DATA (TD). . . . . . . . . . . . . . :        0
     TEMPORARY STORAGE (TS) . . . . . . . . . . . . :        0
     DLI REQUESTS . . . . . . . . . . . . . . . . . :        0
0  BYTES SENT BY TRANSACTION ROUTING REQUESTS . . . :                0
   BYTES RECEIVED BY TRANSACTION ROUTING REQUESTS . :                0
0  BYTES SENT BY PROGRAM CHANNEL REQUESTS . . . . . :                0
   BYTES RECEIVED BY PROGRAM CHANNEL REQUESTS . . . :                0
0  BYTES SENT BY INTERVAL CHANNEL REQUESTS. . . . . :                0
   BYTES RECEIVED BY INTERVAL CHANNEL REQUESTS. . . :                0
    '''
    if "0  SYSTEM ENTRY" in line.upper() and "0  ** SYSTEM TOTALS **" in LineList[index+2].upper():
        CONFlag = 1
        CTotalAlloc = LineList[index+8].split(" ")
        CTotalAlloc = [x for x in CTotalAlloc if x != '' and x != '.']
        CTotalAlloc = CTotalAlloc[6].strip("\r\n")

        CQueuedAlloc = LineList[index+9].split(" ")
        CQueuedAlloc = [x for x in CQueuedAlloc if x != '' and x != '.']
        CQueuedAlloc = CQueuedAlloc[3].strip("\r\n")

        CFailedLinkAlloc = LineList[index+10].split(" ")
        CFailedLinkAlloc = [x for x in CFailedLinkAlloc if x != '' and x != '.']
        CFailedLinkAlloc = CFailedLinkAlloc[4].strip("\r\n")

        CFailedAllocSessUsed = LineList[index+11].split(" ")
        CFailedAllocSessUsed = [x for x in CFailedAllocSessUsed if x != '' and x != '.']
        CFailedAllocSessUsed = CFailedAllocSessUsed[8].strip("\r\n")   

        IntItem.CONItem.TotalAlloc = CTotalAlloc
        IntItem.CONItem.QueuedAlloc = CQueuedAlloc           
        IntItem.CONItem.FailedLinkAlloc = CFailedLinkAlloc            
        IntItem.CONItem.FailedAllocSessUsed = CFailedAllocSessUsed   

        CONRep.AppendItem(IntItem.CONItem)     

        index = index + 32    
            

    index = index + 1   #increase the index for while loop        

fo_input.close()

if IntItem.Applid == '' and len(IntRep.IntervalItemList) == 1:  #This means no real Interval Item is created
    print ("No record is available based on current ApplidPattern")
    exit()



#######################################################################################################################################
# Print the report
#######################################################################################################################################
#Create a subdirecotry as 'RawData' to store extracted data
filesuffix=input_filename.split("/")
filesuffix = [x for x in filesuffix if x != '']
filesuffix=filesuffix[-1]
reportDirName="ReportFile_"+filesuffix
if(os.path.exists(reportDirName)):
   shutil.rmtree(reportDirName)
os.mkdir(reportDirName)
os.mkdir(reportDirName+"//RawData")

copyFiles("css",reportDirName+"/css")
copyFiles("dist",reportDirName+"/dist")
copyFiles("js",reportDirName+"/js")
IntRep.ExtractInfo()

############################################################################################
# Peak Interval Report
############################################################################################
input_peak_template = "Template4PeakInterval.html"

fo_input = open(input_peak_template,"r")
LineList = fo_input.readlines()
HTMLFile = list()
fo_input.close()

for index in range(len(LineList)):
    htmlEntry=LineList[index]
    HTMLFile.append(htmlEntry)
          
if len(HTMLFile) > 1:
    PeakReportName = "CICS_Health_Report_Peak_" + STATISTICS_DATE.replace("/","") + "_" + PEAK_INTERVAL.replace(":","").replace("~","_") + ".html"
    fo_rep = open(reportDirName+"//"+PeakReportName, 'w')
    output = ''
    PeakJSON = IntRep.PeakInfoJSON()
    SummJSON = IntRep.SummInfoJSON()
    TMInsights = IntRep.InsightsTMPeak().replace("\n","<br/>")
    DSInsights = IntRep.InsightsDSPeak().replace("\n","<br/>")
    SMInsights = IntRep.InsightsSMPeak().replace("\n","<br/>")
    TCInsights = IntRep.InsightsTCPeak().replace("\n","<br/>")
    LGInsights = IntRep.InsightsLGPeak().replace("\n","<br/>")
    DBInsights = IntRep.InsightsDBPeak().replace("\n","<br/>")
    NQInsights = IntRep.InsightsNQPeak().replace("\n","<br/>")
    TSQInsights = IntRep.InsightsTSQPeak().replace("\n","<br/>")
    TDQInsights = IntRep.InsightsTDQPeak().replace("\n","<br/>")
    CONInsights = IntRep.InsightsCONPeak().replace("\n","<br/>")
    DUInsights = IntRep.InsightsDUPeak().replace("\n","<br/>")
    ScoreJSON = IntRep.PeakScoreJSON()
    ScoreInsights = IntRep.InsightsScorePeak().replace("\n","<br/>")

    for htmlEntry in HTMLFile:
        if("REPLACEME_INTERVAL" in htmlEntry):
            htmlEntry=htmlEntry.replace('REPLACEME_INTERVAL',STATISTICS_DATE + " " + PEAK_INTERVAL)
        if("REPLACEME_PEAK" in htmlEntry):
            htmlEntry=htmlEntry.replace('REPLACEME_PEAK',PeakJSON)
        if("REPLACEME_SCORE" in htmlEntry):
            htmlEntry=htmlEntry.replace('REPLACEME_SCORE',ScoreJSON)
        if("REPLACEME_SUMMARY" in htmlEntry):
            htmlEntry=htmlEntry.replace('REPLACEME_SUMMARY',SummJSON)
        if("REPLACEME_INSIGHTSTM" in htmlEntry):
            htmlEntry=htmlEntry.replace('REPLACEME_INSIGHTSTM',TMInsights)            
        if("REPLACEME_INSIGHTSDS" in htmlEntry):
            htmlEntry=htmlEntry.replace('REPLACEME_INSIGHTSDS',DSInsights)   
        if("REPLACEME_INSIGHTSSM" in htmlEntry):
            htmlEntry=htmlEntry.replace('REPLACEME_INSIGHTSSM',SMInsights)            
        if("REPLACEME_INSIGHTSTC" in htmlEntry):
            htmlEntry=htmlEntry.replace('REPLACEME_INSIGHTSTC',TCInsights)            
        if("REPLACEME_INSIGHTSLG" in htmlEntry):
            htmlEntry=htmlEntry.replace('REPLACEME_INSIGHTSLG',LGInsights)    
        if("REPLACEME_INSIGHTSDB" in htmlEntry):
            htmlEntry=htmlEntry.replace('REPLACEME_INSIGHTSDB',DBInsights) 
        if("REPLACEME_INSIGHTSNQ" in htmlEntry):
            htmlEntry=htmlEntry.replace('REPLACEME_INSIGHTSNQ',NQInsights) 
        if("REPLACEME_INSIGHTSTSQ" in htmlEntry):
            htmlEntry=htmlEntry.replace('REPLACEME_INSIGHTSTSQ',TSQInsights) 
        if("REPLACEME_INSIGHTSTDQ" in htmlEntry):
            htmlEntry=htmlEntry.replace('REPLACEME_INSIGHTSTDQ',TDQInsights) 
        if("REPLACEME_INSIGHTSCON" in htmlEntry):
            htmlEntry=htmlEntry.replace('REPLACEME_INSIGHTSCON',CONInsights) 
        if("REPLACEME_INSIGHTSDU" in htmlEntry):
            htmlEntry=htmlEntry.replace('REPLACEME_INSIGHTSDU',DUInsights)                     
        if("REPLACEME_INSIGHTSSCORE" in htmlEntry):
            htmlEntry=htmlEntry.replace('REPLACEME_INSIGHTSSCORE',ScoreInsights)   
        if("REPLACEME_ENQUEUE" in htmlEntry):
            htmlEntry=htmlEntry.replace('REPLACEME_ENQUEUE',str(ENQNAME)) 
        if("REPLACEME_TCLASS" in htmlEntry):
            htmlEntry=htmlEntry.replace('REPLACEME_TCLASS',str(TCLASSNAME))             

        output = output + htmlEntry
    fo_rep.write(output)
    fo_rep.close()   

############################################################################################
# Trend Report in case multuple intervals are found
############################################################################################
if IntRep.IntervalNum > 1:
    input_trend_template = "Template4MultiIntervals.html"

    fo_input = open(input_trend_template,"r")
    LineList = fo_input.readlines()
    HTMLFile = list()
    fo_input.close()

    for index in range(len(LineList)):
        htmlEntry=LineList[index]
        HTMLFile.append(htmlEntry)
              
    if len(HTMLFile) > 1:
        PeakReportName = "CICS_Health_Report_Trend_" + STATISTICS_DATE.replace("/","") + ".html"
        fo_rep = open(reportDirName+"//"+PeakReportName, 'w')
        output = ''
        for htmlEntry in HTMLFile:
            if("REPLACEME_DATE" in htmlEntry):
                htmlEntry=htmlEntry.replace('REPLACEME_DATE',STATISTICS_DATE)
            if("REPLACEME_TREND" in htmlEntry):
                htmlEntry=htmlEntry.replace('REPLACEME_TREND',IntRep.TrendInfoJSON())
            if("REPLACEME_SUMMARY" in htmlEntry):
                htmlEntry=htmlEntry.replace('REPLACEME_SUMMARY',IntRep.SummInfoJSON())
            output = output + htmlEntry
        fo_rep.write(output)
        fo_rep.close()   


TMRep.DumpToFile()
DSRep.DumpToFile()
NQRep.DumpToFile()
SMRep.DumpToFile()
LGRep.DumpToFile()
DBRep.DumpToFile()
DURep.DumpToFile()
TSQRep.DumpToFile()
TDQRep.DumpToFile()
CONRep.DumpToFile()
IntRep.InsightsAllInterval()

print("Statistics Analysis completed!")
print("The Report has been generated in " + reportDirName)
exit()



