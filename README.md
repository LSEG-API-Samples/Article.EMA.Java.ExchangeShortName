# How to get Exchange Information from a Market Price Domain using EMA Java

## Overview

For some instruments, the suffix of RIC indicates the exchange where it is traded, e.g. HSBA.L (.L = LSE - London Stock Exchange), HBC.N (.N = NYSE - New York Stock Exchange). However, there are some instruments without the exchange code suffix. How does the application know the exchange of those instruments?

## Solution

Refinitiv Real-Time provides a short exchange name information via the FID 1709 (Field name RDN_EXCHD2) for the Market Price domain. This FID 1709 is the enumeration field that is used in most of the exchanges. The [Refinitiv Real-Time SDK 1.1.0 (Enterprise Message API Java 3.1.0)](https://developers.refinitiv.com/elektron/elektron-sdk-java) (formerly known as Elektron SDK/Elektron Message API) and above can retrieve an enum value from dictionary directly, so it can help a consumer application get a short exchange name from this enumeration field..

## IMPORTANT Rebranding Announcement: 

Starting with version RTSDK 2.0.0.L1 (same as EMA/ETA 3.6.0.L1), there are namespace changes and library name changes. Please note that all interfaces remain the same as prior releases of RTSDK and Elektron SDK and will remain fully wire compatible. Along with RTSDK 2.X version, a [REBRAND.md](https://github.com/Refinitiv/Real-Time-SDK/blob/master/REBRAND.md) is published to detail impact to existing applications and how to quickly adapt to the re-branded libraries. Existing applications will continue to work indefinitely as-is.  Applications should be proactively rebranded to be able to utilize new features, security updates or fixes post 2.X release. Please see [PCN](https://my.refinitiv.com/content/mytr/en/pcnpage/12072.html?_ga=2.103280071.632863608.1606731450-325683966.1598503157) for more details on support. 


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

### Demo prerequisite
This example requires the following dependencies software and libraries.
1. Oracle/Open JDK 8 or Oracle JDK 11.
2. [Apache ANT](http://ant.apache.org/) project management.
3. [Apache IVY](https://ant.apache.org/ivy/) dependency manager tool.
4. Internet connection. 
5. Refinitiv Real-Time connection. 

*Note:* 
The Real-Time SDK Java version 2.0.0 (EMA Java 3.6.0) supports Oracle JDK 1.8 & 1.11, OpenJDK 1.8 & 1.11. If you are using other versions of Real-Time SDK Java, please check the SDK's [README.md](https://github.com/Refinitiv/Elektron-SDK/blob/master/Java/README.md) file regarding the supported Java version.

The Real-Time SDK Java is now available in [Maven Central Repository](https://search.maven.org/). This demo application uses Apache IVY to download the EMA Java and its dependencies jars file for the application.

The example code has been tested with Real-Time SDK Java version 2.0.0 (EMA Java 3.6.0 - ivy.xml dependency: ```<dependency org="com.refinitiv.ema" name="ema" rev="3.6.0.0" />```).

## Running the application
You can get it via the following git command
```
$>git clone git@github.com:Refinitiv-API-Samples/Article.EMA.Java.ExchangeShortName.git
```
Note: The application works with EMA Java 3.1.0 (Real-Time SDK 1.1.0) and above which supports the enum parsing only.

You can build the application and run it via the following steps

1. Install and configure [Apache ANT](http://ant.apache.org/) and [Apache IVY](https://ant.apache.org/ivy/) in your machine
2. Configure the Channel_1 of EmaConfig.xml file to specify the host name and RSSL Port of the server (Refinitiv Real-Time Advanced Distribution
Server) to which the EMA connects. This is for setting values of the <ChannelGroup><ChannelList><Channel><Host> node. This value can be a remote host name or IP address.
      ```
      <Channel>
            <Name value="Channel_1"/>								
            <ChannelType value="ChannelType::RSSL_SOCKET"/>													
            <CompressionType value="CompressionType::None"/>
            <GuaranteedOutputBuffers value="5000"/>
            <ConnectionPingTimeout value="30000"/>
            <TcpNodelay value="1"/>

            <Host value="[Your Refinitiv Real-Time Advanced Distribution Server HOST]"/>
            <Port value="[Your Refinitiv Real-Time Advanced Distribution Server RSSL Port]"/>
      </Channel>
      ```
3. You can change the requested service and item name in the following line of code to match your environment
      ```
      consumer.registerClient(EmaFactory.createReqMsg().serviceName("<service>").name("<item name>").interestAfterRefresh(false).payload(view), appClient);
      ```
4. All application class files will be available in "out" folder, the EmaConfig.xml is also copied to the out folder automatically.
      ```
      $>ant build
      ```
5. Stay in the same location, run the application with the following command
      ```
      java -cp out;lib/* com.refinitiv.platformservices.article.ExchangeName
      ```
6. The example output when you run the application for each item name:
      ```
      //IBM.N
      Item Name: IBM.N
      Service Name: ELEKTRON_DD
      Item State: Non-streaming / Ok / None / 'All is well'
      Fid: 3 Name = DSPLY_NAME DataType: Rmtes Value: INTL BUS MACHINE
      Fid: 22 Name = BID DataType: Real Value: 0.0
      Fid: 25 Name = ASK DataType: Real Value: 0.0
      Fid: 1709 Name = RDN_EXCHD2 DataType: Enum Value: NYS //FID value: 2          "NYS"   New York Stock Exchange

      //HSBA.L
      Item Name: HSBA.L
      Service Name: ELEKTRON_DD
      Item State: Open / Ok / None / 'All is well'
      Fid: 3 Name = DSPLY_NAME DataType: Rmtes Value: HSBC HOLDINGS
      Fid: 22 Name = BID DataType: Real Value: 642.8
      Fid: 25 Name = ASK DataType: Real Value: 642.9
      Fid: 1709 Name = RDN_EXCHD2 DataType: Enum Value: LSE //FID value: 64        "LSE"   London Stock Exchange

      //PTT.BK
      Item Name: PTT.BK
      Service Name: ELEKTRON_DD
      Item State: Non-streaming / Ok / None / 'All is well'
      Fid: 3 Name = DSPLY_NAME DataType: Rmtes Value: PTT
      Fid: 22 Name = BID DataType: Real Value: 35.25
      Fid: 25 Name = ASK DataType: Real Value: 35.5
      Fid: 1709 Name = RDN_EXCHD2 DataType: Enum Value: SET //FID value: 158        "SET"   The Stock Exchange of Thailand
      ```

## Conclusion

If your application subscribes to data from Refinitiv Real-Time Infrastructure and you need an exchange information, you can get it from the FID 1709 (RDN_EXCHD2). The EMA Java API (version 3.1.0 and above) can help an application consume and parse this FID to get an exchange short name with only a few lines of code.

## References
For further details, please check out the following resources:
* [Refinitiv Real-time Java API page](https://developers.refinitiv.com/en/api-catalog/elektron/elektron-sdk-java) on the [Refinitiv Developer Community](https://developers.refinitiv.com/) web site.
* [Enterprise Message API Java Quick Start](https://developers.refinitiv.com/en/api-catalog/elektron/elektron-sdk-java/quick-start)
* [Developer Webinar: Introduction to Enterprise App Creation With Open-Source Enterprise Message API](https://www.youtube.com/watch?v=2pyhYmgHxlU)

For any question related to this article or Enterprise Message API page, please use the Developer Community [Q&A Forum](https://community.developers.refinitiv.com/spaces/72/index.html).
