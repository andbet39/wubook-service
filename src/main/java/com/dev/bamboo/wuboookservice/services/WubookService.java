package com.dev.bamboo.wuboookservice.services;

import com.dev.bamboo.wuboookservice.domains.Room;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

@Service
public class WubookService {

    private String currentToken;

    private static String PASSWORD="Giada123";
    private static String TOKEN="bamboo:rome";
    private static String USERNAME="AT035";
    private static String LCODE="1377875938";
    private static String URL="https://wired.wubook.net/xrws/";


    public String getCurrentToken() {
        if (currentToken == null || currentToken.isEmpty()){
            currentToken = this.acquireToken();
        }
        return currentToken;
    }

    public void setCurrentToken(String currentToken) {
        this.currentToken = currentToken;
    }


    public ArrayList<Room> fetchRoom(){

        ArrayList<Room> roomList = new ArrayList<>();

        try {

            XmlRpcClientConfigImpl xmlConfig = new XmlRpcClientConfigImpl();
            xmlConfig.setServerURL(new URL(URL));
            XmlRpcClient client = new XmlRpcClient();

            client.setConfig(xmlConfig);

            Vector params = new Vector();

            params.addElement(this.getCurrentToken());
            params.addElement(LCODE);

            Object[] result = (Object[]) client.execute("fetch_rooms", params);

            Object[] rooms = (Object[]) result[1];

            for(Object r_object : rooms){

                ObjectMapper mapper = new ObjectMapper();
                try{
                    Room room = mapper.convertValue(r_object, Room.class);
                    roomList.add(room);

                }catch (Exception e){
                    System.err.println("JavaClient: " + e);

                }
            }

        }catch (Exception e){
            System.err.println("JavaClient: " + e);
        }

        return  roomList;

    }



    private String acquireToken(){
        try {

            XmlRpcClientConfigImpl xmlConfig = new XmlRpcClientConfigImpl();
            xmlConfig.setServerURL(new URL(URL));
            XmlRpcClient client = new XmlRpcClient();

            client.setConfig(xmlConfig);

            Vector params = new Vector();

            params.addElement(USERNAME);
            params.addElement(PASSWORD);
            params.addElement(TOKEN);

            Object[] result = (Object[]) client.execute("acquire_token", params);

            return result[1].toString();

        } catch (Exception exception) {
            System.err.println("JavaClient: " + exception);
        }

        return null;
    }


    public Object[]  getPricingPlan(){

        try{
            XmlRpcClientConfigImpl xmlConfig = new XmlRpcClientConfigImpl();
            xmlConfig.setServerURL(new URL(URL));
            XmlRpcClient client = new XmlRpcClient();

            client.setConfig(xmlConfig);

            Vector params = new Vector();


            params.addElement(this.getCurrentToken());
            params.addElement(LCODE);


            Object[] result = (Object[]) client.execute("get_pricing_plans", params);

            return result;

        } catch (Exception exception) {
            System.err.println("JavaClient: " + exception);
        }

        return null;
     }

    public Double getPrice(String date,Long roomId){
        try {

            XmlRpcClientConfigImpl xmlConfig = new XmlRpcClientConfigImpl();
            xmlConfig.setServerURL(new URL(URL));
            XmlRpcClient client = new XmlRpcClient();

            client.setConfig(xmlConfig);

            Vector params = new Vector();
            //fetch_plan_prices(token, lcode, pid, dfrom, dto[, rooms= []])


            params.addElement(this.getCurrentToken());
            params.addElement(LCODE);
            params.addElement(9845);
            params.addElement(date);
            params.addElement(date);

            Object[] result = (Object[]) client.execute("fetch_plan_prices", params);

            HashMap<String,Object[]> prices = (HashMap)result[1];

            if ( prices.get(roomId.toString()).length>0){
                return (Double) prices.get(roomId.toString())[0];
            }

            return  0.0;

        } catch (Exception exception) {
            System.err.println("JavaClient: " + exception);
        }

        return null;

    }

    public void updatePrice(String st_date,Double price,Long roomId){
        try {

            XmlRpcClientConfigImpl xmlConfig = new XmlRpcClientConfigImpl();
            xmlConfig.setServerURL(new URL(URL));
            XmlRpcClient client = new XmlRpcClient();

            client.setConfig(xmlConfig);

            Vector params = new Vector();
           // update_plan_prices(token, lcode, pid, dfrom, prices)¶


            params.addElement(this.getCurrentToken());
            params.addElement(LCODE);
            params.addElement(9845);
            params.addElement(st_date);

            HashMap<String,Double[]> room_prices = new HashMap<>();

            Double[] prices = {price};
            room_prices.put(roomId.toString(),prices);


            params.addElement(room_prices);

            Object[] result = (Object[]) client.execute("update_plan_prices", params);

            System.out.println(result);

        } catch (Exception exception) {
            System.err.println("JavaClient: " + exception);
        }

    }

}
