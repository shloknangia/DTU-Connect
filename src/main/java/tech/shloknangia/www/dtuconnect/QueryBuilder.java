package tech.shloknangia.www.dtuconnect;

/**
 * Created by SHLOK on 16-04-2017.
 */
public class QueryBuilder {

    /**
     * Specify your database name here
     * @return
     */
    public String getDatabaseName() {
        return "donors";
    }

    /**
     * Specify your MongoLab API here
     * @return
     */
    public String getApiKey() {
        return "your_api_key";

    }

    /**
     * This constructs the URL that allows you to manage your database,
     * collections and documents
     * @return
     */
    public String getBaseUrl()
    {
        return "https://api.mongolab.com/api/1/databases/"+getDatabaseName()+"/collections/";
    }

    /**
     * Completes the formating of your URL and adds your API key at the end
     * @return
     */
    public String docApiKeyUrl()
    {
        return "?apiKey="+getApiKey();
    }

    /**
     * Get a specified document
     * @param docid
     * @return
     */
    public String docApiKeyUrl(String docid)
    {
        return "/"+docid+"?apiKey="+getApiKey();
    }

    /**
     * Returns the docs101 collection
     * @return
     */
    public String documentRequest()
    {
        return "main";
    }

    /**
     * Builds a complete URL using the methods specified above
     * @return
     */
    public String buildContactsSaveURL()
    {
        return getBaseUrl()+documentRequest()+docApiKeyUrl();
    }

    /**
     * This method is identical to the one above.
     * @return
     */
    public String buildContactsGetURL()
    {
        return getBaseUrl()+documentRequest()+docApiKeyUrl();
    }

    /**
     * Get a Mongodb document that corresponds to the given object id
     * @param doc_id
     * @return
     */
    public String buildContactsUpdateURL(String doc_id)
    {
        return getBaseUrl()+documentRequest()+docApiKeyUrl(doc_id);
    }


    /**
     * Formats the contact details for MongoHLab Posting
     * @param contact: Details of the person
     * @return
     */
    public String createContact(MyContact contact)
    {
        return String
                .format("{\"name\": \"%s\", \"email\": \"%s\", \"branch\": \"%s\", \"course\": \"%s\", \"year\": \"%s\", " +
                                "\"bloodgroup\": \"%s\", \"phone\": \"%s\", \"password\": \"%s\"}",
                        contact.name, contact.email, contact.branch, contact.course,
                        contact.year,contact.bloodgroup,contact.phone,contact.password);
    }

    /**
     * Update a given contact record
     * @param contact
     * @return
     */
    public String setContactData(MyContact contact) {
        return String.format("{ \"$set\" : "
                        + "{\"name\": \"%s\", \"email\": \"%s\", \"branch\": \"%s\", \"course\": \"%s\", \"year\": \"%s\", " +
                        "\"bloodgroup\": \"%s\", \"phone\": \"%s\", \"password\": \"%s\" }" +"}",
                contact.name, contact.email, contact.branch, contact.course,
                contact.year,contact.bloodgroup,contact.phone,contact.password);
    }
}
