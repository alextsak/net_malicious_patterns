package com.webserver.server;
  
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import com.packetMem.shared.AvailableNodes;
import com.packetMem.shared.MaliciousPatterns;
import com.packetMem.shared.StatisticalReports;

@WebService
@SOAPBinding(style=Style.DOCUMENT)
public interface WebMonitorService 
{     
	@WebMethod
	public boolean registerPC(String nodeID);
    public MaliciousPatterns maliciousPatternsRequest(String nodeID);
    public void maliciousPatternsStatisticalReport(String nodeID, StatisticalReports m);
    public boolean unregister(String nodeID);
    public List<StatisticalReports> retrieveStatistics(String username, String password);
    //public StatisticalReports[] retrieveStatistics(String username, String password);
    public String retrieveMaliciousPatterns(String username, String password);
    public void insertMaliciousPatterns(String username, String password, String maliciousIP, String stringPatterns);
    public int login(String username, String password);
    public boolean logout(String username, String password);
    public boolean delete(String username, String password, String nodeID);
    @WebMethod(operationName = "registerUser",action="SOAPAction")
    public boolean register(String username, String password, AvailableNodes nodes);
}