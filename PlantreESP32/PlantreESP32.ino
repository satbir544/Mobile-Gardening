#include <pthread.h>
#include <SPI.h>
#include <Wire.h>
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>
#include <WiFi.h>
#include <WiFiUdp.h>
#include <String.h>
#include "BluetoothSerial.h"
#include <BlynkSimpleEsp32.h>


#define WATERPUMP 5
#define SECONDWATERPUMP 19
#define SCREEN_WIDTH 128 // OLED display width, in pixels
#define SCREEN_HEIGHT 64 // OLED display height, in pixels
#define OLED_RESET -1 // Reset pin # (or -1 if sharing Arduino reset pin)


#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif

Adafruit_SSD1306 display(SCREEN_WIDTH, SCREEN_HEIGHT, &Wire, OLED_RESET);

const int ledPin = 15;
const int prPin = 26;
int bigValue = 2704;
int smallValue = 790;

BluetoothSerial SerialBT; 
//this is for first sensor
const int AirValue = 2580;   //you need to replace this value with Value_1
const int WaterValue = 1070;  //you need to replace this value with Value_2
int soilMoistureValue = 0;
int soilmoisturepercent=100;
const int SensorPin1 = 36;

//this is for second sensor
const int AirValue2 = 3380;   //you need to replace this value with Value_1
const int WaterValue2 = 1370;  //you need to replace this value with Value_2
const int SensorPin2 = 35;
int soilMoistureValue2 = 0;
int soilmoisturepercent2=100;
char auth[] = "ihbYhRnEL8H3lw84v8fyU-CPtH-BJs00";

bool wifiConnected = false;


String allInfo;
String strs[5];
int StringCount = 0;

String ssidStr;
String passwordStr;
String Ip;
String port;
const char* ssid;
const char* password;

const char * udpAddress;
WiFiUDP udp;
unsigned int udpPort;
bool pumpOn;
bool secondPumpOn;
bool lightOn;

char pktbuf[30];
String x_val;
int randNum = 0;

String wateringInfo[6];
String wateringFragmentInfo[2];
int minMoistureValues[2];

bool plant1WateredWithMoisture;
bool plant2WateredWithMoisture;

int timerValue[2];
int savedTimerValue[2];

int calculatedTime[2];

int elapsedTime[2];

int receivedPktCount;

unsigned long Timer1StartTime;
unsigned long Timer2StartTime;

bool WateringMethod1IsTimer;
bool WateringMethod2IsTimer;

int wateringDuration[2];

unsigned long moisture1StartTime;
unsigned long moisture2StartTime;
int count1;
int count2;

int moistureElapsedTime[2];
int moistureEndTime[2];

bool lightRequested;
//--------------------------------------------------------------------------------------------------------------------------

 
void setup() {

   Serial.begin(115200);
   SerialBT.begin("MyPlantre"); //Bluetooth device name
   Serial.println("The device started, now you can pair it with bluetooth!");
   //display.begin(SSD1306_SWITCHCAPVCC, 0x3C); //initialize with the I2C addr 0x3C (128x64)
   //display.clearDisplay();
   pinMode(WATERPUMP, OUTPUT);
   pinMode(SECONDWATERPUMP, OUTPUT);
   digitalWrite(WATERPUMP, HIGH); 
   digitalWrite(SECONDWATERPUMP, HIGH); 
   pinMode(ledPin,OUTPUT);
   digitalWrite(ledPin, LOW); 

   lightRequested = false;
   lightOn = false;
   plant1WateredWithMoisture = false;
   plant2WateredWithMoisture = false;
   receivedPktCount = 0;
   pumpOn = false;
   secondPumpOn = false;

   Timer1StartTime = 0;
   Timer2StartTime = 0;

   savedTimerValue[0] = 0;
   savedTimerValue[1] = 0;
  
   minMoistureValues[0] = -20;
   minMoistureValues[1] = -20;

   wateringDuration[0] = 0;
   wateringDuration[1] = 0;

   display.begin(SSD1306_SWITCHCAPVCC, 0x3C); //initialize with the I2C addr 0x3C (128x64)
   display.clearDisplay();
   wifiConnected = false;
   //Serial.println("Got it");
   pthread_t threads[3];
   int returnValue;
 
   for( int i = 0; i< 3; i++ ) {
      if(i == 0)
      {
        returnValue = pthread_create(&threads[i], NULL, printThreadId, (void *)i);
      }
      if(i == 1)
      {
        returnValue = pthread_create(&threads[i], NULL, printThreadId1, (void *)i);
      }
      if(i == 2)
      {
        returnValue = pthread_create(&threads[i], NULL, printThreadId2, (void *)i);
      }
      
 
      if (returnValue) {
         Serial.println("An error has occurred");
      }
   }
 
}


