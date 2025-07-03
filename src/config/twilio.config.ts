import {EmptyConfig} from './empty.config';

export const TWILIO_CONFIG = 'config.TwilioConfig';

export interface TwilioConfig {
  accountSid: string;
  authToken: string;
  phoneNumber: string;
  messagePrefix: string;
  channel: string;
  valid: boolean;
}

const buildTwilioConfig = (): TwilioConfig | EmptyConfig => {
  const accountSid = process.env.TWILIO_ACCOUNT_SID;
  const authToken = process.env.TWILIO_AUTH_TOKEN;
  const phoneNumber = process.env.TWILIO_PHONE;

  if (!accountSid || !authToken || !phoneNumber) {
    return {
      valid: false,
    }
  }

  return {
    accountSid,
    authToken,
    phoneNumber,
    messagePrefix: process.env.TWILIO_MESSAGE_PREFIX ?? 'FBG: ',
    valid: !!accountSid && !!authToken && !!phoneNumber,
    channel: 'text',
  }
}

export const twilioConfig: TwilioConfig | EmptyConfig = buildTwilioConfig();
