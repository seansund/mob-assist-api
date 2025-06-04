import {Context} from '@loopback/context';
import {TWILIO_CONFIG, twilioConfig} from './twilio.config';
import {GMAIL_CONFIG, gmailConfig} from './gmail.config';
import {SENDGRID_CONFIG, sendgridConfig} from './sendgrid.config';

export * from './empty.config';
export * from './twilio.config';
export * from './gmail.config';
export * from './sendgrid.config';

export const bindConfig = (server: Context) => {
  server.bind(TWILIO_CONFIG).to(twilioConfig);
  server.bind(GMAIL_CONFIG).to(gmailConfig);
  server.bind(SENDGRID_CONFIG).to(sendgridConfig);
}
