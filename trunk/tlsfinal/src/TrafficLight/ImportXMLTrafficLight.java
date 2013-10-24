package TrafficLight;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
/*import org.jdom.Document;
 import org.jdom.Element;
 import org.jdom.JDOMException;
 import org.jdom.input.SAXBuilder;
 */
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class ImportXMLTrafficLight {
    
    /*
     */
	public static void main(String[] args) {
		
           
	}
	
	public Map<String, List<List<String>>> From_To = new HashMap<String, List<List<String>>>();;

	public Map<String, List<String>> GetFromToInformation(String Arquivo) {
                System.out.println("Abrindo: "+Arquivo );
                        
                SAXBuilder builder = new SAXBuilder();
		// File xmlFile = new File("Output/teste.xml");
		File xmlFile = new File(Arquivo);
		Map<String, List<String>> Junctions = new HashMap<String, List<String>>();
		Map<String, List<String>> Connections = new HashMap<String, List<String>>();
		
                try {
			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
             
			// Obtém todas as juctions do arquivo de rede
			@SuppressWarnings("rawtypes")
			List list = rootNode.getChildren("junction");
 
			
                        for (int i = 0; i < list.size(); i++) {
				Element node = (Element) list.get(i);
                              
                                if(!node.getAttributeValue("id").toString().substring(0, 1).equals(":")){
                                   Junctions.put(node.getAttributeValue("id"),
						Arrays.asList(node.getAttributeValue("intLanes").split(
								" ")));
                                }
				
			}
                         
			list.clear();

			// Obtém todas as conections do arquivo de rede
			list = rootNode.getChildren("connection");
                        
			for (int i = 0; i < list.size(); i++) {
				Element node = (Element) list.get(i);
				// verifica se existe a definição de uma via
				if (node.getAttributeValue("via") != null) {
					String aux = node.getAttribute("from").toString();
					// Verifica se não eh uma conexão interna gerada
					if (!aux.contains("[Attribute: from=\":")) {       
						Connections.put(node.getAttributeValue("via"), Arrays
								.asList(node.getAttributeValue("from"),
										node.getAttributeValue("to"),node.getAttributeValue("fromLane"),node.getAttributeValue("toLane")));
					}
				}
			}
                                 
                        
// #----------------------- GERA A ESTRUTURA SEMAFORICA FROM/TO ----------------------# 
			System.out.println("Gerando De Para ");

			for( String intLaneJunction : Junctions.keySet()){
				List<List<String>> values = new ArrayList<List<String>>();
				for( String idConexao :  Connections.keySet()){
					 if(Junctions.get(intLaneJunction).contains(idConexao)){
						 values.add(Connections.get(idConexao));
					 }
				}
				From_To.put(intLaneJunction, values);	
			}

			// System.out.println(DePara);
			System.out.println(From_To);
			GetXMLDEPARA();
			// from="1i" to="3o" fromLane="0" toLane="0" via=":0_12_0"

		} catch (IOException io) {
			System.out.println(io.getMessage());
		} catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		}
		return null;
	}
	
	public String GetXMLDEPARA() {
		// <TrafficLight id=" " FromTo=""/>
		String XML_FROM_TO = "";
		for( String Junction :From_To.keySet()){
			XML_FROM_TO = XML_FROM_TO + "\n <TrafficLight id=\""+Junction+"\" FromTo=\"";
			for(List<String> i : From_To.get(Junction)){ 
				XML_FROM_TO = XML_FROM_TO + i.get(0)+" "+i.get(1)+" "+i.get(2)+" "+i.get(3);
			}
			XML_FROM_TO = XML_FROM_TO +"\"/>";
		}
		return XML_FROM_TO;
	}
	
}