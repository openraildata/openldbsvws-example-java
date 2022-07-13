package com.openraildata;

import com.thalesgroup.rtti._2013_11_28.token.types.AccessToken;
import com.thalesgroup.rtti._2021_11_01.ldbsv.GetBoardByCRSParams;
import com.thalesgroup.rtti._2021_11_01.ldbsv.GetBoardResponseType;
import com.thalesgroup.rtti._2021_11_01.ldbsv.LDBSVServiceSoap;
import com.thalesgroup.rtti._2021_11_01.ldbsv.Ldbsv;
import com.thalesgroup.rtti._2021_11_01.ldbsv.types.ServiceItem;
import com.thalesgroup.rtti._2021_11_01.ldbsv.types.StationBoard;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.ext.logging.LoggingInInterceptor;
import org.apache.cxf.ext.logging.LoggingOutInterceptor;
import org.apache.cxf.frontend.ClientProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.ConfigurationException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Open Live Departure Boards Staff Version Web Service (OpenLDBSVWS) API Demonstrator
 * Copyright (C)2018-2022 OpenTrainTimes Ltd.
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

    private static final Logger logger = LoggerFactory.getLogger(GetDepartureBoardExample.class);

    private static final String LDB_TOKEN = "";
    private static final boolean DEBUG = false;

    public static void main(String[] args) throws ConfigurationException, DatatypeConfigurationException {

        if (LDB_TOKEN.isEmpty()) {
            throw new ConfigurationException("Please configure your OpenLDBSVWS token in GetDepartureBoardExample!");
        }

        AccessToken accessToken = new AccessToken();
        accessToken.setTokenValue(LDB_TOKEN);

        Ldbsv soap = new Ldbsv();
        LDBSVServiceSoap soapService = soap.getLDBSVServiceSoap12();

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

        logger.info("Trains at {}", stationBoard.getLocationName());
        logger.info("===============================================================================");

        List<ServiceItem> service = stationBoard.getTrainServices().getService();

        for (ServiceItem si : service) {

            logger.info("{} to {} - {}", si.getStd(), si.getDestination().getLocation().get(0).getLocationName(), si.getEtd());

        }

    }

}
