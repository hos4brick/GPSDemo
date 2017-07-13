package com.gps.demo.model.event;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.NtpV3Packet;
import org.apache.commons.net.ntp.TimeInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * Created by Jon on 6/30/2017.
 */

public class TimeDetectorThread extends Thread {
    // TODO potentially make a list of servers read from file
    // will a reading from different servers lead to synch problems or will they be close enough?
    private static final String NIST_TIME_SERVER = "time.nist.gov";
    private static final String DATE_FORMAT = "yyyy.MM.dd HH:mm:ss z";
    private static final int POLL_AMMOUNT = 3;
    private static final int POLL_DELAY = 1000;
    private Object waiter = new Object();

    private long utcOffset = 0;

    @Override
    public void run() {
        super.run();

        utcOffset = calculateUtcOffset();
    }

    public long calculateUtcOffset() {
        long offsets[] = new long[POLL_AMMOUNT];

        for (int i = 0; i < POLL_AMMOUNT; i++) {
            try {
                NTPUDPClient timeClient = new NTPUDPClient();
                InetAddress timeServerAddress = InetAddress.getByName(NIST_TIME_SERVER);
                TimeInfo timeInfo = timeClient.getTime(timeServerAddress);

                long preRequestTime = new Date().getTime();

                NtpV3Packet timePacket = timeInfo.getMessage();

                long postRequestTime = new Date().getTime();

                long serverTime = timePacket.getTransmitTimeStamp().getTime();

                long delay = postRequestTime - preRequestTime;  // timePacket.getRootDelay();

                // add the trip back time to the time reported by the server
                serverTime += (delay / 2);

                // offset is difference between device time and serverTime;
                offsets[i] = serverTime - postRequestTime;
                synchronized (waiter) {
                    waiter.wait(POLL_DELAY);
                }

            }
            catch (UnknownHostException unknownHostException) {
                unknownHostException.printStackTrace();
            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long offset = 0;

        for (int i = 0; i < POLL_AMMOUNT; i++) {
            offset += offsets[i];
        }

        offset = offset / POLL_AMMOUNT;
        System.out.println("OFFSET: " + offset);

        return offset;
    }

    public long getUtcOffset() {
        return utcOffset;
    }
}
