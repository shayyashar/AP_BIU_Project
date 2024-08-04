package graph;

import java.util.Date;

public class Message{
    public final byte[] data;
    public final String asText;
    public final double asDouble;
    public final Date date;

    public Message(String asText) {
        double asDouble1;
        this.asText = asText;
        try {
            asDouble1 = Double.parseDouble(asText);
        }
        catch (Exception e){
            asDouble1 = Double.NaN;
        }
        this.asDouble = asDouble1;
        this.data =  asText.getBytes();;

        this.date = new Date();
    }

    public Message(double asDouble){
        this(Double.toString(asDouble));
    }

    public Message(byte[] data){
        this(new String(data));
    }
}