void connectToWifi()
{
  if (SerialBT.available()) 
      {
        allInfo =SerialBT.readString();
        //Serial.println(allInfo + " This was the string array\n\n"); 
        
    
        // Split the string into substrings
        while (allInfo.length() > 0)
        {
          int index = allInfo.indexOf('\n');
          if (index == -1) // No space found
          {
            strs[StringCount++] = allInfo;
            break;
          }
          else
          {
            strs[StringCount++] = allInfo.substring(0, index);
            allInfo = allInfo.substring(index+1);
          }
        }
      
        ssid = strs[0].c_str();
        password = strs[1].c_str();
        udpAddress = strs[2].c_str();
        port = strs[3];
        udpPort = strs[3].toInt();
        Serial.println(ssid);
        Serial.println(password);
        Serial.println(udpAddress);
        Serial.println(port);
        
    
        WiFi.begin(ssid,password);
        
        while(WiFi.waitForConnectResult() != WL_CONNECTED)  
        {
          Serial.println("Wrong Credentials given");
          SerialBT.print("1");
          delay(1000);
          ESP.restart();
        }
        wifiConnected = true;
        Serial.println(WiFi.localIP());
        Serial.println("Status: Connected");
        SerialBT.print("0");
        udp.begin(udpPort);
        Serial.println(udpPort);
        Blynk.begin(auth,ssid,password);
      }
      delay(20);
}
 
void loop() 
{
   if(wifiConnected == false)
   {
      connectToWifi();
   }
   else
   {
      Blynk.run();
   }

}

void readSoilMoistureValue()
{
    soilMoistureValue = analogRead(SensorPin1);  //put Sensor insert into soil
    soilmoisturepercent = map(soilMoistureValue, AirValue, WaterValue, 0, 100);
    Serial.println("pin1 percentage:");
    Serial.println(soilmoisturepercent);
    Serial.println("pin1 value:");
    Serial.println(soilMoistureValue);
    
    soilMoistureValue2 = analogRead(SensorPin2);  //put Sensor insert into soil
    soilmoisturepercent2 = map(soilMoistureValue2, AirValue2, WaterValue2, 0, 100);
    Serial.println("pin2 percentage:");
    Serial.println(soilmoisturepercent2);
    Serial.println("pin2 value:");
    Serial.println(soilMoistureValue2);

    
}

void sendUdpPacket()
{
    int rp = udp.parsePacket();
    if(!rp)
    {
        x_val = (String)soilmoisturepercent + "," + soilmoisturepercent2;
        Serial.print("udp_send: ");
        Serial.println(x_val);
        udp.beginPacket(udpAddress, udpPort);
        //udp.write(rx_val);
        udp.print(x_val);
        udp.endPacket();
        delay(1000);
    }
    else
    {
      udp.read(pktbuf,30);
      Serial.print("Packet from " + String(udpAddress)+": ");
      receivedPktCount += 1;
      String packet = pktbuf;
      Serial.println(packet);
      if(pktbuf[0] == '5')
      {
        pumpOn = true;
      }
      else if(pktbuf[0] == '4')
      {
        secondPumpOn = true;
      }
      else if(pktbuf[0] == '6')
      {
        lightRequested = true;
      }
      else
      {
        getAllValues(packet, '\n', 0);
        getAllValues(wateringFragmentInfo[0], ',', 1);
        getAllValues(wateringFragmentInfo[1], ',', 2);
      }

      for(int i = 0; i < 6; i++)
      {
        Serial.println(wateringInfo[i]);
      }
      wateringDuration[0] = wateringInfo[2]. toInt();
      wateringDuration[1] = wateringInfo[5]. toInt();
      
      if(wateringInfo[0].equals("Moisture1"))
      {
        WateringMethod1IsTimer = false;
        minMoistureValues[0] = wateringInfo[1]. toInt();
      }
      else if(wateringInfo[0].equals("Timer1"))
      {       
        timerValue[0] = wateringInfo[1]. toInt();
        triggerTimerForPlant(1);
        WateringMethod1IsTimer = true;
      }
      
      if( wateringInfo[3].equals("Moisture2"))
      {
        WateringMethod2IsTimer = false;
        minMoistureValues[1] = wateringInfo[4]. toInt();
      }
      else if(wateringInfo[3].equals("Timer2"))
      {
        
        timerValue[1] = wateringInfo[4]. toInt();
        triggerTimerForPlant(2);
        WateringMethod2IsTimer = true;
      }
      

      for( int i = 0; i < sizeof(pktbuf);  ++i )
      {
        pktbuf[i] = (char)0;
      }
   
      
      delay(1000);
    }
}

