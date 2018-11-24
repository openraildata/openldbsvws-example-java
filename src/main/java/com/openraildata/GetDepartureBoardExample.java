package com.openraildata;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import com.thalesgroup.rtti._2013_11_28.token.types.AccessToken;
import com.thalesgroup.rtti._2017_10_01.ldbsv.GetBoardByCRSParams;
import com.thalesgroup.rtti._2017_10_01.ldbsv.GetBoardResponseType;
import com.thalesgroup.rtti._2017_10_01.ldbsv.LDBSVServiceSoap;
import com.thalesgroup.rtti._2017_10_01.ldbsv.Ldbsv;
import com.thalesgroup.rtti._2017_10_01.ldbsv.types.ServiceItem;
import com.thalesgroup.rtti._2017_10_01.ldbsv.types.StationBoard;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

/**
 * Open Live Departure Boards Staff Version Web Service (OpenLDBWS) API Demonstrator
 * Copyright (C)2018 OpenTrainTimes Ltd.
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
public class GetDepartureBoardExample {

    private static final String LDB_TOKEN = "";
    private static final boolean DEBUG = false;

    public static void main(String[] args) throws DatatypeConfigurationException {

        if (LDB_TOKEN.isEmpty()) {
            throw new RuntimeException("Please configure your OpenLDBWS token in GetDepartureBoardExample!");
        }

        AccessToken accessToken = new AccessToken();
        accessToken.setTokenValue(LDB_TOKEN);

        Ldbsv soap = new Ldbsv();
        LDBSVServiceSoap soapService = soap.getLDBSVServiceSoap();

        /*
         * To examine the request and responses sent to the service, set DEBUG to true above
         */
        if (DEBUG) {
            Client client = ClientProxy.getClient(soapService);
            client.getInInterceptors().add(new LoggingInInterceptor());
            client.getOutInterceptors().add(new LoggingOutInterceptor());
        }

        GetBoardByCRSParams params = new GetBoardByCRSParams();
        params.setCrs("EUS");
        params.setNumRows(20);
        params.setTimeWindow(60);

        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
        XMLGregorianCalendar now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
        params.setTime(now);

        GetBoardResponseType departureBoardByCRS = soapService.getDepartureBoardByCRS(params, accessToken);
        StationBoard stationBoard = departureBoardByCRS.getGetBoardResult();

        System.out.println("Trains at " + stationBoard.getLocationName());
        System.out.println("===============================================================================");

        List<ServiceItem> service = stationBoard.getTrainServices().getService();

        for (ServiceItem si : service) {

            System.out.println(si.getStd() + " to " + si.getDestination().getLocation().get(0).getLocationName() + " - " + si.getEtd());

        }

    }

}
