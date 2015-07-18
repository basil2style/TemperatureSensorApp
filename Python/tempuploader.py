import time,serial
from parse_rest.connection import register
from parse_rest.datatypes import Object,GeoPoint

APP_ID  = "APP_ID"   # YOUR_APP_ID
REST_ID = "REST_KEY"   # YOUR_REST_KEY

register(APP_ID,REST_ID)                              # Register App

ser = serial.Serial('/dev/ttyACM0',9600,timeout=0) # This is for Linux 

class TempScore(Object):                           # Demo Object 
	pass


interval =10   # time interval 

while 1 :
 try:
  temperature = ser.readline()    # reading temperature valure from PORT
  latitude  = 20.0  	          # example Latitude and Longitude for GeoPoint
  longitude = 77.0 
  print 'Temperature'+temperature+" "    
  gameScore = TempScore(temperatures=temperature)  # saving temperature to temperatures column
  gameScore.location= GeoPoint(latitude,longitude) 
  gameScore.save()                                 # final value commit to Parse
  time.sleep(interval)                             # sleep interval
 except IOError:
  print("Something Went Wrong")                    #if there anything IO error occur