void triggerTimerForPlant(int timerNo)
{
  if((timerNo == 1) && (savedTimerValue[0] != timerValue[0]))
  {
    Serial.println("Timer 1 Has Started For plant 1");
    Timer1StartTime = millis();
    //calculatedTime[0] = timerValue[0] * 60 * 60* 1000;
    calculatedTime[0] = timerValue[0] * 60 * 1000;
    savedTimerValue[0] = timerValue[0];
  }
  if((timerNo == 2) && (savedTimerValue[1] != timerValue[1]))
  {
    Serial.println("Timer 2 Has Started For plant 2");
    Timer2StartTime = millis();
    calculatedTime[1] = timerValue[1] * 60 * 1000;
    savedTimerValue[1] = timerValue[1];
  } 
}

void getAllValues(String str, char c, int workFlow)
{
  int StringCount = 0;
  String strs[3];
  
  while (str.length() > 0)
  {
    int index = str.indexOf(c);
    if (index == -1) // No space found
    {
      strs[StringCount++] = str;
      break;
    }
    else
    {
      strs[StringCount++] = str.substring(0, index);
      str = str.substring(index+1);
    }
  }

  if(workFlow == 0)
  {
    wateringFragmentInfo[0] = strs[0];
    wateringFragmentInfo[1] = strs[1];
  }
  else if(workFlow == 1)
  {
    wateringInfo[0] = strs[0];
    wateringInfo[1] = strs[1];
    wateringInfo[2] = strs[2];
  }
  else if(workFlow == 2)
  {
    wateringInfo[3] = strs[0];
    wateringInfo[4] = strs[1];
    wateringInfo[5] = strs[2];
  }
}



void *printThreadId2(void *threadid) {
   
   while(true)
   {
     

     if(WateringMethod1IsTimer == false)
     {
         if((plant1WateredWithMoisture == false) && (soilmoisturepercent < minMoistureValues[0]) && (receivedPktCount > 1))
         {
           Serial.println("Watering Plant 1");
           plant1WateredWithMoisture = true;
           runWaterPump(WATERPUMP,wateringDuration[0]);
           
         }
     }
     else if (WateringMethod1IsTimer == true) 
     {
        if(Timer1StartTime != 0)
        {
          unsigned long currentTime = millis();
          elapsedTime[0] = (int)currentTime - (int)Timer1StartTime;
          if(elapsedTime[0] >= calculatedTime[0])
          {
            Serial.println("1st plant watering with timer");
            Timer1StartTime = millis();
            runWaterPump(WATERPUMP,wateringDuration[0]);
            elapsedTime[0] = 0;           
          }
        }
     }

     if(WateringMethod2IsTimer == false)
     {
       if((plant2WateredWithMoisture == false) && (soilmoisturepercent2 < minMoistureValues[1])&& (receivedPktCount > 1))
       {
         Serial.println("Watering Plant 2");
         plant2WateredWithMoisture = true;
         runWaterPump(SECONDWATERPUMP,wateringDuration[1]);
         
       }
     }
     else if (WateringMethod2IsTimer == true) 
     {
        if(Timer2StartTime != 0)
        {
          int currentTime = millis();
          elapsedTime[1] = currentTime - Timer2StartTime;
          if(elapsedTime[1] >= calculatedTime[1])
          {
            Serial.println("2nd plant watering with timer");
            Timer2StartTime = millis();
            runWaterPump(SECONDWATERPUMP,wateringDuration[1]);
            elapsedTime[1] = 0;           
          }
        }
     }


     if(plant1WateredWithMoisture) 
     {
       Serial.println("Waiting 1 hour for 1st plant");
       if(count1 == 0)
       {
         moisture1StartTime = millis();
         moistureEndTime[0] = 2*60*1000;
       }
       count1 += 1;
       unsigned long curTime =  millis();
       moistureElapsedTime[0] = (int)curTime - (int)moisture1StartTime;
       if(moistureElapsedTime[0] >= moistureEndTime[0])
       {
         plant1WateredWithMoisture = false;
         count1 = 0;
       }
       
     }
     
     if(plant2WateredWithMoisture)
     {
       Serial.println("Waiting 1 hour for 2nd plant");
       if(count2 == 0)
       {
         moisture2StartTime = millis();
         moistureEndTime[1] = 2*60*1000;
       }
       count2 += 1;
       unsigned long curTime =  millis();
       moistureElapsedTime[1] = (int)curTime - (int)moisture2StartTime;
       if(moistureElapsedTime[1] >= moistureEndTime[1])
       {
         plant2WateredWithMoisture = false;
         count2 = 0;
       }
     }
     delay(1000);

   }
   return NULL;
}

