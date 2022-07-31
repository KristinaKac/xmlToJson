package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.google.gson.Gson;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Main {

    static List<Employee> list = new ArrayList<>();

    public static void main(String[] args) {
        list = parseXML("data.xml");
        String json = listToJson(list);
        writeString(json);
    }
    private static List<Employee> parseXML(String filePath){
        String filepath = "data.xml";
        File xmlFile = new File(filepath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();
            System.out.println("Корневой элемент: " + document.getDocumentElement().getNodeName());
            NodeList nodeList = document.getElementsByTagName("employee");
            for (int i = 0; i < nodeList.getLength(); i++) {
                list.add(getEmployee(nodeList.item(i)));
            }
            for (Employee emp : list) {
                System.out.println(emp.toString());
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return list;
    }
    private static Employee getEmployee(Node node) {
        Employee emp = new Employee();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            emp.setId(Integer.parseInt(getTagValue("id", element)));
            emp.setFirstName(getTagValue("firstName", element));
            emp.setLastName(getTagValue("lastName", element));
            emp.setCountry(getTagValue("country", element));
            emp.setAge(Integer.parseInt(getTagValue("age", element)));
        }
        return emp;
    }
    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }
    private static String listToJson(List<Employee> list){
        String json = new Gson().toJson(list);
        return json;
    }
    private static void writeString(String json){
        try (FileWriter writer = new FileWriter("data.json")){
            writer.write(json);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}