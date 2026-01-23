# How to get Exchange Information from a Market Price Domain using EMA Java

## Overview

**Last Updated**: January 2026

For some instruments, the suffix of RIC indicates the exchange where it is traded, e.g. HSBA.L (.L = LSE - London Stock Exchange), HBC.N (.N = NYSE - New York Stock Exchange). However, there are some instruments without the exchange code suffix. How does the application know the exchange of those instruments?

## Solution

The Real-Time Platform provides a short exchange name information via the FID 1709 (Field name RDN_EXCHD2) for the Market Price domain. This FID 1709 is the enumeration field that is used in most of the exchanges. The Enter Price Message API (EMA) of the [Real-Time SDK Java](https://developers.lseg.com/en/api-catalog/real-time-opnsrc/rt-sdk-java) (formerly known as ESDK) and above can retrieve an enum value from dictionary directly, so it can help a consumer application get a short exchange name from this enumeration field..

## IMPORTANT Rebranding Announcement: 

Starting with version RTSDK 2.0.0.L1 (same as EMA/ETA 3.6.0.L1), there are namespace changes and library name changes. Please note that all interfaces remain the same as prior releases of RTSDK and Elektron SDK and will remain fully wire compatible. Along with RTSDK 2.X version, a [REBRAND.md](https://github.com/Refinitiv/Real-Time-SDK/blob/master/REBRAND.md) is published to detail impact to existing applications and how to quickly adapt to the re-branded libraries. Existing applications will continue to work indefinitely as-is.  Applications should be proactively rebranded to be able to utilize new features, security updates or fixes post 2.X release. Please see [PCN](https://my.refinitiv.com/content/mytr/en/pcnpage/12072.html?_ga=2.103280071.632863608.1606731450-325683966.1598503157) for more details on support. 


#### RDMFieldDictionary

```ini
RDN_EXCHD2 "EXCHANGE ID 2"       1709  NULL        ENUMERATED    5 ( 3 )  ENUM             2
```

#### enumtype.def

```ini
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
1. Supported Java versions.
2. [Apache Maven](https://maven.apache.org/) project management tool.
3. Internet connection. 
4. The Real-Time Platform connection. 

*Note:* 
Please check for the supported Java versions from the [API Compatibility Matrix](https://developers.lseg.com/en/api-catalog/real-time-opnsrc/rt-sdk-java/documentation#api-compatibility-matrix) page.

The Real-Time SDK Java is now available in [Maven Central Repository](https://search.maven.org/). This demo application uses Apache IVY to download the EMA Java and its dependencies jars file for the application.

The example code has been tested with Real-Time SDK Java version 2.3.2L1 (EMA Java ```3.9.2.0``` on the Maven Central).

## Running the application

You can get it via the following git command

```bash
$>git clone git@github.com:LSEG-API-Samples/Article.EMA.Java.ExchangeShortName.git
```
Note: The application works with EMA Java 3.1.0 (Real-Time SDK 1.1.0) and above which supports the enum parsing only.

You can build the application and run it via the following steps

1. Configure the Channel_1 of EmaConfig.xml file to specify the host name and RSSL Port of the server (Real-Time Advanced Distribution
Server) to which the EMA connects. This is for setting values of the <ChannelGroup><ChannelList><Channel><Host> node. This value can be a remote host name or IP address.

      ```xml
      <Channel>
            <Name value="Channel_1"/>								
            <ChannelType value="ChannelType::RSSL_SOCKET"/>													
            <CompressionType value="CompressionType::None"/>
            <GuaranteedOutputBuffers value="5000"/>
            <ConnectionPingTimeout value="30000"/>
            <TcpNodelay value="1"/>

            <Host value="[Your Real-Time Advanced Distribution Server HOST]"/>
            <Port value="[Your Real-Time Advanced Distribution Server RSSL Port]"/>
      </Channel>
      ```

3. You can change the requested service and RIC names in the following line of code to match your environment

      ```java
      //Batch
      OmmArray batch_array = EmaFactory.createOmmArray();
      // Add interested RICs
      batch_array.add(EmaFactory.createOmmArrayEntry().ascii("<RIC name 1>"));
      batch_array.add(EmaFactory.createOmmArrayEntry().ascii("<RIC name 2>"));
      ...
      //Send a snapshot request message to the Real-Time Platform endpoint with Batch View request in the payload
      consumer.registerClient(EmaFactory.createReqMsg()
                        .serviceName("<SERVICE_NAME>")
                        .interestAfterRefresh(false)
                        .payload(batchView)
                  , appClient);
      ```
4. Run the following command to build, compile, and create a FAT-jar file. All files will be available in folder named **target**.

      ```bash
      $>mvn clean compile package
      ```
5. Stay in the same location, run the application with the following command

      ```bash
      java -jar .\target\EMA_ExchangeName-1.0-SNAPSHOT-jar-with-dependencies.jar
      ```
6. The example output when you run the application for each item name:

      ```bash
      Item Name: PTT.BK
      Service Name: ELEKTRON_DD
      Item State: Non-streaming / Ok / None / '*All is well'
      Fid: 3 Name = DSPLY_NAME DataType: Rmtes Value: PTT
      Fid: 22 Name = BID DataType: Real Value: 33.5
      Fid: 25 Name = ASK DataType: Real Value: 33.75
      Fid: 1709 Name = RDN_EXCHD2 DataType: Enum Value: SET //[Wasin explanation]<-- FID value: 158 "SET" The Stock Exchange of Thailand

      Item Name: LSEG.L
      Service Name: ELEKTRON_DD
      Item State: Non-streaming / Ok / None / '*All is well'
      Fid: 3 Name = DSPLY_NAME DataType: Rmtes Value: LON.STK.EXCH
      Fid: 22 Name = BID DataType: Real Value:  blank
      Fid: 25 Name = ASK DataType: Real Value:  blank
      Fid: 1709 Name = RDN_EXCHD2 DataType: Enum Value: LSE //[Wasin explanation]<-- FID value: 64 "LSE" London Stock Exchange

      Item Name: 2330.TW
      Service Name: ELEKTRON_DD
      Item State: Non-streaming / Ok / None / '*All is well'
      Fid: 3 Name = DSPLY_NAME DataType: Rmtes Value: TAIWAN SEMICONT
      Fid: 22 Name = BID DataType: Real Value: 1765.0
      Fid: 25 Name = ASK DataType: Real Value: 1770.0
      Fid: 1709 Name = RDN_EXCHD2 DataType: Enum Value: TAI //[Wasin explanation]<-- FID value: 175 "TAI"   Taiwan Stock Exchange

      Item Name: NVDA.O
      Service Name: ELEKTRON_DD
      Item State: Non-streaming / Ok / None / '*All is well'
      Fid: 3 Name = DSPLY_NAME DataType: Rmtes Value: NVIDIA CORP
      Fid: 22 Name = BID DataType: Real Value: 184.33
      Fid: 25 Name = ASK DataType: Real Value: 184.37
      Fid: 1709 Name = RDN_EXCHD2 DataType: Enum Value: NSQ //[Wasin explanation]<-- FID value: 646 "NSQ" Consolidated Issue Listed on Nasdaq Global Select Market
      ```

## Conclusion

If your application subscribes to data from the Real-Time Platform and you need an exchange information, you can get it from the FID 1709 (RDN_EXCHD2). The EMA Java API (version 3.1.0 and above) can help an application consume and parse this FID to get an exchange short name with only a few lines of code.

## References
For further details, please check out the following resources:

* [Real-Time SDK Java page](https://developers.lseg.com/en/api-catalog/real-time-opnsrc/rt-sdk-java) on the [LSEG Developers Community](https://developers.lseg.com/) website.
* [Enterprise Message API Java Quick Start](https://developers.lseg.com/en/api-catalog/real-time-opnsrc/rt-sdk-java/quick-start)
* [Developer Webinar: Introduction to Enterprise App Creation With Open-Source Enterprise Message API](https://www.youtube.com/watch?v=2pyhYmgHxlU)
* [Real-Time SDK Family](https://developers.lseg.com/en/use-cases-catalog/real-time) page.
* [Enterprise Message API Java Quick Start](https://developers.lseg.com/en/api-catalog/real-time-opnsrc/rt-sdk-javaquick-start)
* [Developer Article: 10 important things you need to know before you write an Enterprise Real Time application](https://developers.lseg.com/en/article-catalog/article/10-important-things-you-need-know-you-write-elektron-real-time-application) article.
* [Enterprise Messaging API (Java) - Batch and View features](https://developers.lseg.com/en/article-catalog/article/enterprise-messaging-api-java-batch-and-view-features) article.

For any question related to this article or Enterprise Message API page, please use the Developer Community [Q&A Forum](https://community.developers.refinitiv.com).
