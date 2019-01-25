import java.net.*;
import java.io.*;

public class CLChatServer {

   static final int DEFAULT_PORT = 1728;  // Port to listen on,
                                          // if none is specified
                                          // on the command line.
                                          
   static final String HANDSHAKE = "CLChat";  // Handshake string.
                   // Each end of the connection sends this string
                   // to the other just after the connection is 
                   // opened.  This is done to confirm that the
                   // program on the other side of the connection 
                   // is a CLChat program.
                   
   static final char MESSAGE = '0';  // This character is prepended
                                     // to every message that is sent.

   static final char CLOSE = '1';    // This character is sent to
                                     // the connected program when
                                     // the user quits.

   public static void main(String[] args) {
   
      int port;   // The port on which the server listens.
      
      ServerSocket listener;  // Listens for a connection request.
      Socket connection;      // For communication with the client.
      
      TextReader incoming;  // Stream for receiving data from client.
      PrintWriter outgoing; // Stream for sending data to client.
      String messageOut;    // A message to be sent to the client.
      String messageIn;     // A message received from the client.
      
      /* First, get the port number from the command line,
         or use the default port if none is specified. */
      
      if (args.length == 0) 
         port = DEFAULT_PORT;
      else {
         try {
            port= Integer.parseInt(args[0]);
            if (port < 0 || port > 65535)
               throw new NumberFormatException();
         }
         catch (NumberFormatException e) {
            TextIO.putln("Illegal port number, " + args[0]);
            return;
         }
      }
      
      /* Wait for a connection request.  When it arrives, close
         down the listener.  Create streams for communication
         and exchange the handshake. */
      
      try {
         listener = new ServerSocket(port);
         TextIO.putln("Listening on port " + listener.getLocalPort());
         connection = listener.accept();
         listener.close();  
         incoming = new TextReader(connection.getInputStream());
         outgoing = new PrintWriter(connection.getOutputStream());
         outgoing.println(HANDSHAKE);
         outgoing.flush();
         messageIn = incoming.getln();
         if (! messageIn.equals(HANDSHAKE) ) {
            throw new IOException("Connected program is not CLChat!");
         }
         TextIO.putln("Connected.  Waiting for the first message.
");
      }
      catch (Exception e) {
         TextIO.putln("An error occurred while opening connection.");
         TextIO.putln(e.toString());
         return;
      }
      
      /* Exchange messages with the other end of the connection
         until one side or the other closes the connection.
         This server program waits for the first message from
         the client.  After that, messages alternate strictly
         back an forth. */
      
      try {
         while (true) {
            TextIO.putln("WAITING...");
            messageIn = incoming.getln();
            if (messageIn.length() > 0) {
                  // The first character of the message is a command.
                  // If the command is CLOSE, then the connection
                  // is closed.  Otherwise, remove the command 
                  // character from the message and procede.
                if (messageIn.charAt(0) == CLOSE) {
                   TextIO.putln("Connection closed at other end.");
                   connection.close();
                   break;
                }
                messageIn = messageIn.substring(1);
            }
            TextIO.putln("RECEIVED:  " + messageIn);
            TextIO.put("SEND:      ");
            messageOut = TextIO.getln();
            if (messageOut.equalsIgnoreCase("quit"))  {
                  // User wants to quit.  Inform the other side
                  // of the connection, then close the connection.
                outgoing.println(CLOSE);
                outgoing.flush();  // Make sure the data is sent!
                connection.close();
                TextIO.putln("Connection closed.");
                break;
            }
            outgoing.println(MESSAGE + messageOut);
            outgoing.flush(); // Make sure the data is sent!
            if (outgoing.checkError()) {
               throw new IOException("Error ocurred while reading incoming message.");
            }
         }
      }
      catch (Exception e) {
         TextIO.putln("Sorry, an error has occurred.  Connection lost.");
         TextIO.putln(e.toString());
         System.exit(1);
      }
      
   }  // end main()
} //end
