/*
 * Utils.java
 *
 * Version: $Revision: 1.11 $
 *
 * Date: $Date: 2005/04/20 14:23:17 $
 *
 * Copyright (c) 2002-2005, Hewlett-Packard Company and Massachusetts
 * Institute of Technology.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * - Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * - Neither the name of the Hewlett-Packard Company nor the name of the
 * Massachusetts Institute of Technology nor the names of their
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package com.dgr.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.rmi.dgc.VMID;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;

public class Utils{
    private static int counter = 0;
    private static Random random = new Random();
    private static VMID vmid = new VMID();

    private Utils(){
    }
    
    public static boolean isUUID(String uuid){
    	return uuid.toLowerCase().matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}") ? true : false;
    }
    
    public static String getUUID(){
    	UUID uuid = UUID.randomUUID();
    	return uuid.toString();
    }

    public static String getMD5(String data)
    {
        return getMD5(data.getBytes());
    }

    public static String getMD5(byte[] data)
    {
        return toHex(getMD5Bytes(data));
    }

    public static byte[] getMD5Bytes(byte[] data)
    {
        try
        {
            MessageDigest digest = MessageDigest.getInstance("MD5");

            return digest.digest(data);
        }
        catch (NoSuchAlgorithmException nsae)
        {
        }

        // Should never happen
        return null;
    }

    public static String toHex(byte[] data)
    {
        if ((data == null) || (data.length == 0))
        {
            return null;
        }

        StringBuffer result = new StringBuffer();

        // This is far from the most efficient way to do things...
        for (int i = 0; i < data.length; i++)
        {
            int low = (int) (data[i] & 0x0F);
            int high = (int) (data[i] & 0xF0);

            result.append(Integer.toHexString(high).substring(0, 1));
            result.append(Integer.toHexString(low));
        }

        return result.toString();
    }

    public static String generateKey()
    {
        return new BigInteger(generateBytesKey()).abs().toString();
    }

    public static String generateHexKey()
    {
        return toHex(generateBytesKey());
    }

    public static synchronized byte[] generateBytesKey()
    {
        byte[] junk = new byte[16];

        random.nextBytes(junk);

        String input = new StringBuffer().append(vmid).append(
                new java.util.Date()).append(junk).append(counter++).toString();

        return getMD5Bytes(input.getBytes());
    }

    public static void copy(final InputStream input, final OutputStream output)
            throws IOException
    {
        final int BUFFER_SIZE = 1024 * 4;
        final byte[] buffer = new byte[BUFFER_SIZE];

        while (true)
        {
            final int count = input.read(buffer, 0, BUFFER_SIZE);

            if (-1 == count)
            {
                break;
            }

            // write out those same bytes
            output.write(buffer, 0, count);
        }

        //output.flush();
    }

    public static void bufferedCopy(final InputStream source,
            final OutputStream destination) throws IOException
    {
        final BufferedInputStream input = new BufferedInputStream(source);
        final BufferedOutputStream output = new BufferedOutputStream(
                destination);
        copy(input, output);
        output.flush();
    }

    public static String addEntities(String value)
    {
        value = value.replaceAll("&", "&amp;");
        value = value.replaceAll("\"", "&quot;");

        value = value.replaceAll("<", "&lt;");
        value = value.replaceAll(">", "&gt;");

        return value;
    }
    
    public static int getRandomNumber(){
        Random rand = new Random();
        int randomNum = rand.nextInt();

//        String result = String.valueOf(randomNum);
//        int pos = result.indexOf("-");
//        if(pos > -1){
//            result = result.substring(pos + 1);
//        }

        return randomNum;
    }

//    public static String getRandomNumber(){
//        Random rand = new Random();
//        int randomNum = rand.nextInt();
//
//        String result = String.valueOf(randomNum);
//        int pos = result.indexOf("-");
//        if(pos > -1){
//            result = result.substring(pos + 1);
//        }
//
//        return result;
//    }
}
