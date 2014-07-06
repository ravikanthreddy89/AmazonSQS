package org.ravikanth.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ravikanth.service.Credentials;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;

public class Main {

	// this is Url of Inbox Queue
	final static String inboxQueueUrl="REPLACE_THIS_WITH_YOUR_QUEUE_URL";
	final static String outboxQueueUrl="REPLACE_THIS_WITH_YOUR_QUEUE_URL";
	
	
	public static void main(String [] args){
		
		
		// Get Credentials
		AWSCredentials credentials=Credentials.getCredentials();
		//Get SQS client
		AmazonSQS sqs = new AmazonSQSClient(credentials);
		
		// Start processing the messages from inbox queue
		for(;;){
			// Receive messages
            System.out.println("Receiving messages from Inbox Queue.\n");
            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(inboxQueueUrl);
            List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
            
            if(messages.isEmpty()){
            	continue;
            }            
            else {
            for (Message message : messages) {

            	// temp variable to handle the integers in the message
            	//MessageAttributeValue a=null;
            	//MessageAttributeValue b=null;
            	
            	String a="";
            	String b="";
            	System.out.println("  Message");
                System.out.println("    MessageId:     " + message.getMessageId());
                System.out.println("    ReceiptHandle: " + message.getReceiptHandle());
                System.out.println("    MD5OfBody:     " + message.getMD5OfBody());
                System.out.println("    Body:          " + message.getBody());
                
                String receiptHandle= message.getReceiptHandle();
                String[] numbers=message.getBody().split(":");
                
                /*Map<String, MessageAttributeValue> e = message.getMessageAttributes();
                
                //for (Entry<String, MessageAttributeValue> entry: e.entrySet()) {
                	
                for(Entry<String, String> entry : message.getAttributes().entrySet()){
                    System.out.println("  Attribute");
                    String temp1="firstNumber";
                    String temp2="secondNumber";
                    
                    if(temp1.compareTo(entry.getKey())==0){
                    	a=entry.getValue().toString();
                    }
                    
                    if(temp2.compareTo(entry.getKey())==0){
                    	b=entry.getValue().toString();
                    }
                    System.out.println("    Name:  " + entry.getKey());
                    System.out.println("    Value: " + entry.getValue());
                }
                */
                // process the numbers                 
                String sum = numbers[0]+"+"+numbers[1];
                String diff= numbers[0]+"-"+numbers[1];
                
                System.out.println(sum);
                System.out.println(diff);
               
                Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
                messageAttributes.put("sum", new MessageAttributeValue().withDataType("String").withStringValue(sum));
                messageAttributes.put("diff", new MessageAttributeValue().withDataType("String").withStringValue(diff));
                
                
                SendMessageRequest request = new SendMessageRequest();
                request.withMessageBody("A test message body.");
                request.withQueueUrl(outboxQueueUrl);
                request.withMessageAttributes(messageAttributes);
                sqs.sendMessage(request);
                
                //delete the message from inbox queue
                sqs.deleteMessage(new DeleteMessageRequest(inboxQueueUrl, receiptHandle));


                }
            }
            System.out.println();

		}
	}
}
