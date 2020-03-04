package com.lctafrica.MemberSms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.twilio.type.PhoneNumber;

//import ch.qos.logback.core.boolex.Matcher;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("twilio")
public class TwilioSmsSender implements SmsSender {

	private static final Logger LOGGER = LoggerFactory.getLogger(TwilioSmsSender.class);
	private final TwilioConfiguration twilioConfiguration;

	@Autowired
	public TwilioSmsSender(TwilioConfiguration twilioConfiguration) {
		this.twilioConfiguration = twilioConfiguration;
	}

	// check valid number
	public static boolean isPhoneNumberValid(String s) {
		// TODO Auto-generated method stub
		Pattern p = Pattern.compile("^(?:254|\\+254|0)?(7(?:(?:[12][0-9])|(?:0[0-8])|(?:9[0-2]))[0-9]{6})$");
		Matcher m = p.matcher(s);
		return (m.find() && m.group().equals(s));
	}

	@Override
	public void sendSms(SmsRequest smsRequest) {
		try {
			// validation checks
			if (isPhoneNumberValid(smsRequest.getPhoneNumber())) {
				PhoneNumber to = new PhoneNumber(smsRequest.getPhoneNumber());
				PhoneNumber from = new PhoneNumber(twilioConfiguration.getTrialNumber());
				String message = smsRequest.getMessage();
				MessageCreator creator = Message.creator(to, from, message);
				creator.create();
				LOGGER.info("Send sms{}" + smsRequest);
			} else {
				throw new IllegalArgumentException(
						"Phone number [" + smsRequest.getPhoneNumber() + "] is  invalid. Coz Abdallah says so");
			}
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
}
