package com.thomsonreuters.platformservices.article;

import com.thomsonreuters.ema.access.Msg;
import com.thomsonreuters.ema.access.AckMsg;
import com.thomsonreuters.ema.access.GenericMsg;
import com.thomsonreuters.ema.access.OmmArray;
import com.thomsonreuters.ema.access.RefreshMsg;
import com.thomsonreuters.ema.access.StatusMsg;
import com.thomsonreuters.ema.access.UpdateMsg;
import com.thomsonreuters.ema.access.Data;
import com.thomsonreuters.ema.access.DataType;
import com.thomsonreuters.ema.access.DataType.DataTypes;
import com.thomsonreuters.ema.access.EmaFactory;
import com.thomsonreuters.ema.access.FieldEntry;
import com.thomsonreuters.ema.access.FieldList;
import com.thomsonreuters.ema.access.OmmConsumer;
import com.thomsonreuters.ema.access.OmmConsumerClient;
import com.thomsonreuters.ema.access.OmmConsumerEvent;
import com.thomsonreuters.ema.access.OmmException;
import com.thomsonreuters.ema.access.ElementList;
import com.thomsonreuters.ema.rdm.EmaRdm;

/**
 * Created by Wasin Waeosri on 5/2/2017.
 */

class AppClient implements OmmConsumerClient {
    //A callback function to receive REFRESH message
    public void onRefreshMsg(RefreshMsg refreshMsg, OmmConsumerEvent event) {
        System.out.println("Item Name: " + (refreshMsg.hasName() ? refreshMsg.name() : "<not set>"));
        System.out.println("Service Name: " + (refreshMsg.hasServiceName() ? refreshMsg.serviceName() : "<not set>"));

        System.out.println("Item State: " + refreshMsg.state());
        //Parsing incoming FieldList data
        if (DataType.DataTypes.FIELD_LIST == refreshMsg.payload().dataType())
            decode(refreshMsg.payload().fieldList());

        System.out.println();
    }

    //A callback function to receive UPDATE messages
    public void onUpdateMsg(UpdateMsg updateMsg, OmmConsumerEvent event) {
        System.out.println("Item Name: " + (updateMsg.hasName() ? updateMsg.name() : "<not set>"));
        System.out.println("Service Name: " + (updateMsg.hasServiceName() ? updateMsg.serviceName() : "<not set>"));

        //Parsing incoming FieldList data
        if (DataType.DataTypes.FIELD_LIST == updateMsg.payload().dataType())
            decode(updateMsg.payload().fieldList());

        System.out.println();
    }

    //A callback function to receive STATUS messages
    public void onStatusMsg(StatusMsg statusMsg, OmmConsumerEvent event) {
        System.out.println("Item Name: " + (statusMsg.hasName() ? statusMsg.name() : "<not set>"));
        System.out.println("Service Name: " + (statusMsg.hasServiceName() ? statusMsg.serviceName() : "<not set>"));

        if (statusMsg.hasState())
            System.out.println("Item State: " + statusMsg.state());

        System.out.println();
    }

    public void onGenericMsg(GenericMsg genericMsg, OmmConsumerEvent consumerEvent) {
    }

    public void onAckMsg(AckMsg ackMsg, OmmConsumerEvent consumerEvent) {
    }

    public void onAllMsg(Msg msg, OmmConsumerEvent consumerEvent) {
    }

    //Decode incoming data FieldList object
    void decode(FieldList fieldList) {
        //Iterates each FieldEntry object data
        for (FieldEntry fieldEntry : fieldList) {
            System.out.print("Fid: " + fieldEntry.fieldId() + " Name = " + fieldEntry.name() + " DataType: " + DataType.asString(fieldEntry.load().dataType()) + " Value: ");

            if (Data.DataCode.BLANK == fieldEntry.code())
                System.out.println(" blank");
            else
                switch (fieldEntry.loadType()) {
                    case DataTypes.REAL:
                        //System.out.println(fieldEntry.real().asDouble());
                        System.out.println(fieldEntry.real().asDouble());
                        break;
                    case DataTypes.DATE:
                        System.out.println(fieldEntry.date().day() + " / " + fieldEntry.date().month() + " / " + fieldEntry.date().year());
                        break;
                    case DataTypes.TIME:
                        System.out.println(fieldEntry.time().hour() + ":" + fieldEntry.time().minute() + ":" + fieldEntry.time().second() + ":" + fieldEntry.time().millisecond());
                        break;
                    case DataTypes.INT:
                        System.out.println(fieldEntry.intValue());
                        break;
                    case DataTypes.UINT:
                        System.out.println(fieldEntry.uintValue());
                        break;
                    case DataTypes.ASCII:
                        System.out.println(fieldEntry.ascii());
                        break;
                    case DataTypes.ENUM: //Parsing ENUM data for RDN_EXCHD2
                        System.out.println(fieldEntry.hasEnumDisplay() ? fieldEntry.enumDisplay() : fieldEntry.enumValue());
                        break;
                    case DataTypes.RMTES: //Parsing RMTES data for DSPLY_NAME field
                        System.out.println(fieldEntry.rmtes());
                        break;
                    case DataTypes.ERROR:
                        System.out.println("(" + fieldEntry.error().errorCodeAsString() + ")");
                        break;
                    default:
                        System.out.println();
                        break;
                }
        }
    }
}

public class ExchangeName {
    public static void main(String[] args) {
        OmmConsumer consumer = null;
        try {
            AppClient appClient = new AppClient();
            //Initialize OmmConsumer object. Use the EmaConfig.xml's Consumer_1 configuration to assign ADS IP and RSSL Port
            consumer = EmaFactory.createOmmConsumer(EmaFactory.createOmmConsumerConfig().consumerName("Consumer_1"));

            //View
            ElementList view = EmaFactory.createElementList();
            OmmArray view_array = EmaFactory.createOmmArray();

            view_array.fixedWidth(2);
            //Add interested FIDs
            view_array.add(EmaFactory.createOmmArrayEntry().intValue(3)); //DSPLY_NAME
            view_array.add(EmaFactory.createOmmArrayEntry().intValue(22)); //BID
            view_array.add(EmaFactory.createOmmArrayEntry().intValue(25)); //ASK
            view_array.add(EmaFactory.createOmmArrayEntry().intValue(1709)); //RDN_EXCHD2


            view.add(EmaFactory.createElementEntry().uintValue(EmaRdm.ENAME_VIEW_TYPE, 1));
            view.add(EmaFactory.createElementEntry().array(EmaRdm.ENAME_VIEW_DATA, view_array));
            //Send a request message to Elektron with View request in the payload
            consumer.registerClient(EmaFactory.createReqMsg().serviceName("ELEKTRON_DD").name("TWXF2Z7").payload(view), appClient);

            Thread.sleep(60000);            // API calls onRefreshMsg(), onUpdateMsg() and onStatusMsg()
        } catch (InterruptedException | OmmException excp) {
            System.out.println(excp.getMessage());
        } finally {
            if (consumer != null) consumer.uninitialize();
        }
    }
}
