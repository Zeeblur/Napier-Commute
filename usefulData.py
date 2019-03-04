import csv

file_reader= open('summarisedTravelData.csv', "rt")
read = csv.reader(file_reader)

dataList = []
for row in read :
      dataList.append(row)

# print header
print (dataList[0])

#create staff only list
ALLstaffList = []
for mem in dataList :
      if mem[1] == 'TRUE' and mem[9]:
            ALLstaffList.append(mem)

print ("Total Staff ANSWERED: {}".format(len(ALLstaffList)))

# MERCHISTON STAFF ONLY
staffList = []
for mem in ALLstaffList :
      if "Merchiston" in mem[22]:
            staffList.append(mem)
            
print ("Total Sighthill Staff: {}".format(len(staffList)))

#check how many people where travel mode and pref are equal
equalList = []
for member in staffList :
      if member[9] == member[11] :
            equalList.append(member)

print ("Staff who travel in prefered format: {}".format(len(equalList)))

# figure out most popular reasons for entire
newReasons = []
for member in staffList :
      newReasons.append(member[10])

reason_dict = {i:newReasons.count(i) for i in newReasons}

print (reason_dict)

# most popular for people who do the same thing as preferred
goodReasons = []
for member in equalList :
      goodReasons.append(member[10])

greasons = {i:goodReasons.count(i) for i in goodReasons}

print (greasons)

# postcodes matching of data
# load in data from problem set
file_reader= open('NapierBaseLine-Sight.csv', "rt")
read = csv.reader(file_reader)

import re

# find first line of actual data
def isStartDigit(string) :
      m = re.match('^\d{2}', string)
      
      if m :
            return True
      else :
            return False
      
newProblem = []
dataList = []
for row in read :
      if isStartDigit(row[0]):
            dataList.append(row)
      else :
            newProblem.append(row)


for x in range(0, len(dataList)) :
      for staff in equalList :
            if dataList[x][1] == staff[23] :
                  print ("found match at {} for {}".format(x, staff[9]))
                  if "Car" in staff[9] :
                        dataList[x][9] = "Car"
                  elif "Bus" in staff[9] :
                        dataList[x][9] = "Bus"
                  elif "Train" in staff[9] :
                        dataList[x][9] = "Rail"
                  elif "Foot" in staff[9] :
                        dataList[x][9] = "Walk"
                  elif "Bicycle" in staff[9] :
                        dataList[x][9] = "Cycle"
                  elif "Tram" in staff[9] :
                        dataList[x][9] = "Bus"    
                  break
            

# write new problem set
myFile = open('problemSetSightMatch.csv', 'w', newline="")  
with myFile:  
   writer = csv.writer(myFile)
   writer.writerows(newProblem)
   writer.writerows(dataList)


print ("file copied")

# for preferenced.
for x in range(0, len(dataList)) :
      for staff in staffList :
            if dataList[x][1] == staff[23] :
                  print ("found match at {} for {}".format(x, staff[9]))
                  string = ""
                  if "Car" in staff[9] :
                        string = "Car"
                  elif "Bus" in staff[9] :
                        string = "Bus"
                  elif "Tram" in staff[9] :
                        string = "Bus"
                  elif "Train" in staff[9] :
                        string = "Rail"
                  elif "Foot" in staff[9] :
                        string = "Walk"
                  elif "Bicycle" in staff[9] :
                        string = "Cycle"
                  #also add the preferred
                  if "Car" in staff[11] :
                        if "Car" not in string :
                              string = string + "_Car"
                  elif "Bus" in staff[11] :
                        if "Bus" not in string :
                              string = string + "_Bus"
                  elif "Tram" in staff[11] :
                        if "Bus" not in string :
                              string = string + "_Bus"
                  elif "Train" in staff[11] :
                        if "Rail" not in string :
                              string = string + "_Rail"
                  elif "Foot" in staff[11] :
                        if "Walk" not in string :
                              string = string + "_Walk"
                  elif "Bicycle" in staff[11] :
                        if "Cycle" not in string :
                              string = string +"_Cycle"
                  dataList[x][9] = string

                  break
            

# write new problem set
myFile = open('problemSetSightPref.csv', 'w', newline="")  
with myFile:  
   writer = csv.writer(myFile)
   writer.writerows(newProblem)
   writer.writerows(dataList)


