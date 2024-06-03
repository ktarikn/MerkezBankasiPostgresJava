package com.example.isBankasiDbApp;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class DataFetcher {
    public static void main(String[] args) {
        try {
            URL url = new URL("https://www.tcmb.gov.tr/kurlar/today.xml");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(conn.getInputStream());

            NodeList nodeList = document.getElementsByTagName("Currency");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String currencyCode = element.getAttribute("CurrencyCode");
                    String unit = element.getElementsByTagName("Unit").item(0).getTextContent();
                    String currencyName = element.getElementsByTagName("CurrencyName").item(0).getTextContent();
                    String forexBuying = element.getElementsByTagName("ForexBuying").item(0).getTextContent();
                    String forexSelling = element.getElementsByTagName("ForexSelling").item(0).getTextContent();
                    String banknoteBuying = element.getElementsByTagName("BanknoteBuying").item(0).getTextContent();
                    String banknoteSelling = element.getElementsByTagName("BanknoteSelling").item(0).getTextContent();

                    System.out.println("Currency Code: " + currencyCode);
                    System.out.println("Unit: " + unit);
                    System.out.println("Currency Name: " + currencyName);
                    System.out.println("Forex Buying: " + forexBuying);
                    System.out.println("Forex Selling: " + forexSelling);
                    System.out.println("Banknote Buying: " + banknoteBuying);
                    System.out.println("Banknote Selling: " + banknoteSelling);
                    System.out.println("----------------------");
                }
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static Currency[] fetch(){
        ArrayList<Currency> data = new ArrayList<>();
        try {
            URL url = new URL("https://www.tcmb.gov.tr/kurlar/today.xml");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(conn.getInputStream());

            NodeList nodeList = document.getElementsByTagName("Currency");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String currencyCode = element.getAttribute("CurrencyCode");
                    String unit = element.getElementsByTagName("Unit").item(0).getTextContent();
                    String currencyName = element.getElementsByTagName("CurrencyName").item(0).getTextContent();
                    String forexBuying = element.getElementsByTagName("ForexBuying").item(0).getTextContent();
                    String forexSelling = element.getElementsByTagName("ForexSelling").item(0).getTextContent();
                    String banknoteBuying = element.getElementsByTagName("BanknoteBuying").item(0).getTextContent();
                    String banknoteSelling = element.getElementsByTagName("BanknoteSelling").item(0).getTextContent();

                    /*System.out.println("Currency Code: " + currencyCode);
                    System.out.println("Unit: " + unit);
                    System.out.println("Currency Name: " + currencyName);
                    System.out.println("Forex Buying: " + forexBuying);
                    System.out.println("Forex Selling: " + forexSelling);
                    System.out.println("Banknote Buying: " + banknoteBuying);
                    System.out.println("Banknote Selling: " + banknoteSelling);
                    System.out.println("----------------------");*/
                    data.add(new Currency(Float.parseFloat(forexBuying),
                            Float.parseFloat(forexSelling.equals("")?"0":forexSelling),
                            Float.parseFloat(banknoteBuying.equals("")?"0":banknoteBuying),
                            Float.parseFloat(banknoteSelling.equals("")?"0":banknoteSelling),
                            currencyCode));

                }
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data.toArray(new Currency[data.size()]);
    }
}