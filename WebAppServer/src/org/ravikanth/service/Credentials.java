package org.ravikanth.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;

public class Credentials {

	public static AWSCredentials getCredentials(){
		 /*
         * The ProfileCredentialsProvider will return your [ColumbiaAssignment]
         * credential profile by reading from the credentials file located at
         * (/root/.aws/credentials).
         */
        AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider("ColumbiaAssignment").getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (/root/.aws/credentials), and is in valid format.",
                    e);
        }
        return credentials;
	}
}
