# Article.EMA.Java.ExchangeShortName
## Overview
For some instruments, suffix of RIC indicates the exchange where it is traded, e.g. HSBA.L (.L = LSE - London Stock Exchange), HBC.N (.N = NYSE - New York Stock Exchange). However, there are some instruments without the exchange code suffix like TWXF2Z7 (Time Warner DEC7), KC4F5Z7 (Kone Corp DEC7), etc. How the application know the exchange of those instruments?

## Solution
The [Elektron API 3.1.0](https://developers.thomsonreuters.com/elektron/elektron-sdk-java) and above can retrieve a short exchange name information from the FID 1709 (Field name RDN_EXCHD2) for the Market Price domain. This FID 1709 is the enumeration field that used in the most of the exchanges.

#### RDMFieldDictionary
```
RDN_EXCHD2 "EXCHANGE ID 2"       1709  NULL        ENUMERATED    5 ( 3 )  ENUM             2
```

#### enumtype.def
```
RDN_EXCHD2  1709
...
! VALUE      DISPLAY   MEANING
! -----      -------   -------
      0        "   "   undefined
      1        "ASE"   NYSE AMEX
      2        "NYS"   New York Stock Exchange
      3        "BOS"   Boston Stock Exchange
      4        "CIN"   National Stock Exchange (formerly Cincinnati Stock Exchange)
      5        "PSE"   NYSE Arca
      6        "XPH"   NASDAQ OMX PSX when trading in SIAC (formerly Philadelphia Stock Exchange)
	...
      1454     "PCW"   PetroChem Wire LLC
      1455     "SMP"   Euronext - Smartpool
      1456     "BT1"   BATS ONE - LEVEL 1 (PRODUCT)
```
## Running the application
You can get it via the following git command
```
$>git clone git@github.com:TR-API-Samples/Article.EMA.Java.ExchangeShortName.git
```
You can build the application and run it via the following steps

1. Copy all required EMA Java libraries to the "libs" folder. The required libraries are following
      - ema.jar (&lt;Elektron SDK Java package&gt;/Ema/Libs)
      - upa.jar (&lt;Elektron SDK Java package&gt;/Eta/Libs)
      - upaValueAdd.jar (&lt;Elektron SDK Java package&gt;/Eta/Libs)
      - commons-configuration-1.10.jar (&lt;Elektron SDK Java package&gt;/Ema/Libs/apache)
      - commons-lang-2.6.jar (&lt;Elektron SDK Java package&gt;/Ema/Libs/apache)
      - commons-logging-1.2.jar (&lt;Elektron SDK Java package&gt;/Ema/Libs/apache)
      - org.apache.commons.collections.jar (&lt;Elektron SDK Java package&gt;/Ema/Libs/apache)
      - slf4j-api-1.7.12.jar (&lt;Elektron SDK Java package&gt;/Ema/Libs/SLF4J/slf4j-1.7.12)
      - slf4j-api-1.7.12.jar (&lt;Elektron SDK Java package&gt;/Ema/Libs/SLF4J/slf4j-1.7.12)
2. Install and configure [Apache ANT](http://ant.apache.org/) in your machine

3. Configure the Channel_1 of EmaConfig.xml file to specify the host name of the server (the TREP or Elektron platform) to which the EMA connects. This is set thanks to the value of the <ChannelGroup><ChannelList><Channel><Host> node. This value can be a remote host name or IP address.

4. You can change the requested service and item name in the following line of code to match your environment
```
consumer.registerClient(EmaFactory.createReqMsg().serviceName("<service>").name("<item name>").payload(view), appClient);
```

5. Build the application with ant command. All application class files will be available at "out" folder
```
$>ant build
```

6. Inside "out" folder, run the application with the following command
```
java -cp .;..\libs\ema.jar;..\libs\upa.jar;..\libs\upaValueAdd.jar;..\libs\org.apache.commons.collections.jar;..\libs\commons-configuration-1.10.jar;..\libs\commons-lang-2.6.jar;..\libs\commons-logging-1.2.jar;..\libs\slf4j-api-1.7.12.jar;..\libs\slf4j-jdk14-1.7.12.jar; com.thomsonreuters.platformservices.article.ExchangeName
```

7. The example output when you run the application for each item name:
```
//TWXF2Z7
Item Name: TWXF2Z7
Service Name: ELEKTRON_DD
Item State: Open / Ok / None / 'All is well'
Fid: 3 Name = DSPLY_NAME DataType: Rmtes Value: Time Warner
Fid: 22 Name = BID DataType: Real Value:  blank
Fid: 25 Name = ASK DataType: Real Value:  blank
Fid: 1709 Name = RDN_EXCHD2 DataType: Enum Value: EUX //FID value: 418        "EUX"   EUREX

//KC4F5Z7
Item Name: KC4F5Z7
Service Name: ELEKTRON_DD
Item State: Open / Ok / None / 'All is well'
Fid: 3 Name = DSPLY_NAME DataType: Rmtes Value: Kone Corp DEC7
Fid: 22 Name = BID DataType: Real Value:  blank
Fid: 25 Name = ASK DataType: Real Value:  blank
Fid: 1709 Name = RDN_EXCHD2 DataType: Enum Value: EUX //FID value: 418        "EUX"   EUREX

//HSBA.L
Item Name: HSBA.L
Service Name: ELEKTRON_DD
Item State: Open / Ok / None / 'All is well'
Fid: 3 Name = DSPLY_NAME DataType: Rmtes Value: HSBC HOLDINGS
Fid: 22 Name = BID DataType: Real Value: 642.8
Fid: 25 Name = ASK DataType: Real Value: 642.9
Fid: 1709 Name = RDN_EXCHD2 DataType: Enum Value: LSE //FID value: 64        "LSE"   London Stock Exchange

```

