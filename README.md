# Article-EMA-Java-ExchangeShortName
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

