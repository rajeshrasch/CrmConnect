import java.io.*;
import java.util.*;
import java.net.*;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.PartSource;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;

public class fetchRecordsFromCrm

{   //Dynamically fetch the token each time the application connects to ZOHO database
	public static String dynamicTokenFetch(String CRM, String username, String pass)
    {
            String token = null;
            try {

                    String string1 ="https://accounts.zoho.com/apiauthtoken/nb/create?SCOPE="+CRM+"/crmapi&EMAIL_ID="+username+"&PASSWORD="+pass;
                    URL u = new URL(string1);
                    HttpURLConnection con = (HttpURLConnection)u.openConnection();
                    InputStream input = con.getInputStream();
                    InputStreamReader inputRead=new InputStreamReader(input);
                    BufferedReader br =new BufferedReader(inputRead);
                    String string2  = null;

                    while ((string2 = br.readLine()) != null) {
                            if(string2 != null && string2.startsWith("AUTHTOKEN")) {
                                    token = string2.substring(10);
                            }
                   }

                    input.close();
            }
            catch (Exception e){
                    e.printStackTrace();
                    System.out.println("Error in token Fetching,Please check your Credentials");

            }
            return token;
    }
	public static void main(String a[])
	{	
		try	
		{
			
			String authorization = fetchRecordsFromCrm.dynamicTokenFetch("ZohoCRM", "emailforrajesh@gmail.com","rajesh@12345"); 
			String apitype = "crmapi";
			//Records Range to fetch the information 
			String fromInd = "1";
			String toInd = "5";
			//XML format values can be 1 or 2 depending on the requirement
			String newFormat = "2";
			
            //Target URL
			String string3 ="Leads(Lead Owner,First Name,Last Name,Email,Company)";
			String URL = "https://crm.zoho.com/crm/private/xml/Leads/getRecords"; 
	       	PostMethod postobj = new PostMethod(URL);
			postobj.setParameter("authtoken",authorization);
			postobj.setParameter("scope",apitype);
			postobj.setParameter("newFormat",newFormat);
			postobj.setParameter("selectColumns",string3);
			postobj.setParameter("fromIndex",fromInd);
			postobj.setParameter("toIndex",toInd);
			HttpClient httpobj = new HttpClient();
			//printWriter object to create an external file that is placed in working directory named CRMLeadRecords.xml
			PrintWriter outwrite = null;

			try 
			{
				long t1 = System.currentTimeMillis();
				int result = httpobj.executeMethod(postobj);
				System.out.println("HTTP Response status code from ZOHO CRM: " + result);
				System.out.println(">> Time taken to Process  " + (System.currentTimeMillis() - t1)+" milliseconds");

				// writing the response to a file 
				outwrite = new PrintWriter(new File("CRMLeadRecords.xml"));
				outwrite.print(postobj.getResponseBodyAsString());

				//get response from the server
				String serverfetch = postobj.getResponseBodyAsString();
				System.out.println("Response from the ZOHO Server");
				System.out.println();
				System.out.println(serverfetch);
			}
			catch(Exception e)
			{
				System.out.println("Something Went wrong ");
				e.printStackTrace();
			}	
			finally 
			{
				//System.out.println(getRecords.getAuthToken("ZohoCRM", "emailforrajesh@gmail.com","rajesh@12345"));
				outwrite.close();
				postobj.releaseConnection();
			}
		}
		catch(Exception e)
		{
			System.out.println("Something Went wrong ");
			e.printStackTrace();
		}	
	}
	
	
}