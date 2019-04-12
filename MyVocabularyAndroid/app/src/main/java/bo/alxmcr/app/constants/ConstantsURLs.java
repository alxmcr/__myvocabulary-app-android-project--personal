package bo.alxmcr.app.constants;

/**
 * Created by Jose Coca on 21/04/2015.
 */
public class ConstantsURLs {

    public static final String NAME_APP = "Backend_Vocabulary";

    /*Data URL's API - BASIC
       {0} : DOMAIN APP
       {1} : KEYWORD API 'PATH'
       {2} : KEYWORD API 'MODULE'
  */
    public static final String FORMAT_URL_API_BASIC = "http://{0}/{1}/{2}";

    /*Data URL's API - ADVANCED
        {0} : DOMAIN APP
        {1} : KEYWORD API 'PATH'
        {2} : KEYWORD API 'MODULE'
        {3} : KEYWORD API name to 'ACTION'
        {4} : KEYWORD API 'VALUE' to action
    */
    public static final String FORMAT_URL_API_ACTION_PLUS_VALUE = "http://{0}/{1}/{2}/{3}/{4}";

    /*Data URL's API - ADVANCED
       {0} : DOMAIN APP
       {1} : KEYWORD API 'PATH'
       {2} : KEYWORD API 'MODULE'
       {3} : KEYWORD API 'VALUE'
   */
    public static final String FORMAT_URL_API_VALUE = "http://{0}/{1}/{2}/{3}";

    //Data connection to server
    public static final String IP_SERVER_REMOTE = "murmuring-crag-4694.herokuapp.com";
    public static final String IP_SERVER_LOCAL = "192.168.1.33";
    public static final String PORT_SERVER = "8787";

    public static final String KEYWORD_API_PATH = "api";

    //Configuration 'Local'
    public static final String DOMAIN_APP_LOCAL = IP_SERVER_LOCAL + ":" + PORT_SERVER + "/" + NAME_APP;

    //Configuration 'Remote'
    public static final String DOMAIN_APP_REMOTE = IP_SERVER_REMOTE;

}

