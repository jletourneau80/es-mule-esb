package com.basistech.autopsy.rules;

/**
 * Created with IntelliJ IDEA.
 * User: jletourneau
 * Date: 2/28/14
 * Time: 12:32 PM
 * To change this template use File | Settings | File Templates.
 */
import com.basistech.autopsy.HibernateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonParser;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractTransformer;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import java.util.Date;

import java.util.List;


/**
 * <code>StringToNameString</code> converts from a String to a NameString object.
 */
public class LoggingTransformer extends AbstractTransformer
{
    private static final Log log = LogFactory.getLog(LoggingTransformer.class);
    public LoggingTransformer()
    {
        super();

    }

    @Override
    public Object doTransform(Object src, String encoding) throws TransformerException
    {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        JsonFactory factory = new JsonFactory(); // since 2.1 use mapper.getFactory() instead
        try{
            JsonParser parser = factory.createJsonParser((String)src);
            JsonNode actualObj = parser.readValueAsTree();
            LogEntry entry = new LogEntry();
            entry.setClassName(actualObj.get("name").getValueAsText());
            entry.setCurrentValue(actualObj.get("md5").getValueAsText());
            entry.setLevel(actualObj.get("known").getValueAsText());
            entry.setSession(actualObj.get("case").getValueAsText());
            entry.setTimeOccured(new Date());
            entry.setMessage(actualObj.get("examiner").getTextValue());
            session.save(entry);
        }catch(Exception e){
            log.error(e);
        }

        return src;
    }

}
