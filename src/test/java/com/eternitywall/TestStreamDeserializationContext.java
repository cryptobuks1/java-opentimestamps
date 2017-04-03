package com.eternitywall;

import com.eternitywall.ots.OpenTimestamps;
import com.eternitywall.ots.StreamDeserializationContext;
import com.eternitywall.ots.StreamSerializationContext;
import com.eternitywall.ots.Timestamp;
import com.eternitywall.ots.attestation.PendingAttestation;
import com.eternitywall.ots.attestation.TimeAttestation;
import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by casatta on 24/03/17.
 */
public class TestStreamDeserializationContext {
    @Test
    public void testReadvaruint() {
        final byte[] uri = "https://ots.eternitywall.it".getBytes(StandardCharsets.US_ASCII);
        PendingAttestation pendingAttestation=new PendingAttestation(uri);

        StreamSerializationContext streamSerializationContext = new StreamSerializationContext();
        pendingAttestation.serialize(streamSerializationContext);
        //System.out.println( DatatypeConverter.printHexBinary(streamSerializationContext.getOutput() ).toLowerCase() );

        StreamDeserializationContext streamDeserializationContext =new StreamDeserializationContext(streamSerializationContext.getOutput());
        PendingAttestation pendingAttestationCheck = (PendingAttestation) TimeAttestation.deserialize(streamDeserializationContext);

        assertTrue(Arrays.equals(uri, pendingAttestationCheck.getUri()));
    }


    @Test
    public void test2() {
        byte []ots = DatatypeConverter.parseHexBinary("F0105C3F2B3F8524A32854E07AD8ADDE9C1908F10458D95A36F008088D287213A8B9880083DFE30D2EF90C8E2C2B68747470733A2F2F626F622E6274632E63616C656E6461722E6F70656E74696D657374616D70732E6F7267");
        byte []digest= DatatypeConverter.parseHexBinary("7aa9273d2a50dbe0cc5a6ccc444a5ca90c9491dd2ac91849e45195ae46f64fe352c3a63ba02775642c96131df39b5b85");

        //System.out.println("ots hex: " + DatatypeConverter.printHexBinary(ots));

        StreamDeserializationContext streamDeserializationContext = new StreamDeserializationContext(ots);
        Timestamp timestamp = Timestamp.deserialize(streamDeserializationContext, digest);
        //System.out.println(Timestamp.strTreeExtended(timestamp,2));

        StreamSerializationContext streamSerializationContext = new StreamSerializationContext();
        timestamp.serialize(streamSerializationContext);
        byte []otsBefore = streamSerializationContext.getOutput();
        //System.out.println("fullOts hex:" + DatatypeConverter.printHexBinary(otsBefore));

        //System.out.println("upgrading\n" + OpenTimestamps.info(timestamp));
        boolean changed = OpenTimestamps.upgrade(timestamp);
        if(changed){
            //System.out.println("Equals timestamps");
        }

    }
}