void *printThreadId1(void *threadid) {
   
   while(true)
   {    
     int i = 0;
     if(pumpOn == true)
     {
       runDefaultWaterPump(WATERPUMP);
     }
     else if(secondPumpOn == true)   
     {
       runDefaultWaterPump(SECONDWATERPUMP);
     }
     else if(lightRequested)
     {
       runUVLight();
       lightRequested = false;
     }
     else
     {
       delay(1000);
     }
   }
   return NULL;
}

void *printThreadId(void *threadid) {
   
   while(true)
   {
     int i = 0;
     
     if(wifiConnected == true)
     {
       readSoilMoistureValue();
       sendUdpPacket();
       showInOLED();
     }
     else
     {
       delay(1000);
     }
   }                     // wait for a second
   return NULL;
}

void runWaterPump(int waterPumpPin, int wateringTime)
{
  int wTime = wateringTime * 1000;
  digitalWrite(waterPumpPin, LOW);   // turn the LED on (HIGH is the voltage level)
  delay(wTime);                       // wait for a second
  digitalWrite(waterPumpPin, HIGH);    // turn the LED off by making the voltage LOW
  pumpOn = false;
  secondPumpOn = false;
}

void runDefaultWaterPump(int waterPumpPin)
{
  digitalWrite(waterPumpPin, LOW);   // turn the LED on (HIGH is the voltage level)
  delay(30000);                       // wait for 30 seconds
  digitalWrite(waterPumpPin, HIGH);    // turn the LED off by making the voltage LOW
  
  pumpOn = false;
  secondPumpOn = false;
}

void runUVLight()
{
  if(lightOn == false)
  {
    digitalWrite(ledPin, HIGH);
    lightOn = true;
  }
  else
  {
    digitalWrite(ledPin, LOW);
    lightOn = false;
  }
}

void showInOLED()
{
    if(soilmoisturepercent > 100)
    {
      Serial.println("100 %");
      
      display.setCursor(45,0);  //oled display
      display.setTextSize(2);
      display.setTextColor(WHITE);
      display.println("Soil");
      display.setCursor(20,15);  
      display.setTextSize(2);
      display.setTextColor(WHITE);
      display.println("Moisture");
      
      display.setCursor(30,40);  //oled display
      display.setTextSize(3);
      display.setTextColor(WHITE);
      display.println("100 %");
      display.display();
      
      delay(250);
      display.clearDisplay();
    }
    else if(soilmoisturepercent <0)
    {
      Serial.println("0 %");
      
      display.setCursor(45,0);  //oled display
      display.setTextSize(2);
      display.setTextColor(WHITE);
      display.println("Soil");
      display.setCursor(20,15);  
      display.setTextSize(2);
      display.setTextColor(WHITE);
      display.println("Moisture");
      
      display.setCursor(30,40);  //oled display
      display.setTextSize(3);
      display.setTextColor(WHITE);
      display.println("0 %");
      display.display();
     
      delay(250);
      display.clearDisplay();
    }
    else if(soilmoisturepercent >=0 && soilmoisturepercent <= 100)
    {
      Serial.print(soilmoisturepercent);
      Serial.println("%");
      
      display.setCursor(45,0);  //oled display
      display.setTextSize(2);
      display.setTextColor(WHITE);
      display.println("Soil");
      display.setCursor(20,15);  
      display.setTextSize(2);
      display.setTextColor(WHITE);
      display.println("Moisture");
      
      display.setCursor(30,40);  //oled display
      display.setTextSize(3);
      display.setTextColor(WHITE);
      display.println(soilmoisturepercent);
      display.setCursor(70,40);
      display.setTextSize(3);
      display.println(" %");
      display.display();
     
      delay(250);
      display.clearDisplay();
    }  
}

BLYNK_WRITE(V1)
{
  int pinValue = param.asInt();
  runDefaultWaterPump(WATERPUMP);
}

BLYNK_WRITE(V2)
{
  int pinValue = param.asInt();
  runDefaultWaterPump(SECONDWATERPUMP);
}

BLYNK_WRITE(V3)
{
  int pinValue = param.asInt();
  runUVLight();
}
