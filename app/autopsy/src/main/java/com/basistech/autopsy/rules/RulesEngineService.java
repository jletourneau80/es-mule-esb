package com.basistech.autopsy.rules;

/**
 * Created by IntelliJ IDEA.
 * User: jletourneau
 * Date: Feb 24, 2010
 * Time: 10:58:08 AM
 * To change this template use File | Settings | File Templates.
 */
import javax.ws.rs.*;


import com.basistech.autopsy.HibernateUtil;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import java.util.List;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.KnowledgePackage;
import org.drools.definition.rule.Rule;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.StatelessKnowledgeSession;
import org.drools.runtime.rule.WorkingMemory;
import org.drools.command.CommandFactory;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import org.drools.runtime.ExecutionResults;


@Path("/rules")
public class RulesEngineService {

    
    @GET
    @Produces("application/json")
    @Path("/log/{logsession}")
    public String produceLogList(@PathParam("logsession") String logsession) throws java.net.MalformedURLException {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List result = session.createQuery("from LogEntry where session='" + logsession + "'").list();
        session.getTransaction().commit();
        String retString = "";


        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add( ResourceFactory.newClassPathResource( "anomalies.drl", getClass() ),
                      ResourceType.DRL );
        if ( kbuilder.hasErrors() ) {
            retString+= kbuilder.getErrors().toString();
        }

        final KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages( kbuilder.getKnowledgePackages() );
        StatelessKnowledgeSession ksession = kbase.newStatelessKnowledgeSession();
        Iterator it = kbuilder.getKnowledgePackages().iterator();

        //populate the rules list so we can determine if we fulfill all or not
        //need to store name, time
        HashMap<String,Date> list = new HashMap<String,Date>();
        while(it.hasNext()){
            KnowledgePackage e = (KnowledgePackage)it.next();
            Iterator r = e.getRules().iterator();
            while(r.hasNext()){
                Rule rule = (Rule)r.next();
                list.put(rule.getName(),null);    //null indicates no success yet
            }
        }



        ksession.setGlobal("list", list);

        ExecutionResults execResults = ksession.execute(CommandFactory.newInsertElements(result));

        


        //Map<String,Date> sortedResult = sortByValue(list);

        Iterator i = list.keySet().iterator();

        retString="{";
        
        Date gameStartDate = list.get("Game Start");
        while(i.hasNext()){
            String key = (String)i.next();
            if (list.get(key)!=null){
                retString+= "\"" + key + "\": \"" + (list.get(key).getTime() - gameStartDate.getTime()) + "\",";
            }else{
                retString+= "\"" + key + "\": \"null\",";
            }
        }

        retString=retString.substring(0,retString.length()-1) + "}";

        return retString;
    }


    Map<String,Date> sortByValue(Map map) {
     List list = new LinkedList(map.entrySet());
     Collections.sort(list, new Comparator() {
          public int compare(Object o1, Object o2) {
                if (((Map.Entry) (o1)).getValue() == null && ((Map.Entry) (o2)).getValue() == null) return 0;
                // assuming you want null values shown last 
                if (((Map.Entry) (o1)).getValue() != null && ((Map.Entry) (o2)).getValue() == null) return -1;
                if (((Map.Entry) (o1)).getValue() == null && ((Map.Entry) (o2)).getValue() != null) return 1;
               return ((Comparable) ((Map.Entry) (o1)).getValue())
              .compareTo(((Map.Entry) (o2)).getValue());
          }
     });
    // logger.info(list);
    Map result = new LinkedHashMap();
    for (Iterator it = list.iterator(); it.hasNext();) {
         Map.Entry entry = (Map.Entry)it.next();
         result.put(entry.getKey(), entry.getValue());
         }
    return result;
}

}

